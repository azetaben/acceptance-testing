package com.saucedemo.configReader;

import com.saucedemo.userTestData.UserTestData;
import com.saucedemo.utils.IconfigReader;
import org.apache.log4j.Level;

import java.util.Locale;
import java.util.Objects;

public class PropertyFileReader implements IconfigReader {
    private final FrameworkConfig config;

    public PropertyFileReader() {
        this.config = FrameworkConfig.getInstance();
    }

    public String getUserName() {
        return config.getString("username");
    }

    public String getPassword() {
        return config.getString("password");
    }

    public String getWebsite() {
        return config.getString("url");
    }

    public String getProperty(String key) {
        return config.getString(key, "");
    }

    public String getFileProperty(String key) {
        String value = config.asProperties().getProperty(key);
        return value == null ? "" : value.trim();
    }

    public String getRequiredProperty(String key) {
        String value = config.getString(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required config key is missing or blank: " + key);
        }
        return value;
    }

    public String resolveValue(String rawValue) {
        if (rawValue == null) {
            return "";
        }

        String trimmed = rawValue.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String normalized = unwrapToken(trimmed);
        String lowered = normalized.toLowerCase(Locale.ROOT);

        int beginIndex = normalized.indexOf(':') >= 0 ? normalized.indexOf(':') + 1 : normalized.indexOf('.') + 1;
        if (lowered.startsWith("config:") || lowered.startsWith("config.")) {
            String key = normalized.substring(beginIndex).trim();
            String fileValue = getFileProperty(key);
            return fileValue.isEmpty() ? getProperty(key) : fileValue;
        }

        if (lowered.startsWith("faker:") || lowered.startsWith("faker.")) {
            String token = normalized.substring(beginIndex).trim();
            return FakerUtils.resolveToken(token);
        }

        if (lowered.startsWith("user:") || lowered.startsWith("user.")
                || lowered.startsWith("userdata:") || lowered.startsWith("userdata.")) {
            String token = normalized.substring(beginIndex).trim();
            return resolveUserTestDataToken(token);
        }

        return trimmed;
    }

    public int getPageLoadTimeOut() {
        return config.getInt("pageLoadTimeOut", 30);
    }

    public int getImplicitWait() {
        return config.getInt("implicitWaitTimeout", 10);
    }

    public int getExplicitWait() {
        return config.getInt("explicitWaitTimeout", 15);
    }

    public Level getLoggerLevel() {

        return switch (Objects.requireNonNull(config.getString("Logger.Level", "ALL"))) {
            case "DEBUG" -> Level.DEBUG;
            case "INFO" -> Level.INFO;
            case "WARN" -> Level.WARN;
            case "ERROR" -> Level.ERROR;
            case "FATAL" -> Level.FATAL;
            default -> Level.ALL;
        };
    }

    private String resolveUserTestDataToken(String token) {
        if (token == null || token.isBlank()) {
            return "";
        }

        return switch (token.trim().toUpperCase(Locale.ROOT)) {
            case "STANDARD_USERNAME" -> UserTestData.STANDARD_USERNAME;
            case "LOCKED_OUT_USERNAME" -> UserTestData.LOCKED_OUT_USERNAME;
            case "PERFORMANCE_GLITCH_USERNAME" -> UserTestData.PERFORMANCE_GLITCH_USERNAME;
            case "PROBLEM_USERNAME" -> UserTestData.PROBLEM_USERNAME;
            case "INVALID_USERNAME" -> UserTestData.INVALID_USERNAME;
            case "VISUAL_USERNAME" -> UserTestData.VISUAL_USERNAME;
            case "PASSWORD_FOR_ALL_USERS" -> UserTestData.PASSWORD_FOR_ALL_USERS;
            case "PASSWORD" -> UserTestData.PASSWORD;
            case "LOGIN_ERROR_MESSAGE" -> UserTestData.LOGIN_ERROR_MESSAGE;
            case "LOGIN_PAGE_URL" -> UserTestData.LOGIN_PAGE_URL;
            case "LOGIN_BUTTON" -> UserTestData.LOGIN_BUTTON;
            case "CONTINUE_BUTTON" -> UserTestData.CONTINUE_BUTTON;
            case "INVENTORY_PAGE_TITLE" -> UserTestData.INVENTORY_PAGE_TITLE;
            case "INVENTORY_PAGE_URL" -> UserTestData.INVENTORY_PAGE_URL;
            case "CART_PAGE_URL" -> UserTestData.CART_PAGE_URL;
            case "CART_PAGE_TITLE" -> UserTestData.CART_PAGE_TITLE;
            case "CHECKOUT_STEP_ONE_PAGE_URL" -> UserTestData.CHECKOUT_STEP_ONE_PAGE_URL;
            case "CHECKOUT_STEP_ONE_PAGE_TITLE" -> UserTestData.CHECKOUT_STEP_ONE_PAGE_TITLE;
            case "CHECKOUT_STEP_TWO_PAGE_URL" -> UserTestData.CHECKOUT_STEP_TWO_PAGE_URL;
            case "CHECKOUT_STEP_TWO_PAGE_TITLE" -> UserTestData.CHECKOUT_STEP_TWO_PAGE_TITLE;
            case "CHECKOUT_COMPLETE_PAGE_URL" -> UserTestData.CHECKOUT_COMPLETE_PAGE_URL;
            case "CHECKOUT_COMPLETE_PAGE_TITLE" -> UserTestData.CHECKOUT_COMPLETE_PAGE_TITLE;

            default -> token;
        };
    }

    private String unwrapToken(String token) {
        if (token.startsWith("${") && token.endsWith("}")) {
            return token.substring(2, token.length() - 1).trim();
        }
        return token;
    }
}
