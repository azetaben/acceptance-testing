package com.saucedemo.webdriverutilities;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.factories.ExplicitWaitFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class WebDrv {

    private static final Logger log = LogManager.getLogger(WebDrv.class);
    private static final String DEFAULT_BROWSER = "chrome";
    private static final int MAX_INIT_ATTEMPTS = 2;
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();

    private static volatile WebDrv instance;
    private final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    private WebDrv() {
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


    public static void resetInstance() {
        synchronized (WebDrv.class) {
            if (instance != null) {
                instance.closeCurrentDriver();
            }
            instance = null;
        }
    }


    public WebDriver openBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or blank");
        }

        WebDriver existing = driverHolder.get();
        if (existing != null) {
            log.info("Reusing driver on [" + Thread.currentThread().getName() + "] — navigating to: " + url);
            existing.manage().deleteAllCookies();
            navigate(existing, url);
            return existing;
        }

        String browser = resolveBrowserType();
        log.info("Starting '" + browser + "' driver on [" + Thread.currentThread().getName() + "]");

        WebDriver driver = createDriverWithRetry(browser);
        applyStartupSettings(driver);
        driverHolder.set(driver);
        ExplicitWaitFactory.setDriver(driver);
        navigate(driver, url);
        return driver;
    }


    public WebDriver getWebDriver() {
        return driverHolder.get();
    }


    public boolean isDriverActive() {
        WebDriver driver = driverHolder.get();
        if (driver == null) return false;
        try {
            driver.getTitle();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void closeCurrentDriver() {
        WebDriver driver = driverHolder.get();
        if (driver == null) return;
        try {
            driver.quit();
            log.info("Driver closed on [" + Thread.currentThread().getName() + "]");
        } catch (Exception e) {
            log.warn("Error closing driver: " + e.getMessage(), e);
        } finally {
            ExplicitWaitFactory.setDriver(null);
            driverHolder.remove();
        }
    }


    private String resolveBrowserType() {
        String sysProp = System.getProperty("browser");
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim().toLowerCase();
        }
        String configured = CONFIG.getString("browserType", DEFAULT_BROWSER);
        return (configured == null || configured.trim().isEmpty()) ? DEFAULT_BROWSER : configured.trim().toLowerCase();
    }

    private WebDriver createDriverWithRetry(String browser) {
        RuntimeException last = null;
        for (int attempt = 1; attempt <= MAX_INIT_ATTEMPTS; attempt++) {
            try {
                return createDriver(browser);
            } catch (RuntimeException e) {
                last = e;
                log.warn("Driver init attempt " + attempt + "/" + MAX_INIT_ATTEMPTS + " failed: " + e.getMessage());
                if (attempt < MAX_INIT_ATTEMPTS) {
                    try {
                        Thread.sleep(1000L * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        throw new IllegalStateException(
                "Could not create '" + browser + "' driver after " + MAX_INIT_ATTEMPTS + " attempts", last);
    }

    private WebDriver createDriver(String browser) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver(WebDriverConfig.buildChromeOptions());
            case "firefox" -> new FirefoxDriver(WebDriverConfig.buildFirefoxOptions());
            case "edge" -> new EdgeDriver(WebDriverConfig.buildEdgeOptions());
            case "safari" -> new SafariDriver();
            case "ie", "internet explorer" -> {
                InternetExplorerOptions ie = new InternetExplorerOptions();
                ie.setCapability("ignoreProtectedModeSettings", true);
                ie.setCapability("requireWindowFocus", true);
                yield new InternetExplorerDriver(ie);
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: '" + browser + "'. Valid values: chrome, firefox, edge, safari, ie.");
        };
    }

    private void applyStartupSettings(WebDriver driver) {
        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.getExplicitWait()));

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    private void navigate(WebDriver driver, String url) {
        driver.get(url);
        waitForPageReady(driver);
    }

    private void waitForPageReady(WebDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                    .until(d -> "complete".equals(
                            ((JavascriptExecutor) d).executeScript("return document.readyState")));
        } catch (Exception e) {
            log.warn("Page-ready wait timed out — continuing anyway", e);
        }
    }
}
