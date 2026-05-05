package com.saucedemo.helperUtilities.pageLoad;

import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class CheckPageIsLoaded {
    private static final Logger log = LogManager.getLogger(CheckPageIsLoaded.class);
    private static WebDriver driver = WebDrv.getInstance().getWebDriver();

    public static void setDriver(WebDriver driver) {
        CheckPageIsLoaded.driver = driver;

    }

    public static void checkPageIsFullyLoaded() throws Exception {
        log.info("Checking if page is ready. Loop count of five");
        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
            assert driver != null;
            String readyState = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
            log.debug("Document ready state: " + readyState);
            return Objects.equals(readyState, "complete");
        };
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.PAGE_LOAD_TIME));
        try {
            wait.until(pageLoadCondition);
            log.info("Page is ready.");
        } catch (Exception e) {
            log.error("Page load check failed. Error: " + e.getMessage());
            throw e;
        }
    }

    public static void checkPageIsReady() throws Exception {
        log.info("Checking if page is ready. Loop count of five");
        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
            assert driver != null;
            String readyState = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
            log.debug("Document ready state: " + readyState);
            return Objects.equals(readyState, "complete");
        };
        WebDriverWait wait = new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.PAGE_LOAD_TIME));
        try {
            wait.until(pageLoadCondition);
            log.info("Page is ready.");
        } catch (Exception e) {
            log.error("Page load check failed. Error: " + e.getMessage());
            throw e;
        }
    }

    public static void waitForUrlToChange(String currentUrl) {
        new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.PAGE_LOAD_TIME)).until(webDriver ->
                !Objects.equals(webDriver.getCurrentUrl(), currentUrl)
        );
    }

    public static void waitForDocumentReadyState() {
        new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.PAGE_LOAD_TIME)).until(webDriver ->
                "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"))
        );
    }

    public static class PageLoadHelper {

        public static void waitForPageToLoad() {
            new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.PAGE_LOAD_TIME)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    JavascriptExecutor js = (JavascriptExecutor) d;
                    return Objects.equals(js.executeScript("return document.readyState"), "complete");
                }
            });
        }
    }


}
