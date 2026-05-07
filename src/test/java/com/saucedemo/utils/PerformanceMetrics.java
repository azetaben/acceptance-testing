package com.saucedemo.utils;

import com.saucedemo.constants.FrameworkConstants;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.v102.performance.Performance;
import org.openqa.selenium.devtools.v102.performance.model.Metric;

import org.openqa.selenium.support.ui.WebDriverWait;

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


    private static DevTools activeDevToolsInstance;
    private static List<Metric> lastCollectedMetricsList;


    public static List<Metric> collectLoadPerformanceMetrics(WebDriver driver, String url) {
        if (!(driver instanceof ChromeDriver chromeDriver)) {
            LOGGER.warning("DevTools performance metrics are only available for ChromeDriver. Received: " +
                    (driver != null ? driver.getClass().getName() : "null driver"));
            lastCollectedMetricsList = Collections.emptyList();
            return lastCollectedMetricsList;
        }

        DevTools devTools = chromeDriver.getDevTools();
        try {
            devTools.createSession();
            devTools.send(Performance.enable(Optional.empty()));
            LOGGER.info("Performance metrics collection enabled for URL: " + url);

            driver.get(url);
            LOGGER.info("Navigated to URL: " + url);


            new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                    .until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));

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

            if (devTools != null && devTools.getCdpSession() != null) {
                try {
                    devTools.send(Performance.disable());
                    LOGGER.info("Performance metrics collection disabled for URL: " + url);
                } catch (DevToolsException e) {
                    LOGGER.log(Level.WARNING, "DevToolsException while disabling performance metrics: " + e.getMessage(), e);
                }
            }


        }
        return lastCollectedMetricsList;
    }


    public static void startPerformanceRecording(WebDriver driver) {
        if (!(driver instanceof ChromeDriver chromeDriver)) {
            LOGGER.warning("DevTools performance metrics are only available for ChromeDriver. Cannot start recording. Received: " +
                    (driver != null ? driver.getClass().getName() : "null driver"));
            activeDevToolsInstance = null;
            return;
        }


        activeDevToolsInstance = chromeDriver.getDevTools();
        try {
            activeDevToolsInstance.createSession();
            activeDevToolsInstance.send(Performance.enable(Optional.empty()));
            LOGGER.info("Performance recording started for interaction.");
        } catch (DevToolsException e) {
            LOGGER.log(Level.SEVERE, "DevToolsException while starting performance recording: " + e.getMessage(), e);
            activeDevToolsInstance = null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while starting performance recording: " + e.getMessage(), e);
            activeDevToolsInstance = null;
        }
    }


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

            try {
                activeDevToolsInstance.send(Performance.disable());
                LOGGER.info("Performance recording stopped and metrics disabled.");
            } catch (DevToolsException e) {
                LOGGER.log(Level.WARNING, "DevToolsException while disabling performance metrics after interaction: " + e.getMessage(), e);
            }

            activeDevToolsInstance = null;
        }
        return lastCollectedMetricsList;
    }


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
                .map(Metric::getValue)
                .map(Number::doubleValue)
                .findFirst();
    }


    public static List<Metric> getLastCollectedMetrics() {
        return lastCollectedMetricsList == null ? Collections.emptyList() : Collections.unmodifiableList(lastCollectedMetricsList);
    }
}
