package com.saucedemo.helperUtilities.globalVar;


import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.utils.PathUtil;

public class GlobalVarsHelper {

    public static final int IMPLICIT_WAIT_TIME = 10;
    public static final int PAGE_LOAD_TIME = 15;
    public static final int EXPLICIT_WAIT_BASIC_TIME = 30;
    // All characters validation
    public static final String ALL_CHARACTERS_VALIDATION = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz!#$%&'*+-/=?^_`\\:#\u00A3:@][{|}~;.,()";
    public static String CONFIG_PROPERTIES_DIRECTORY = "properties\\config.properties";
    public static String DATA_PROVIDER_JSONPATH = PathUtil.getTestDataJsonFilePath("Register.json");
    public static String URL_HOMEPAGE = FrameworkConfig.getInstance().getString("url");
    public static String BASE_URL = FrameworkConfig.getInstance().getString("login_url", URL_HOMEPAGE);
    public static String BROWSER = FrameworkConfig.getInstance().getString("browserType", "chrome");
    public static String REGISTER_JSON_PATH = "//src//testScripts//java//opencart//testData.data//createAnAccount.json";
    public static String EMAIL = FrameworkConfig.getInstance().getString("checkout.email");
    public static String wrong_password = "Password_Wrong";
    public static String pass_word = "Password";
    public static String CONTINUE_BUTTON = FrameworkConfig.getInstance().getString("continue_button");
    public static String ACCOUNT_LOGOUT_HEADER = "Account Logout.feature";
    public static String ACCOUNT_EDIT_URL_PATH = "account/edit";
    public static String PRODUCT_NAME_1 = FrameworkConfig.getInstance().getString("product.name.1");
    public static String PRODUCT_NAME_2 = FrameworkConfig.getInstance().getString("product.name.2");
    public static String PRODUCT_NAME_3 = FrameworkConfig.getInstance().getString("product.name.3");
    public static String LASTNAME = FrameworkConfig.getInstance().getString("checkout.last_name");
    public static String FIRSTNAME = FrameworkConfig.getInstance().getString("checkout.first_name");
    public static String FULL_NAMES = "Jon Doe";
    public static String TELEPHONE = "0740500000000";
    public static String FAX = "0740500000001";
    public static String LOGIN_NAME = "webdriverio2";
    public static String PASSWORD = "webdriverio2";
    public static String INCORRECT_LOGIN_PASSWORD_PROVIDED = "Epic sadface: Username and password do not match any user in this service";
    public static int explicitWait = 15;
    public static int EXPLICIT_WAIT = 10;
    public static int SHORT_IMPLICIT_WAIT_TIME = 5;
    public static int THREE = 3;
    public static int TWO = 2;
    public static int ONE = 1;
    public static int FIVE = 5;
    public static int SIX = 6;
    public static int IMPLICIT_TIMEOUT = 10;
    public static int IMPLICIT_WAIT_TIMEOUT = 1;
    public static int DEFAULT_EXPLICIT_TIMEOUT = 15;
    public static int EXPLICIT_TIMEOUT = 30;
    public static int POLLING_TIMEOUT = 250;
    public static int PAGE_LOAD_TIMEOUT = 120;
    public static int DEFAULT_IMPLICIT_TIMEOUT = 7;
    public static int implicitWait = 10;
    static String THANKS_FOR_SHOPPING_WITH_US = "Thank you for shopping with us!";
    private static final GlobalVarsHelper ourInstance = new GlobalVarsHelper();
    private String URL = FrameworkConfig.getInstance().getString("url");
    private String userUrl;

    private GlobalVarsHelper() {
    }

    public static GlobalVarsHelper getInstance() {
        return ourInstance;
    }

    public static String getConfigPropertiesDirectory() {
        return CONFIG_PROPERTIES_DIRECTORY;
    }

    public static String getDataProviderJsonpath() {
        return DATA_PROVIDER_JSONPATH;
    }

    public static String getUrlHomepage() {
        return URL_HOMEPAGE;
    }

    public static String getLoginPageUrl() {
        return BASE_URL;
    }

    public static String getRegisterJsonPath() {
        return REGISTER_JSON_PATH;
    }

    public static String getContinueButton() {
        return CONTINUE_BUTTON;
    }

    public static String getAccountLogoutHeader() {
        return ACCOUNT_LOGOUT_HEADER;
    }

    public static String getAccountEditUrlPath() {
        return ACCOUNT_EDIT_URL_PATH;
    }

    public static String getProductName1() {
        return PRODUCT_NAME_1;
    }

    public static String getProductName2() {
        return PRODUCT_NAME_2;
    }

    public static String getProductName3() {
        return PRODUCT_NAME_3;
    }

    public static String getLOGINNAME() {
        return LOGIN_NAME;
    }

    public static String getIncorrectLoginPasswordProvided() {
        return INCORRECT_LOGIN_PASSWORD_PROVIDED;
    }

    public static int getShortImplicitWaitTime() {
        return SHORT_IMPLICIT_WAIT_TIME;
    }

    public static int getImplicitTimeout() {
        return IMPLICIT_TIMEOUT;
    }

    public static int getImplicitWaitTimeout() {
        return IMPLICIT_WAIT_TIMEOUT;
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

    public static int getDefaultImplicitTimeout() {
        return DEFAULT_IMPLICIT_TIMEOUT;
    }

    public static String getThanksForShoppingWithUs() {
        return THANKS_FOR_SHOPPING_WITH_US;
    }

    public static String getEmailAddress() {
        return EMAIL;
    }

    public static String getBrowser() {
        return BROWSER;
    }

    public static String get_standard_user() {
        return FrameworkConfig.getInstance().getString("standard_user");
    }

    public static String get_locked_out_user() {
        return FrameworkConfig.getInstance().getString("locked_out_user");
    }

    public static String get_problem_user() {
        return FrameworkConfig.getInstance().getString("problem_user");
    }

    public static String get_performance_glitch_user() {
        return FrameworkConfig.getInstance().getString("performance_glitch_user");
    }

    public static String get_error_user() {
        return FrameworkConfig.getInstance().getString("error_user");
    }

    public static String get_visual_user() {
        return FrameworkConfig.getInstance().getString("visual_user");
    }

    public static String getPasswordForAllUsers() {
        return FrameworkConfig.getInstance().getString("secret_sauce");
    }



    public String getURL() {
        return URL;

    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUserURL() {
        return userUrl;

    }

    public void setUserURL(String URL) {
        this.userUrl = URL;

    }

    public void setResponseHeaderAuthorisationCode(String authorization) {
        System.setProperty("http.headers.Authorization", authorization);
    }

    public String getCustomersUrl() {
        return FrameworkConfig.getInstance().getString("url");
    }


}
