package com.saucedemo.webdriverutilities;

import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;

public class WebDrv {
    private static final Logger log = LogManager.getLogger(WebDrv.class);
    private static final String DEFAULT_BROWSER = "chrome";
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();

    private static WebDrv instance;
    private final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private String browserType = DEFAULT_BROWSER;

    private WebDrv() {
        loadConfig();
    }

    public static WebDrv getInstance() {
        if (instance == null) {
            synchronized (WebDrv.class) {
                if (instance == null) {
                    instance = new WebDrv();
                }
            }
        }
        return instance;
    }

    public static void setInstance() {
        synchronized (WebDrv.class) {
            if (instance != null) {
                instance.closeCurrentDriver();
            }
            instance = null;
        }
    }

    private void loadConfig() {
        String configuredBrowser = CONFIG.getString("browserType", DEFAULT_BROWSER);
        if (configuredBrowser == null || configuredBrowser.trim().isEmpty()) {
            this.browserType = DEFAULT_BROWSER;
            log.warn("browserType is missing in config. Falling back to default: " + DEFAULT_BROWSER);
        } else {
            this.browserType = configuredBrowser.trim();
        }
    }

    public WebDriver openBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or blank");
        }

        WebDriver existingDriver = driverHolder.get();
        if (existingDriver != null) {
            log.info("Reusing existing driver session - clearing cookies and navigating to: " + url);
            existingDriver.manage().deleteAllCookies();
            navigateWithPostLoadSetup(existingDriver, url);
            return existingDriver;
        }

        String normalizedBrowser = browserType == null || browserType.trim().isEmpty()
                ? DEFAULT_BROWSER
                : browserType.trim().toLowerCase();

        log.info("Opening browser '" + normalizedBrowser + "' for URL: " + url);
        WebDriver webDriver;
        if ("chrome".equals(normalizedBrowser)) {
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_settings.popups", false);
            chromePrefs.put("download.prompt_for_download", false);
            chromePrefs.put("safebrowsing.enabled", false);
            // --- Password manager related preferences ---
            // Disables the "Save password" prompt
            chromePrefs.put("credentials_enable_service", false);
            // Disables the password manager features more broadly
            chromePrefs.put("profile.password_manager_enabled", false);
            // *** This is the key preference to stop the "Change your password" alert for breached passwords ***
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
            chromeOptions.addArguments("disable-infobars"); // Deprecated but sometimes still works
            chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            webDriver = new ChromeDriver(chromeOptions);
        } else if ("firefox".equals(normalizedBrowser)) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            webDriver = new FirefoxDriver(firefoxOptions);
        } else if ("edge".equals(normalizedBrowser)) {
            EdgeOptions edgeOptions = new EdgeOptions();
            // You can set Edge preferences here if needed
            webDriver = new EdgeDriver(edgeOptions);
        } else if ("internet explorer".equals(normalizedBrowser)) {
            InternetExplorerOptions ieOptions = new InternetExplorerOptions();
            ieOptions.setCapability("ignoreProtectedModeSettings", true);
            ieOptions.setCapability("requireWindowFocus", true);
            webDriver = new InternetExplorerDriver(ieOptions);
        } else if ("safari".equals(normalizedBrowser)) {
            webDriver = new SafariDriver();
        } else {
            throw new IllegalArgumentException("Browser type not supported: " + normalizedBrowser);
        }

        driverHolder.set(webDriver);
        ExplicitWaitFactory.setDriver(webDriver);
        navigateWithPostLoadSetup(webDriver, url);
        return webDriver;
    }

    private void navigateWithPostLoadSetup(WebDriver driver, String url) {
        driver.get(url);
        driver.manage().window().maximize();
        waitForPageToBeReady(driver);
    }

    private void waitForPageToBeReady(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));
            wait.until(webDriver -> "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
        } catch (Exception e) {
            log.warn("Explicit wait for page readiness timed out or failed.", e);
        }
    }

    public WebDriver getWebDriver() {
        return driverHolder.get();
    }

    public void closeCurrentDriver() {
        WebDriver currentDriver = driverHolder.get();
        if (currentDriver == null) {
            return;
        }

        try {
            currentDriver.quit();
            log.info("Browser session closed successfully");
        } catch (Exception e) {
            log.warn("Error while closing browser session", e);
        } finally {
            ExplicitWaitFactory.setDriver(null);
            driverHolder.remove();
        }
    }
}