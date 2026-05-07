package com.saucedemo.performance;

import com.saucedemo.constants.FrameworkConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NavigationTimingCollector {
    private static final Logger log = LogManager.getLogger(NavigationTimingCollector.class);

    private NavigationTimingCollector() {
    }

    public static NavigationTimingEntry collect(WebDriver driver, String scenarioName, String trigger) {
        if (driver == null) {
            return null;
        }
        if (!(driver instanceof JavascriptExecutor js)) {
            log.warn("Driver does not support JavascriptExecutor; cannot collect navigation timings.");
            return null;
        }

        waitForDocumentComplete(driver);

        @SuppressWarnings("unchecked")
        Map<String, Object> raw = (Map<String, Object>) js.executeScript(
                "try {" +
                        "  const url = window.location.href;" +
                        "  const nav = (performance.getEntriesByType('navigation')||[])[0];" +
                        "  if (!nav) { return { url: url, missing: true }; }" +
                        "  const out = {" +
                        "    url: url," +
                        "    type: nav.type," +
                        "    startTime: nav.startTime," +
                        "    requestStart: nav.requestStart," +
                        "    responseStart: nav.responseStart," +
                        "    domContentLoadedEventEnd: nav.domContentLoadedEventEnd," +
                        "    loadEventEnd: nav.loadEventEnd," +
                        "    transferSize: nav.transferSize," +
                        "    encodedBodySize: nav.encodedBodySize," +
                        "    decodedBodySize: nav.decodedBodySize" +
                        "  };" +
                        "  return out;" +
                        "} catch (e) { return { error: String(e) }; }"
        );

        Map<String, Object> safeRaw = raw == null ? Collections.emptyMap() : new LinkedHashMap<>(raw);
        String url = asString(safeRaw.get("url"));
        Double requestStart = asDouble(safeRaw.get("requestStart"));
        Double responseStart = asDouble(safeRaw.get("responseStart"));
        Double dclEnd = asDouble(safeRaw.get("domContentLoadedEventEnd"));
        Double loadEnd = asDouble(safeRaw.get("loadEventEnd"));

        NavigationTimingEntry entry = new NavigationTimingEntry();
        entry.setScenarioName(scenarioName);
        entry.setTrigger(trigger);
        entry.setUrl(url);
        entry.setCollectedAtEpochMs(System.currentTimeMillis());

        if (requestStart != null && responseStart != null) {
            entry.setTtfbMs(clampNonNegative(responseStart - requestStart));
        }
        if (dclEnd != null) {
            entry.setDomContentLoadedMs(clampNonNegative(dclEnd));
        }
        if (loadEnd != null) {
            entry.setLoadEventMs(clampNonNegative(loadEnd));
        }

        entry.setTransferSizeBytes(asLong(safeRaw.get("transferSize")));
        entry.setEncodedBodySizeBytes(asLong(safeRaw.get("encodedBodySize")));
        entry.setDecodedBodySizeBytes(asLong(safeRaw.get("decodedBodySize")));
        entry.setRaw(safeRaw);
        return entry;
    }

    private static void waitForDocumentComplete(WebDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                    .until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
        } catch (Exception e) {

            log.debug("Timed out waiting for document.readyState=complete before collecting timings: " + e.getMessage());
        }
    }

    private static Double clampNonNegative(Double v) {
        if (v == null) return null;
        return v < 0 ? 0.0 : v;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static Double asDouble(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(String.valueOf(o));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Long asLong(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.longValue();
        try {
            return Long.parseLong(String.valueOf(o));
        } catch (Exception ignored) {
            return null;
        }
    }
}
