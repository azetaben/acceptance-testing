package com.saucedemo.webdriverutilities;

import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.utils.PathUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebDriverConfig {
    private static final Logger log = LogManager.getLogger(WebDriverConfig.class);

    private final WebDriver webDriver;

    public WebDriverConfig(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Deprecated
    public static WebDriver configureAndOpenBrowser(String url, DesiredCapabilities caps, WebDriver webDriver) {
        if (caps != null) {
            log.debug("DesiredCapabilities argument is currently not used by ChromeDriver initialization");
        }
        return configureAndOpenBrowser(url);
    }

    public static WebDriver configureAndOpenBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or blank");
        }

        WebDriver webDriver = new ChromeDriver(createChromeOptions());
        ExplicitWaitFactory.setDriver(webDriver);
        webDriver.get(url.trim());
        webDriver.manage().window().maximize();
        waitForPageToBeReady(webDriver);
        log.info("Chrome browser opened and navigated to: " + url);
        return webDriver;
    }

    private static void waitForPageToBeReady(WebDriver webDriver) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));
            wait.until(driver -> "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState")));
        } catch (Exception e) {
            log.warn("Explicit wait for page readiness timed out or failed.", e);
        }
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();

        // Apply preferences directly to ChromeOptions
        chromeOptions.setExperimentalOption("prefs", createChromePreferences());

        // Existing arguments
        chromeOptions.addArguments("--disable-dev-shm-usage");
        // chromeOptions.addArguments("--auto-open-devtools-for-tabs"); // Often for debugging, can be removed for regular runs
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-gpu"); // Good for headless, sometimes useful for headed too
        chromeOptions.addArguments("--whitelisted-ips"); // Be cautious with this, ensure it's necessary
        chromeOptions.addArguments("--ignore-certificate-errors"); // Use with caution, security risk

        // Optional: To prevent "Chrome is being controlled by automated test software" infobar
        chromeOptions.addArguments("disable-infobars"); // Deprecated but sometimes still works
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);


        //chromeOptions.addArguments("--headless");
        return chromeOptions;
    }

    private static Map<String, Object> createChromePreferences() { // Changed return type to Map for better practice
        HashMap<String, Object> chromePrefs = new HashMap<>(); // Use diamond operator
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.default_directory", PathUtil.getDownloadsDir());
        // --- Password manager related preferences ---
        // Disables the "Save password" prompt
        chromePrefs.put("credentials_enable_service", false);
        // Disables the password manager features more broadly
        chromePrefs.put("profile.password_manager_enabled", false);
        // *** This is the key preference to stop the "Change your password" alert for breached passwords ***
        chromePrefs.put("profile.password_manager_leak_detection", false);
        chromePrefs.put("safebrowsing.enabled", false); // Disables Safe Browsing (use with caution, security risk)
        return chromePrefs;
    }

    public WebDriverConfig maximiseWindow() {
        webDriver.manage().window().maximize();
        return this;
    }

    public WebDriverConfig setPageLoadTimeout(Duration pageLoadTime) {
        webDriver.manage().timeouts().pageLoadTimeout(pageLoadTime);
        return this;
    }
}