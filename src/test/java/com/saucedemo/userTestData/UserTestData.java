package com.saucedemo.usertestdata;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.utils.PathUtil;

public class UserTestData {


    public static final String FIRSTNAME = FrameworkConfig.getInstance().getString("checkout.first_name");
    public static final String LASTNAME = FrameworkConfig.getInstance().getString("checkout.last_name");
    public static final String EMAIL = FrameworkConfig.getInstance().getString("checkout.email");
    public static final String POSTAL_OR_ZIP_CODE = FrameworkConfig.getInstance().getString("checkout.postal_or_zip_code");

    public static final String LOGIN_PAGE_URL = FrameworkConfig.getInstance().getString("login_url");
    public static final String STANDARD_USERNAME = FrameworkConfig.getInstance().getString("standard_user");
    public static final String LOCKED_OUT_USERNAME = FrameworkConfig.getInstance().getString("locked_out_user");
    public static final String PERFORMANCE_GLITCH_USERNAME = FrameworkConfig.getInstance().getString("performance_glitch_user");
    public static final String PROBLEM_USERNAME = FrameworkConfig.getInstance().getString("problem_user");
    public static final String INVALID_USERNAME = FrameworkConfig.getInstance().getString("invalid_user");
    public static final String VISUAL_USERNAME = FrameworkConfig.getInstance().getString("visual_user");
    public static final String PASSWORD_FOR_ALL_USERS = FrameworkConfig.getInstance().getString("password_for_all_users");
    public static final String PASSWORD = FrameworkConfig.getInstance().getString("password");
    public static final String LOGIN_BUTTON = FrameworkConfig.getInstance().getString("login_button");
    public static final String CONTINUE_BUTTON = FrameworkConfig.getInstance().getString("continue_button");
    public static final String LOGIN_ERROR_MESSAGE = FrameworkConfig.getInstance().getString("login_error_message", "Epic sadface: Username and password do not match any user in this service");

    public static String INVENTORY_PAGE_TITLE = "Products";
    public static String INVENTORY_PAGE_URL = FrameworkConfig.getInstance().getString("inventory_url");
    public static String CART_PAGE_URL = FrameworkConfig.getInstance().getString("cart_url");
    public static String CART_PAGE_TITLE = FrameworkConfig.getInstance().getString("cart_title");
    public static String CHECKOUT_STEP_ONE_PAGE_URL = FrameworkConfig.getInstance().getString("checkout_step_one_url");
    public static String CHECKOUT_STEP_ONE_PAGE_TITLE = FrameworkConfig.getInstance().getString("checkout_step_one_title");
    public static String CHECKOUT_STEP_TWO_PAGE_URL = FrameworkConfig.getInstance().getString("checkout_step_two_url");
    public static String CHECKOUT_STEP_TWO_PAGE_TITLE = FrameworkConfig.getInstance().getString("checkout_step_two_title");
    public static String CHECKOUT_COMPLETE_PAGE_URL = FrameworkConfig.getInstance().getString("checkout_complete_url");
    public static String CHECKOUT_COMPLETE_PAGE_TITLE = FrameworkConfig.getInstance().getString("checkout_complete_title");


    public static String APPLICATIONS_DOWNLOAD_FILEPATH = PathUtil.getDownloadsDir();
    public static String APPLICATIONS_UPLOAD_FILEPATH = PathUtil.getUploadsDir();
    public static String REPORTING_CSV_DOWNLOAD_FILEPATH = PathUtil.getReportingCsvDir();
    public static String APPLICATIONS_DOWNLOAD_FILENAME = "-Application forms.pdf";


}
