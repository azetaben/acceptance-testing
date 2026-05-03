package com.saucedemo.interfaces;

import com.saucedemo.configReader.FrameworkConfig;

/**
 * A concrete implementation of IconfigReader that fetches values from FrameworkConfig.
 */
public class ConfigReaderImpl implements IconfigReader {
    private final FrameworkConfig config;

    public ConfigReaderImpl() {
        this.config = FrameworkConfig.getInstance();
    }

    @Override
    public String getUsername() {
        return config.getString("username");
    }

    @Override
    public String getPassword() {
        return config.getString("password");
    }

    @Override
    public String getWebsite() {
        return config.getString("url");
    }

    @Override
    public int getPageLoadTimeOut() {
        return config.getInt("pageLoadTimeOut", 30);
    }

    @Override
    public int getImplicitWait() {
        return config.getInt("implicitWaitTimeout", 10);
    }

    @Override
    public int getExplicitWait() {
        return config.getInt("explicitWaitTimeout", 15);
    }
}
