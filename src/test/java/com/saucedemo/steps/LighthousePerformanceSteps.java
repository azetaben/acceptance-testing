package com.saucedemo.steps;
import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LighthousePerformanceSteps {
    private static final Logger log = LogManager.getLogger(LighthousePerformanceSteps.class);

    private static final ThreadLocal<Double> PERFORMANCE_SCORE = new ThreadLocal<>();
    private static final ThreadLocal<String> LIGHTHOUSE_FAILURE = new ThreadLocal<>();
    private final PageManager pm = PageManager.getInstance();

    static Double getPerformanceScore() {
        return PERFORMANCE_SCORE.get();
    }

    static String getLighthouseFailure() {
        return LIGHTHOUSE_FAILURE.get();
    }

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    private boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    private List<String> tokenizeCommand(String commandLine) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|'([^']*)'|(\\S+)").matcher(commandLine);
        while (matcher.find()) {
            String token = matcher.group(1);
            if (token == null) {
                token = matcher.group(2);
            }
            if (token == null) {
                token = matcher.group(3);
            }
            if (token != null && !token.isBlank()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private Optional<String> findExecutableOnPath(String... candidates) {
        String pathValue = System.getenv("PATH");
        if (pathValue == null || pathValue.isBlank()) {
            return Optional.empty();
        }

        for (String directory : pathValue.split(Pattern.quote(File.pathSeparator))) {
            if (directory.isBlank()) {
                continue;
            }

            for (String candidate : candidates) {
                Path candidatePath = Paths.get(directory, candidate);
                if (Files.isRegularFile(candidatePath)) {
                    return Optional.of(candidatePath.toString());
                }
            }
        }

        return Optional.empty();
    }

    private List<String> resolveLighthouseCommand() {
        String configuredCommand = FrameworkConfig.getInstance().getString(SauceDemoConstants.LIGHTHOUSE_COMMAND_PROPERTY);
        if (configuredCommand != null && !configuredCommand.isBlank()) {
            List<String> configuredTokens = tokenizeCommand(configuredCommand);
            if (!configuredTokens.isEmpty()) {
                return configuredTokens;
            }
        }

        String legacyEnvironmentCommand = System.getenv(SauceDemoConstants.LIGHTHOUSE_COMMAND_ENV);
        if (legacyEnvironmentCommand != null && !legacyEnvironmentCommand.isBlank()) {
            List<String> legacyTokens = tokenizeCommand(legacyEnvironmentCommand);
            if (!legacyTokens.isEmpty()) {
                return legacyTokens;
            }
        }

        if (isWindows()) {
            Optional<String> lighthouseExecutable = findExecutableOnPath("lighthouse.cmd", "lighthouse.exe", "lighthouse");
            if (lighthouseExecutable.isPresent()) {
                return List.of(lighthouseExecutable.get());
            }

            Optional<String> npxExecutable = findExecutableOnPath("npx.cmd", "npx.exe", "npx");
            if (npxExecutable.isPresent()) {
                return List.of(npxExecutable.get(), "--yes", "lighthouse");
            }
        } else {
            Optional<String> lighthouseExecutable = findExecutableOnPath("lighthouse");
            if (lighthouseExecutable.isPresent()) {
                return List.of(lighthouseExecutable.get());
            }

            Optional<String> npxExecutable = findExecutableOnPath("npx");
            if (npxExecutable.isPresent()) {
                return List.of(npxExecutable.get(), "--yes", "lighthouse");
            }
        }

        throw new IllegalStateException(
                "Unable to locate Lighthouse CLI. Install it globally, ensure 'npx' is on PATH, " +
                        "or configure the executable via system property/config key '" + SauceDemoConstants.LIGHTHOUSE_COMMAND_PROPERTY +
                        "' or legacy env '" + SauceDemoConstants.LIGHTHOUSE_COMMAND_ENV + "'."
        );
    }

    private Path lighthouseReportPath() {
        return Paths.get(PathUtil.getLighthouseReportPath());
    }

    @Given("I navigate to url {string}")
    public void i_navigate_to_url(String url) {
        WebDrv.getInstance().openBrowser(url);
    }

    @When("I run Lighthouse audit for {string}")
    public void i_run_lighthouse_audit_for(String url) {
        PERFORMANCE_SCORE.remove();
        LIGHTHOUSE_FAILURE.remove();

        try {
            Path reportPath = lighthouseReportPath();
            Files.createDirectories(reportPath.getParent());
            Files.deleteIfExists(reportPath);

            List<String> command = new ArrayList<>(resolveLighthouseCommand());
            command.add(url);
            command.add("--quiet");
            command.add("--chrome-flags=--headless");
            command.add("--preset=desktop");
            command.add("--output=json");
            command.add("--output-path=" + reportPath);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder commandOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    commandOutput.append(line).append(System.lineSeparator());
                    log.info(String.valueOf(line));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int exitCode = process.waitFor();
            boolean reportExists = Files.exists(reportPath) && Files.size(reportPath) > 0L;
            if (exitCode != 0 && !reportExists) {
                throw new IllegalStateException(
                        "Lighthouse audit failed with exit code " + exitCode + ". Command: " + command +
                                System.lineSeparator() + commandOutput
                );
            }

            if (exitCode != 0) {
                log.warn("Lighthouse returned non-zero exit code " + exitCode +
                        " but produced a report. Proceeding with parsed results.");
            } else {
                log.info(String.valueOf("Lighthouse audit completed with exit code: " + exitCode));
            }

            PERFORMANCE_SCORE.set(parseLighthouseReport());
        } catch (IOException e) {
            String message = "Unable to execute Lighthouse audit for URL '" + url + "': " + e.getMessage();
            LIGHTHOUSE_FAILURE.set(message);
            throw new IllegalStateException(message, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String message = "Lighthouse audit was interrupted for URL '" + url + "'.";
            LIGHTHOUSE_FAILURE.set(message);
            throw new IllegalStateException(message, e);
        } catch (RuntimeException e) {
            LIGHTHOUSE_FAILURE.set(e.getMessage());
            throw e;
        }
    }

    private double parseLighthouseReport() {
        try {
            Path reportPath = lighthouseReportPath();
            if (!Files.exists(reportPath)) {
                throw new IllegalStateException("Lighthouse report was not created at: " + reportPath);
            }
            if (Files.size(reportPath) == 0L) {
                throw new IllegalStateException("Lighthouse report is empty at: " + reportPath);
            }

            String content = Files.readString(reportPath);
            JSONObject jsonReport = new JSONObject(content);
            return jsonReport
                    .getJSONObject("categories")
                    .getJSONObject("performance")
                    .getDouble("score");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse Lighthouse report: " + e.getMessage(), e);
        }
    }

    @And("I login as performance glitch user {string} and {string}")
    public void iLoginAsPerformanceGlitchUserAnd(String username, String password) {
        loginPage().login(username, password);
    }
}
