package com.saucedemo.helperutilities.globalvar;

import com.saucedemo.configreader.FrameworkConfig;

public class GlobalVarsHelper {

    public static final int IMPLICIT_WAIT_TIME = 10;
    public static final int PAGE_LOAD_TIME = 15;
    public static final int EXPLICIT_WAIT_BASIC_TIME = 30;
    public static final String ALL_CHARACTERS_VALIDATION = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz!#$%&'*+-/=?^_`\\:#£:@][{|}~;.,()";
    private static final GlobalVarsHelper ourInstance = new GlobalVarsHelper();
    public static String URL_HOMEPAGE = FrameworkConfig.getInstance().getString("url");
    public static String BASE_URL = FrameworkConfig.getInstance().getString("login_url", URL_HOMEPAGE);
    public static String PRODUCT_NAME_1 = FrameworkConfig.getInstance().getString("product.name.1");
    public static String PRODUCT_NAME_2 = FrameworkConfig.getInstance().getString("product.name.2");
    public static String LASTNAME = FrameworkConfig.getInstance().getString("checkout.last_name");
    public static String FIRSTNAME = FrameworkConfig.getInstance().getString("checkout.first_name");
    public static int explicitWait = 15;
    public static int EXPLICIT_WAIT = 10;
    public static int SHORT_IMPLICIT_WAIT_TIME = 5;
    public static int TWO = 2;
    public static int ONE = 1;
    public static int IMPLICIT_TIMEOUT = 10;
    public static int IMPLICIT_WAIT_TIMEOUT = 1;
    public static int DEFAULT_EXPLICIT_TIMEOUT = 15;
    public static int EXPLICIT_TIMEOUT = 30;
    public static int POLLING_TIMEOUT = 250;
    public static int PAGE_LOAD_TIMEOUT = 120;
    public static int DEFAULT_IMPLICIT_TIMEOUT = 7;
    public static int implicitWait = 10;
    private String userUrl;

    private GlobalVarsHelper() {
    }

    public static GlobalVarsHelper getInstance() {
        return ourInstance;
    }

    public static String getLoginPageUrl() {
        return BASE_URL;
    }

    public static String getProductName1() {
        return PRODUCT_NAME_1;
    }

    public static String getProductName2() {
        return PRODUCT_NAME_2;
    }

    public static int getShortImplicitWaitTime() {
        return SHORT_IMPLICIT_WAIT_TIME;
    }

    public static int getImplicitTimeout() {
        return IMPLICIT_TIMEOUT;
    }

    public static int getDefaultExplicitTimeout() {
        return DEFAULT_EXPLICIT_TIMEOUT;
    }

    public static int getExplicitTimeout() {
        return EXPLICIT_TIMEOUT;
    }

    public static int getPollingTimeout() {
        return POLLING_TIMEOUT;
    }

    public static int getPageLoadTimeout() {
        return PAGE_LOAD_TIMEOUT;
    }

    public static String getStandardUser() {
        return FrameworkConfig.getInstance().getString("standard_user");
    }

    public static String getLockedOutUser() {
        return FrameworkConfig.getInstance().getString("locked_out_user");
    }

    public static String getProblemUser() {
        return FrameworkConfig.getInstance().getString("problem_user");
    }

    public static String getPerformanceGlitchUser() {
        return FrameworkConfig.getInstance().getString("performance_glitch_user");
    }

    public static String getErrorUser() {
        return FrameworkConfig.getInstance().getString("error_user");
    }

    public static String getVisualUser() {
        return FrameworkConfig.getInstance().getString("visual_user");
    }

    public static String getPasswordForAllUsers() {
        return FrameworkConfig.getInstance().getString("secret_sauce");
    }

    public static String getURL() {
        return FrameworkConfig.getInstance().getString("url");
    }

    public String getUserURL() {
        return userUrl;
    }

    public void setUserURL(String url) {
        this.userUrl = url;
    }
}
