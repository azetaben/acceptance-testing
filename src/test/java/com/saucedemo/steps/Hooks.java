package com.saucedemo.steps;

import com.saucedemo.helperutilities.logger.LoggerHelper;
import com.saucedemo.pages.PageManager;
import com.saucedemo.performance.PerformanceNavigationContext;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.*;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class Hooks {

    private static volatile boolean startupLogCheckPrinted = false;
    private final Logger log = LoggerHelper.getLogger(Hooks.class);


    @Before(order = 0)
    public void validateDriverState(Scenario scenario) {
        if (WebDrv.getInstance().getWebDriver() != null) {
            log.warn("Stale WebDriver detected before scenario '"
                    + scenario.getName() + "'. Forcing cleanup.");
            WebDrv.getInstance().closeCurrentDriver();
            PageManager.getInstance().clear();
        }
    }


    @Before(order = 1)
    public void initializeLogging(Scenario scenario) {
        logLoggingBootstrapDetails();
    }


    @Before(order = 2)
    public void beginScenario(Scenario scenario) {
        log.info("▶ Starting Scenario: [" + scenario.getId() + "] " + scenario.getName());
        scenario.log("Scenario started: " + scenario.getName());
        PerformanceNavigationContext.beginScenario(scenario.getName());
    }


    @BeforeStep(order = 1)
    public void logStepStart(Scenario scenario) {
        log.debug("  → Step starting in scenario: " + scenario.getName());
    }


    @AfterStep(order = 1)
    public void recordNavigationAfterStep(Scenario scenario) {
        PerformanceNavigationContext.recordIfUrlChanged(
                WebDrv.getInstance().getWebDriver(),
                scenario.getName(),
                "afterStep");
    }


    @After(order = 2)
    public void captureScreenshot(Scenario scenario) {
        attachScreenshot(scenario);
    }


    @After(order = 1)
    public void capturePerformanceMetrics(Scenario scenario) {
        attachPerfTimings(scenario);
    }


    @After(order = 0)
    public void tearDownDriver(Scenario scenario) {
        closeDriver();
        log.info("■ Finished Scenario: "
                + scenario.getName()
                + " — " + (scenario.isFailed() ? "FAILED" : "PASSED"));
    }


    private void attachPerfTimings(Scenario scenario) {
        try {
            PerformanceNavigationContext.recordIfUrlChanged(
                    WebDrv.getInstance().getWebDriver(),
                    scenario.getName(),
                    "afterScenario");

            String json = PerformanceNavigationContext.toJson();
            scenario.attach(
                    json.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    "application/json",
                    "navigation-timings");

            Path outDir = Paths.get(PathUtil.getProjectRootDir(), "target", "performance-metrics");
            PerformanceNavigationContext.flushToFile(outDir);
        } catch (Exception e) {
            log.warn("Failed to attach navigation timing metrics for '"
                    + scenario.getName() + "': " + e.getMessage(), e);
        } finally {
            PerformanceNavigationContext.clear();
        }
    }

    private void attachScreenshot(Scenario scenario) {
        WebDriver driver = WebDrv.getInstance().getWebDriver();
        if (driver == null) {
            log.warn("No active WebDriver — skipping screenshot for: " + scenario.getName());
            return;
        }
        String label = scenario.isFailed() ? "Failed Scenario Screenshot" : "Passed Scenario Screenshot";
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", label);
            log.info("Screenshot attached [" + label + "] for: " + scenario.getName());
        } catch (WebDriverException e) {
            log.error("Failed to capture screenshot for '"
                    + scenario.getName() + "': " + e.getMessage(), e);
        }
    }

    private void closeDriver() {
        if (WebDrv.getInstance().getWebDriver() != null) {
            log.info("Closing the browser...");
            WebDrv.getInstance().closeCurrentDriver();
            PageManager.getInstance().clear();
            log.info("Browser closed.");
        }
    }

    private void logLoggingBootstrapDetails() {
        if (startupLogCheckPrinted) {
            return;
        }
        synchronized (Hooks.class) {
            if (startupLogCheckPrinted) {
                return;
            }
            Logger rootLogger = Logger.getRootLogger();
            List<String> appenders = new ArrayList<>();
            Enumeration<?> en = rootLogger.getAllAppenders();
            while (en != null && en.hasMoreElements()) {
                Object next = en.nextElement();
                if (next instanceof Appender appender) {
                    appenders.add(appender.getName() + "(" + appender.getClass().getSimpleName() + ")");
                }
            }
            if (appenders.isEmpty()) {
                appenders.add("NONE");
            }

            String explicitConfig = System.getProperty("log4j.configurationFile");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL log4j2Resource = cl.getResource("log4j2.properties");
            URL log4j1Resource = cl.getResource("config/log4j.properties");

            log.info("[LOGGER-BOOTSTRAP]"
                    + " level=" + rootLogger.getLevel()
                    + ", appenders=" + appenders
                    + ", -Dlog4j.configurationFile=" + (explicitConfig != null ? explicitConfig : "<not-set>")
                    + ", classpath:log4j2.properties=" + (log4j2Resource != null ? log4j2Resource : "<not-found>")
                    + ", classpath:config/log4j.properties=" + (log4j1Resource != null ? log4j1Resource : "<not-found>"));

            startupLogCheckPrinted = true;
        }
    }
}
