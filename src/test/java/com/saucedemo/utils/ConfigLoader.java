package com.saucedemo.utils;

import com.saucedemo.enums.EnvType;

import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader configLoader;
    private final Properties properties;

    private ConfigLoader() {
        String env = System.getProperty("env", String.valueOf(EnvType.STAGE));
        switch (EnvType.valueOf(env)) {
            case PROD ->
                    properties = PropertyUtils.propertyLoader(PathUtil.getConfigFilePath("prod_config.properties"));
            case STAGE ->
                    properties = PropertyUtils.propertyLoader(PathUtil.getConfigFilePath("stage_config.properties"));
            default -> throw new IllegalStateException("INVALID ENV: " + env);
        }
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            synchronized (ConfigLoader.class) {
                if (configLoader == null) {
                    configLoader = new ConfigLoader();
                }
            }
        }
        return configLoader;
    }

    public String getBaseUrl() {
        String prop = properties.getProperty("baseUrl");
        if (prop != null) return prop;
        else throw new RuntimeException("property baseUrl is not specified in the stage_config.properties file");
    }
}
