package com.saucedemo.helperutilities.pageload;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckPageIsLoaded {

    private static final Logger log = LogManager.getLogger(CheckPageIsLoaded.class);

    private static WebDriver driver() {
        return WebDrv.getInstance().getWebDriver();
    }

    public static void checkPageIsFullyLoaded() {
        log.info("Waiting for document.readyState = complete");
        waitForDocumentReadyState();
        log.info("Page is ready");
    }

    public static void waitForDocumentReadyState() {
        new WebDriverWait(driver(), Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    public static void waitForUrlToChange(String currentUrl) {
        new WebDriverWait(driver(), Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(d -> !currentUrl.equals(d.getCurrentUrl()));
    }

    public static class PageLoadHelper {
        public static void waitForPageToLoad() {
            waitForDocumentReadyState();
        }
    }
}
