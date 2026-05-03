package com.saucedemo.utils;

import com.google.common.util.concurrent.Uninterruptibles;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.v102.performance.Performance;
import org.openqa.selenium.devtools.v102.performance.model.Metric;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerformanceMetrics {
    private static final Logger LOGGER = Logger.getLogger(PerformanceMetrics.class.getName());
    private static final String PERFORMANCE_METRICS_KEY = "performance_metrics";
    private static final String METRIC_EVENT_TIME_KEY = "event_time";
    private static final String METRIC_VALUE_KEY = "value";
    private static final String METRIC_UNIT_KEY = "unit";
    private static final String METRIC_COUNT_KEY = "count";
    private static final String METRIC_THRESHOLD_KEY = "threshold";
    private static final String METRIC_CONTEXT_KEY = "context_key";

    // --- Thread Safety Note ---
    // The static variables below (activeDevToolsInstance, lastCollectedMetricsList)
    // are NOT thread-safe. If you run tests in parallel, you will need to
    // refactor this to use ThreadLocal<DevTools> and ThreadLocal<List<Metric>>
    // or manage DevTools instances per thread at a higher level.
    private static DevTools activeDevToolsInstance; // For interaction recording
    private static List<Metric> lastCollectedMetricsList; // Stores the most recently collected metrics

    /**
     * Collects performance metrics after navigating to a URL.
     *
     * @param driver The WebDriver instance (must be ChromeDriver).
     * @param url    The URL to navigate to.
     * @return A list of collected performance metrics, or an empty list on error.
     */
    public static List<Metric> collectLoadPerformanceMetrics(WebDriver driver, String url) {
        if (!(driver instanceof ChromeDriver chromeDriver)) {
            LOGGER.warning("DevTools performance metrics are only available for ChromeDriver. Received: " +
                    (driver != null ? driver.getClass().getName() : "null driver"));
            lastCollectedMetricsList = Collections.emptyList();
            return lastCollectedMetricsList;
        }

        DevTools devTools = chromeDriver.getDevTools();
        try {
            devTools.createSession(); // Create a new DevTools session
            devTools.send(Performance.enable(Optional.empty()));
            LOGGER.info("Performance metrics collection enabled for URL: " + url);

            driver.get(url);
            LOGGER.info("Navigated to URL: " + url);

            // Wait for metrics to stabilize. Adjust duration as needed or use more sophisticated waits.
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3)); // Example: 3-second wait

            lastCollectedMetricsList = devTools.send(Performance.getMetrics());
            LOGGER.info("Collected " + (lastCollectedMetricsList != null ? lastCollectedMetricsList.size() : 0) +
                    " metrics after page load.");

        } catch (DevToolsException e) {
            LOGGER.log(Level.SEVERE, "DevToolsException while collecting load performance metrics for " + url + ": " + e.getMessage(), e);
            lastCollectedMetricsList = Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while collecting load performance metrics for " + url + ": " + e.getMessage(), e);
            lastCollectedMetricsList = Collections.emptyList();
        } finally {
            // Always attempt to disable performance metrics if DevTools instance was obtained
            if (devTools != null && devTools.getCdpSession() != null) { // Check if session is active
                try {
                    devTools.send(Performance.disable());
                    LOGGER.info("Performance metrics collection disabled for URL: " + url);
                } catch (DevToolsException e) {
                    LOGGER.log(Level.WARNING, "DevToolsException while disabling performance metrics: " + e.getMessage(), e);
                }
            }
            // Consider if devTools.close() is needed here or managed with WebDriver lifecycle.
            // Generally, DevTools sessions are tied to the WebDriver session.
        }
        return lastCollectedMetricsList;
    }

    /**
     * Starts performance recording for an interaction.
     *
     * @param driver The WebDriver instance (must be ChromeDriver).
     */
    public static void startPerformanceRecording(WebDriver driver) {
        if (!(driver instanceof ChromeDriver chromeDriver)) {
            LOGGER.warning("DevTools performance metrics are only available for ChromeDriver. Cannot start recording. Received: " +
                    (driver != null ? driver.getClass().getName() : "null driver"));
            activeDevToolsInstance = null; // Ensure it's null if we can't proceed
            return;
        }

        // Potentially re-initialize or get a fresh DevTools instance for the interaction
        activeDevToolsInstance = chromeDriver.getDevTools();
        try {
            activeDevToolsInstance.createSession(); // Create a new DevTools session
            activeDevToolsInstance.send(Performance.enable(Optional.empty()));
            LOGGER.info("Performance recording started for interaction.");
        } catch (DevToolsException e) {
            LOGGER.log(Level.SEVERE, "DevToolsException while starting performance recording: " + e.getMessage(), e);
            activeDevToolsInstance = null; // Nullify if setup failed
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while starting performance recording: " + e.getMessage(), e);
            activeDevToolsInstance = null; // Nullify if setup failed
        }
    }

    /**
     * Stops performance recording and retrieves the collected metrics.
     *
     * @return A list of collected performance metrics, or an empty list if not started or on error.
     */
    public static List<Metric> stopPerformanceRecordingAndGetMetrics() {
        if (activeDevToolsInstance == null) {
            LOGGER.warning("Performance recording was not started or DevTools instance is not available. Cannot stop recording.");
            lastCollectedMetricsList = Collections.emptyList();
            return lastCollectedMetricsList;
        }

        try {
            lastCollectedMetricsList = activeDevToolsInstance.send(Performance.getMetrics());
            LOGGER.info("Collected " + (lastCollectedMetricsList != null ? lastCollectedMetricsList.size() : 0) +
                    " metrics during interaction.");
        } catch (DevToolsException e) {
            LOGGER.log(Level.SEVERE, "DevToolsException while getting metrics during interaction: " + e.getMessage(), e);
            lastCollectedMetricsList = Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while getting metrics during interaction: " + e.getMessage(), e);
            lastCollectedMetricsList = Collections.emptyList();
        } finally {
            // Always attempt to disable performance metrics
            try {
                activeDevToolsInstance.send(Performance.disable());
                LOGGER.info("Performance recording stopped and metrics disabled.");
            } catch (DevToolsException e) {
                LOGGER.log(Level.WARNING, "DevToolsException while disabling performance metrics after interaction: " + e.getMessage(), e);
            }
            // activeDevToolsInstance.close(); // Manage DevTools session lifecycle with WebDriver session.
            activeDevToolsInstance = null; // Clear for next recording
        }
        return lastCollectedMetricsList;
    }

    /**
     * Extracts a specific metric's value from a list of metrics.
     *
     * @param metrics    The list of metrics to search within.
     * @param metricName The name of the metric to find.
     * @return An Optional containing the metric's value as a Double, or empty if not found.
     */
    public static Optional<Double> getMetricValue(List<Metric> metrics, String metricName) {
        if (metrics == null || metricName == null) {
            if (metrics == null) LOGGER.finer("getMetricValue received null metrics list.");
            if (metricName == null) LOGGER.finer("getMetricValue received null metricName.");
            return Optional.empty();
        }
        if (metrics.isEmpty()) {
            LOGGER.finer("getMetricValue received empty metrics list for metricName: " + metricName);
            return Optional.empty();
        }
        return metrics.stream()
                .filter(m -> metricName.equalsIgnoreCase(m.getName()))
                .map(Metric::getValue) // Metric::getValue returns Number
                .map(Number::doubleValue) // Convert to Double for consistent handling
                .findFirst();
    }

    /**
     * Gets the most recently collected list of metrics.
     * Useful if steps need to access metrics without re-triggering collection.
     *
     * @return The last list of metrics collected, or an empty list if none were collected or an error occurred.
     */
    public static List<Metric> getLastCollectedMetrics() {
        return lastCollectedMetricsList == null ? Collections.emptyList() : Collections.unmodifiableList(lastCollectedMetricsList);
    }
}