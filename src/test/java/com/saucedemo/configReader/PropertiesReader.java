package com.saucedemo.configReader;

@Deprecated(forRemoval = true)
public class PropertiesReader {
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();

    public static String getProperty(String key) {
        return CONFIG.getString(key);

    }
}