package com.saucedemo.configreader;

import com.saucedemo.utils.PathUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private final FrameworkConfig config;

    public ConfigReader() {
        this.config = FrameworkConfig.getInstance();
    }

    public String getProperty(String key) {
        String value = config.getString(key);
        if (value == null) {
            log.warn("Optional property not found: '" + key + "' in " + PathUtil.getConfigPropertiesPath());
        }
        return value;
    }

    public String getRequiredProperty(String key) {
        String value = config.getString(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Required config property '" + key + "' is missing or empty in " + PathUtil.getConfigPropertiesPath()
            );
        }
        return value;
    }
}
