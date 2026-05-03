package com.saucedemo.constants;

import com.saucedemo.configReader.FrameworkConfig;

public final class SauceDemoConstants {
    public static final String LIGHTHOUSE_COMMAND_PROPERTY = "lighthouse.command";
    public static final String LIGHTHOUSE_COMMAND_ENV = "LIGHTHOUSE_CMD";
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();
    public static final String INVENTORY_PAGE_PATH = CONFIG.getString("inventory_page_path");
    public static final String CHECKOUT_OVERVIEW_PAGE_PATH = CONFIG.getString("checkout_overview_page_path");
    public static final String MENU_ITEM_LOGOUT = CONFIG.getString("menu_item_logout");
    public static final String BUTTON_LABEL_LOGIN = CONFIG.getString("login_button");
    public static final String BUTTON_LABEL_CONTINUE = CONFIG.getString("continue_button");
    public static final String BUTTON_LABEL_CANCEL = CONFIG.getString("cancel_button");
    public static final String BUTTON_KEY_LOGIN = CONFIG.getString("button_key_login");
    public static final String HEADING_PRODUCTS = CONFIG.getString("heading.products");
    public static final String HEADING_YOUR_CART = CONFIG.getString("heading.cart");
    public static final String HEADING_CHECKOUT_YOUR_INFORMATION = CONFIG.getString("heading.checkout_your_information");
    public static final String HEADING_CHECKOUT_OVERVIEW = CONFIG.getString("heading.checkout_overview");
    public static final String HEADING_CHECKOUT_COMPLETE = CONFIG.getString("heading.checkout_complete");
    public static final String EMPTY_CART_COUNT = CONFIG.getString("empty_cart_count");
    public static final String DEFAULT_PRODUCT_QUANTITY = CONFIG.getString("default_product_quantity");
    public static final String DEFAULT_PRODUCT_PRICE = CONFIG.getString("default_product_price");
    public static final String DEFAULT_ORDER_TOTAL = CONFIG.getString("default_order_total");
    public static final String CURRENCY_SYMBOL = CONFIG.getString("currency_symbol");
    public static final String LIGHTHOUSE_REPORT_PATH = CONFIG.getString("lighthouse.report.path");

    // User accounts — values sourced from config.properties (override via env vars)
    public static final String USER_STANDARD = CONFIG.getString("standard_user", "standard_user");
    public static final String USER_LOCKED_OUT = CONFIG.getString("locked_out_user", "locked_out_user");
    public static final String USER_PROBLEM = CONFIG.getString("problem_user", "problem_user");
    public static final String USER_PERFORMANCE_GLITCH = CONFIG.getString("performance_glitch_user", "performance_glitch_user");
    public static final String USER_ERROR = CONFIG.getString("error_user", "error_user");
    public static final String USER_VISUAL = CONFIG.getString("visual_user", "visual_user");
    public static final String USER_PASSWORD = CONFIG.getString("password", "secret_sauce");
    public static final String USER_INVALID = "invalid_user";

    // Login error messages
    public static final String ERR_LOCKED_OUT = "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERR_WRONG_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
    public static final String ERR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String ERR_PASSWORD_REQUIRED = "Epic sadface: Password is required";

    private SauceDemoConstants() {
    }
}
