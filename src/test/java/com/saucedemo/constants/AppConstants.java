package com.saucedemo.constants;

import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.utils.PathUtil;

public final class AppConstants {

    private AppConstants() {
    }

    public final static int DEFAULT_TIME_OUT = 5;
    public final static int SHORT_TIME_OUT = 10;
    public final static int MEDIUM_TIME_OUT = 15;
    public final static int MAX_TIME_OUT = 20;

    public final static String LOGIN_PAGE_TITLE = FrameworkConfig.getInstance().getString("login_page_title", "Account Login");
    public final static String LOGIN_PAGE_URL_FRACTION = FrameworkConfig.getInstance().getString("login_page_url_fraction", "route=account/login");

    public final static String HOME_PAGE_TITLE = FrameworkConfig.getInstance().getString("home_page_title", "My Account");
    public final static String HOME_PAGE_URL_FRACTION = FrameworkConfig.getInstance().getString("home_page_url_fraction", "route=account/account");

    public static final String CONFIG_PROD_FILE_PATH = PathUtil.getConfigFilePath("config.properties");
    public static final String CONFIG_QA_PROP_FILE_PATH = PathUtil.getConfigFilePath("qa.config.properties");
    public static final String CONFIG_DEV_PROP_FILE_PATH = PathUtil.getConfigFilePath("dev.config.properties");
    public static final String CONFIG_STAGE_PROP_FILE_PATH = PathUtil.getConfigFilePath("stage.config.properties");
    public static final String CONFIG_UAT_PROP_FILE_PATH = PathUtil.getConfigFilePath("uat.config.properties");


    //*********sheet names*******//
    public static final String PRODUCT_SHEET_NAME = "product";

}
