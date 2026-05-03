package com.saucedemo.configReader;

/**
 * @deprecated Use {@link ConfigReader#getProperty(String)} or
 *             {@link ConfigReader#getRequiredProperty(String)} instead.
 *             This class is a passthrough duplicate and will be removed.
 */
@Deprecated(forRemoval = true)
public class PropertiesReader {
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();

    public static String getProperty(String key) {
        return CONFIG.getString(key);
    }
}