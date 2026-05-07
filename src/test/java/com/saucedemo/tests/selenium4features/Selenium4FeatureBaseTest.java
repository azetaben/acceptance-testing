package com.saucedemo.tests.selenium4features;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.constants.SauceDemoConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;

import java.util.HashMap;

public abstract class Selenium4FeatureBaseTest {
    protected final FrameworkConfig config = FrameworkConfig.getInstance();
    protected final String baseUrl = config.getString("url", SauceDemoConstants.DEFAULT_BASE_URL);
    protected final String standardUser = config.getString("standard_user", SauceDemoConstants.USER_STANDARD);
    protected final String standardPassword = config.getString("password", SauceDemoConstants.USER_PASSWORD);

    protected WebDriver driver;

    protected ChromeDriver createChromeDriver() {
        try {
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("credentials_enable_service", false);

            chromePrefs.put("profile.password_manager_enabled", false);

            chromePrefs.put("profile.password_manager_leak_detection", false);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            chromeOptions.addArguments("--disable-infobars");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--whitelisted-ips");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--test-type");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--disable-default-apps");
            chromeOptions.addArguments("disable-infobars");
            chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            chromeOptions.setExperimentalOption("useAutomationExtension", false);

            chromeOptions.addArguments("--start-maximized");
            return new ChromeDriver(chromeOptions);
        } catch (WebDriverException exception) {
            throw new SkipException("Skipping: Chrome is unavailable in this environment.", exception);
        }
    }

    protected FirefoxDriver createFirefoxDriver() {
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            return new FirefoxDriver(options);
        } catch (WebDriverException exception) {
            throw new SkipException("Skipping: Firefox is unavailable in this environment.", exception);
        }
    }

    protected void loginToSauceDemo(WebDriver webDriver) {
        webDriver.get(baseUrl);
        webDriver.findElement(By.id(SauceDemoConstants.LOGIN_USERNAME_FIELD_ID)).sendKeys(standardUser);
        webDriver.findElement(By.id(SauceDemoConstants.LOGIN_PASSWORD_FIELD_ID)).sendKeys(standardPassword);
        webDriver.findElement(By.id(SauceDemoConstants.LOGIN_BUTTON_ID)).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
