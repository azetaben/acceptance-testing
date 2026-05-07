package com.saucedemo.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class PerformanceNavigationContext {
    private static final Logger log = LogManager.getLogger(PerformanceNavigationContext.class);
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ThreadLocal<State> STATE = ThreadLocal.withInitial(State::new);

    private PerformanceNavigationContext() {
    }

    public static void beginScenario(String scenarioName) {
        State s = STATE.get();
        s.scenarioName = scenarioName;
        s.lastRecordedUrl = null;
        s.entries.clear();
    }

    public static void recordIfUrlChanged(WebDriver driver, String scenarioName, String trigger) {
        if (driver == null) {
            return;
        }
        String currentUrl;
        try {
            currentUrl = driver.getCurrentUrl();
        } catch (Exception e) {
            return;
        }
        if (currentUrl == null || currentUrl.isBlank()) {
            return;
        }

        State s = STATE.get();
        if (currentUrl.equals(s.lastRecordedUrl)) {
            return;
        }

        NavigationTimingEntry entry = NavigationTimingCollector.collect(driver, scenarioName, trigger);
        if (entry == null) {
            s.lastRecordedUrl = currentUrl;
            return;
        }

        if (entry.getUrl() != null && entry.getUrl().equals(s.lastRecordedUrl)) {
            return;
        }

        s.entries.add(entry);
        s.lastRecordedUrl = entry.getUrl() != null ? entry.getUrl() : currentUrl;
        log.info("[PERF] navigation recorded: url=" + s.lastRecordedUrl
                + ", ttfbMs=" + entry.getTtfbMs()
                + ", loadEventMs=" + entry.getLoadEventMs());
    }

    public static List<NavigationTimingEntry> getEntriesSnapshot() {
        State s = STATE.get();
        return List.copyOf(s.entries);
    }

    public static File flushToFile(Path outputDir) {
        State s = STATE.get();
        if (s.entries.isEmpty()) {
            return null;
        }

        try {
            Files.createDirectories(outputDir);
            String safeName = safeFileName(s.scenarioName == null ? "scenario" : s.scenarioName);
            String ts = String.valueOf(Instant.now().toEpochMilli());
            Path out = outputDir.resolve(ts + "_" + safeName + "_navtimings.json");
            byte[] payload = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(s.entries);
            Files.write(out, payload);
            return out.toFile();
        } catch (Exception e) {
            log.warn("Failed to write navigation timing metrics: " + e.getMessage(), e);
            return null;
        }
    }

    public static String toJson() {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(getEntriesSnapshot());
        } catch (Exception e) {
            return "[]";
        }
    }

    public static void clear() {
        STATE.remove();
    }

    private static String safeFileName(String s) {

        return s.replaceAll("[^a-zA-Z0-9._-]+", "_");
    }

    private static final class State {
        final List<NavigationTimingEntry> entries = new ArrayList<>();
        String scenarioName;
        String lastRecordedUrl;
    }
}
