package com.saucedemo.performance;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigationTimingEntry {
    private String scenarioName;
    private String trigger; // e.g. afterStep, navigateTo, refresh, back, forward
    private String url;

    // Epoch millis when we collected the metrics (useful for correlating across runs/logs).
    private long collectedAtEpochMs;

    // Navigation Timing (PerformanceNavigationTiming) - values are in milliseconds.
    private Double ttfbMs;
    private Double domContentLoadedMs;
    private Double loadEventMs;

    // Sizes (bytes) when available.
    private Long transferSizeBytes;
    private Long encodedBodySizeBytes;
    private Long decodedBodySizeBytes;

    // Optional: raw payload in case you want to extend later without changing schema.
    private Map<String, Object> raw;

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCollectedAtEpochMs() {
        return collectedAtEpochMs;
    }

    public void setCollectedAtEpochMs(long collectedAtEpochMs) {
        this.collectedAtEpochMs = collectedAtEpochMs;
    }

    public Double getTtfbMs() {
        return ttfbMs;
    }

    public void setTtfbMs(Double ttfbMs) {
        this.ttfbMs = ttfbMs;
    }

    public Double getDomContentLoadedMs() {
        return domContentLoadedMs;
    }

    public void setDomContentLoadedMs(Double domContentLoadedMs) {
        this.domContentLoadedMs = domContentLoadedMs;
    }

    public Double getLoadEventMs() {
        return loadEventMs;
    }

    public void setLoadEventMs(Double loadEventMs) {
        this.loadEventMs = loadEventMs;
    }

    public Long getTransferSizeBytes() {
        return transferSizeBytes;
    }

    public void setTransferSizeBytes(Long transferSizeBytes) {
        this.transferSizeBytes = transferSizeBytes;
    }

    public Long getEncodedBodySizeBytes() {
        return encodedBodySizeBytes;
    }

    public void setEncodedBodySizeBytes(Long encodedBodySizeBytes) {
        this.encodedBodySizeBytes = encodedBodySizeBytes;
    }

    public Long getDecodedBodySizeBytes() {
        return decodedBodySizeBytes;
    }

    public void setDecodedBodySizeBytes(Long decodedBodySizeBytes) {
        this.decodedBodySizeBytes = decodedBodySizeBytes;
    }

    public Map<String, Object> getRaw() {
        return raw;
    }

    public void setRaw(Map<String, Object> raw) {
        this.raw = raw;
    }
}

