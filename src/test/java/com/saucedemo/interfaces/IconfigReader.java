package com.saucedemo.interfaces;

/**
 * Interface for reading configuration values.
 * Includes default methods as a login implementation example.
 */
public interface IconfigReader {

    String getUsername();

    String getPassword();

    String getWebsite();

    int getPageLoadTimeOut();

    int getImplicitWait();

    int getExplicitWait();

    /**
     * Example of a default method to get a full login URL.
     */
    default String getLoginUrl() {
        return getWebsite();
    }

    /**
     * Example of a default method to check if login credentials are provided.
     */
    default boolean hasLoginCredentials() {
        return getUsername() != null && !getUsername().isBlank()
                && getPassword() != null && !getPassword().isBlank();
    }
}
