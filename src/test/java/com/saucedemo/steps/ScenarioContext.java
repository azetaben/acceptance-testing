package com.saucedemo.steps; // Or your common utility package

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScenarioContext {
    private final Map<String, Object> contextData;

    public ScenarioContext() {
        contextData = new ConcurrentHashMap<>();
    }

    public void set(String key, Object value) {
        contextData.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) contextData.get(key);
    }

    public boolean containsKey(String key) {
        return contextData.containsKey(key);
    }
}