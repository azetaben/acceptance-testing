package com.saucedemo.configReader;

import com.saucedemo.utils.PathUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class FrameworkConfig {
    private static final FrameworkConfig INSTANCE = new FrameworkConfig();

    private final Properties properties = new Properties();

    private FrameworkConfig() {
        Path configPath = Path.of(PathUtil.getConfigPropertiesPath());
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load framework configuration from " + configPath, exception);
        }
    }

    public static FrameworkConfig getInstance() {
        return INSTANCE;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        String value = firstNonBlank(
                System.getProperty(key),
                System.getenv(toEnvironmentKey(key)),
                properties.getProperty(key),
                defaultValue
        );
        return value == null ? null : value.trim();
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public Properties asProperties() {
        Properties copy = new Properties();
        copy.putAll(properties);
        return copy;
    }

    private String toEnvironmentKey(String key) {
        return key.replace('.', '_')
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}

