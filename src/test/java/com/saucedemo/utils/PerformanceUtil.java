package com.saucedemo.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public final class PerformanceUtil {

    private static final Logger log = LogManager.getLogger(PerformanceUtil.class);

    private PerformanceUtil() {
    }

    public static long measurePageLoadTime(WebDriver driver, String url) {
        // Warmup run — primes DNS cache and browser JIT so first-load skew is excluded
        driver.get(url);

        // Timed run using nanoTime (immune to wall-clock adjustments)
        long startNs = System.nanoTime();
        driver.get(url);
        long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;

        // Navigation Timing API — server-side breakdown when available
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> timing = (Map<String, Object>) ((JavascriptExecutor) driver)
                    .executeScript(
                        "var t = window.performance.timing;" +
                        "return {" +
                        "  ttfb: t.responseStart - t.requestStart," +
                        "  domContentLoaded: t.domContentLoadedEventEnd - t.navigationStart," +
                        "  loadEvent: t.loadEventEnd - t.navigationStart" +
                        "};"
                    );
            if (timing != null) {
                log.info("Navigation Timing - TTFB: " + timing.get("ttfb") +
                         "ms, DOMContentLoaded: " + timing.get("domContentLoaded") +
                         "ms, LoadEvent: " + timing.get("loadEvent") + "ms");
            }
        } catch (Exception e) {
            log.debug("Navigation Timing API unavailable: " + e.getMessage());
        }

        return elapsedMs;
    }
}