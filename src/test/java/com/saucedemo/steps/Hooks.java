package com.saucedemo.steps;

import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.pages.PageManager;
import com.saucedemo.performance.PerformanceNavigationContext;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
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

/**
 * Lifecycle hooks with explicit ordering.
 *
 * @Before  (lower order → runs earlier)   : 0 → 1 → 2
 * @After   (higher order → runs earlier)  : 2 → 1 → 0
 * @BeforeStep / @AfterStep follow the same rules as Before/After.
 */
public class Hooks {

    private static volatile boolean startupLogCheckPrinted = false;
    private final Logger log = LoggerHelper.getLogger(Hooks.class);

    // =========================================================================
    // @Before  —  order 0 → 1 → 2  (lower runs first)
    // =========================================================================

    /**
     * Order 0 — Driver guard.
     * Must run first: any stale WebDriver from a crashed previous scenario is
     * force-closed so subsequent hooks always start with a null driver.
     */
    @Before(order = 0)
    public void validateDriverState(Scenario scenario) {
        if (WebDrv.getInstance().getWebDriver() != null) {
            log.warn("Stale WebDriver detected before scenario '"
                    + scenario.getName() + "'. Forcing cleanup.");
            WebDrv.getInstance().closeCurrentDriver();
            PageManager.getInstance().clear();
        }
    }

    /**
     * Order 1 — Logging diagnostics.
     * Prints the log4j bootstrap state once per JVM run; safe to call before
     * the driver exists because it only inspects the Logger infrastructure.
     */
    @Before(order = 1)
    public void initializeLogging(Scenario scenario) {
        logLoggingBootstrapDetails();
    }

    /**
     * Order 2 — Scenario initialisation.
     * Runs last among @Before hooks; the driver guard and logging are already
     * in place, so this hook only sets up scenario-level tracking.
     */
    @Before(order = 2)
    public void beginScenario(Scenario scenario) {
        log.info("▶ Starting Scenario: [" + scenario.getId() + "] " + scenario.getName());
        scenario.log("Scenario started: " + scenario.getName());
        PerformanceNavigationContext.beginScenario(scenario.getName());
    }

    // =========================================================================
    // @BeforeStep  —  lower order runs first
    // =========================================================================

    /**
     * Order 1 — Step-start diagnostic.
     * Logs a breadcrumb before every step to make log output easier to follow
     * when a scenario fails mid-way.
     */
    @BeforeStep(order = 1)
    public void logStepStart(Scenario scenario) {
        log.debug("  → Step starting in scenario: " + scenario.getName());
    }

    // =========================================================================
    // @AfterStep  —  higher order runs first
    // =========================================================================

    /**
     * Order 1 — Navigation timing recorder.
     * Captures performance metrics whenever the URL changes after a step.
     * Runs after the default (order 0) framework teardown if any exists.
     */
    @AfterStep(order = 1)
    public void recordNavigationAfterStep(Scenario scenario) {
        PerformanceNavigationContext.recordIfUrlChanged(
                WebDrv.getInstance().getWebDriver(),
                scenario.getName(),
                "afterStep");
    }

    // =========================================================================
    // @After  —  order 2 → 1 → 0  (higher runs first)
    // =========================================================================

    /**
     * Order 2 — Screenshot capture (runs first in teardown).
     * Must execute before the driver is closed; attaches a pass/fail
     * screenshot to the Cucumber report while the browser is still open.
     */
    @After(order = 2)
    public void captureScreenshot(Scenario scenario) {
        attachScreenshot(scenario);
    }

    /**
     * Order 1 — Performance metrics (runs second in teardown).
     * Flushes navigation timing data to the report and to disk before the
     * driver session ends, so the timings are still accessible on failure.
     */
    @After(order = 1)
    public void capturePerformanceMetrics(Scenario scenario) {
        attachPerfTimings(scenario);
    }

    /**
     * Order 0 — Driver teardown (runs last in teardown).
     * Closes the browser only after screenshot and metrics are safely captured.
     * Logs the final pass/fail state of the scenario.
     */
    @After(order = 0)
    public void tearDownDriver(Scenario scenario) {
        closeDriver();
        log.info("■ Finished Scenario: "
                + scenario.getName()
                + " — " + (scenario.isFailed() ? "FAILED" : "PASSED"));
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

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

            String explicitConfig  = System.getProperty("log4j.configurationFile");
            ClassLoader cl         = Thread.currentThread().getContextClassLoader();
            URL log4j2Resource     = cl.getResource("log4j2.properties");
            URL log4j1Resource     = cl.getResource("config/log4j.properties");

            log.info("[LOGGER-BOOTSTRAP]"
                    + " level="      + rootLogger.getLevel()
                    + ", appenders=" + appenders
                    + ", -Dlog4j.configurationFile=" + (explicitConfig != null ? explicitConfig : "<not-set>")
                    + ", classpath:log4j2.properties="         + (log4j2Resource != null ? log4j2Resource : "<not-found>")
                    + ", classpath:config/log4j.properties="   + (log4j1Resource != null ? log4j1Resource : "<not-found>"));

            startupLogCheckPrinted = true;
        }
    }
}