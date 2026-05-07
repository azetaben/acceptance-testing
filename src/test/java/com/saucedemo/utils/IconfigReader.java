package com.saucedemo.utils;

public interface IConfigReader {

    String getUserName();

    String getPassword();

    String getWebsite();

    int getPageLoadTimeOut();

    int getImplicitWait();

    int getExplicitWait();
}
