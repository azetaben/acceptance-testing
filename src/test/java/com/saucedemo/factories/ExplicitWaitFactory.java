package com.saucedemo.factories;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class ExplicitWaitFactory {
    private static final Logger log = LogManager.getLogger(ExplicitWaitFactory.class);
    private static WebDriver driver;

    private ExplicitWaitFactory() {
    }

    public static void setDriver(WebDriver driver) {
        ExplicitWaitFactory.driver = driver;
    }

    private static WebDriver resolveDriver() {
        if (driver != null) {
            return driver;
        }
        return WebDrv.getInstance().getWebDriver();
    }

    public static WebElement performExplicitWait(WaitStrategy waitstrategy, By by) {
        log.info("Performing explicit wait with strategy: " + waitstrategy + " for By: " + by);
        WebDriver activeDriver = resolveDriver();
        if (activeDriver == null) {
            throw new RuntimeException("Explicit wait failed: WebDriver is not initialized. Call openBrowser() before interacting with elements.");
        }
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(activeDriver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        try {
            if (waitstrategy == WaitStrategy.CLICKABLE) {
                element = wait.until(ExpectedConditions.elementToBeClickable(by));
            } else if (waitstrategy == WaitStrategy.PRESENCE) {
                element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            } else if (waitstrategy == WaitStrategy.HANDLE_STALE_ELEMENT) {
                element = wait.until(d -> {
                    log.info("Retrying stale element lookup without page refresh...");
                    return d.findElement(by);
                });
            } else if (waitstrategy == WaitStrategy.NONE) {
                element = activeDriver.findElement(by);
            } else if (waitstrategy == WaitStrategy.VISIBLE) {
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else if (waitstrategy == WaitStrategy.URL_CONTAINS) {
                wait.until(d -> d.getCurrentUrl() != null && !d.getCurrentUrl().isBlank());
                log.info("URL_CONTAINS wait satisfied; no element returned.");
            } else if (waitstrategy == WaitStrategy.TITLE_CONTAINS) {
                wait.until(d -> d.getTitle() != null && !d.getTitle().isBlank());
                log.info("TITLE_CONTAINS wait satisfied; no element returned.");
            } else if (waitstrategy == WaitStrategy.INVISIBILITY) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            } else if (waitstrategy == WaitStrategy.ELEMENT_TO_BE_ENABLED) {
                element = wait.until(ExpectedConditions.elementToBeClickable(by));
            } else {
                log.warn("Unsupported wait strategy for By locator: " + waitstrategy);
            }
            log.info("Explicit wait completed successfully. Element: " + element);
        } catch (Exception e) {
            log.error("Explicit wait failed for By: " + by + ". Strategy: " + waitstrategy + ". Error: " + e.getMessage());
            throw new RuntimeException("Explicit wait failed for By: " + by + " [strategy=" + waitstrategy + "]", e);
        }
        return element;
    }

    public static WebElement performExplicitWait(WaitStrategy waitstrategy, WebElement element) {
        log.info("Performing explicit wait with strategy: " + waitstrategy + " for WebElement: " + element);
        WebDriver activeDriver = resolveDriver();
        if (activeDriver == null) {
            throw new RuntimeException("Explicit wait failed: WebDriver is not initialized. Call openBrowser() before interacting with elements.");
        }

        WebElement webElement = null;
        WebDriverWait wait = new WebDriverWait(activeDriver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        try {
            if (waitstrategy == WaitStrategy.CLICKABLE) {
                webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            } else if (waitstrategy == WaitStrategy.PRESENCE) {
                wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
                webElement = element;
            } else if (waitstrategy == WaitStrategy.VISIBLE) {
                webElement = wait.until(ExpectedConditions.visibilityOf(element));
            } else if (waitstrategy == WaitStrategy.NONE) {
                webElement = element;
            } else if (waitstrategy == WaitStrategy.ELEMENT_TO_BE_ENABLED) {
                webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            } else if (waitstrategy == WaitStrategy.ELEMENT_TO_BE_INVISIBLE) {
                wait.until(ExpectedConditions.invisibilityOf(element));
            } else {
                log.warn("Unsupported wait strategy for WebElement: " + waitstrategy);
            }
            log.info("Explicit wait completed successfully. WebElement: " + webElement);
        } catch (Exception e) {
            log.error("Explicit wait failed for WebElement. Strategy: " + waitstrategy + ". Error: " + e.getMessage());
            throw new RuntimeException("Explicit wait failed for WebElement [strategy=" + waitstrategy + "]", e);
        }
        return webElement;
    }

    public static List<WebElement> performExplicitWaitForList(WaitStrategy waitstrategy, By by) {
        log.info("Performing explicit wait (list) with strategy: " + waitstrategy + " for By: " + by);
        WebDriver activeDriver = resolveDriver();
        if (activeDriver == null) {
            throw new RuntimeException("Explicit wait failed: WebDriver is not initialized. Call openBrowser() before interacting with elements.");
        }
        List<WebElement> webElements = null;
        WebDriverWait wait = new WebDriverWait(activeDriver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        try {
            if (waitstrategy == WaitStrategy.PRESENCE_OF_ALL_ELEMENTS_LOCATED) {
                webElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
            } else if (waitstrategy == WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS) {
                webElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
            } else {
                log.warn("Unsupported wait strategy for By list: " + waitstrategy);
                webElements = activeDriver.findElements(by);
            }
            log.info("Explicit wait (list) completed. Size: " + (webElements != null ? webElements.size() : "null"));
        } catch (Exception e) {
            log.error("Explicit wait (list) failed for By: " + by + ". Strategy: " + waitstrategy + ". Error: " + e.getMessage());
            throw new RuntimeException("Explicit wait (list) failed for By: " + by + " [strategy=" + waitstrategy + "]", e);
        }
        return webElements;
    }

    public static List<WebElement> performExplicitWait(WaitStrategy waitstrategy, List<WebElement> elements) {
        log.info("Performing explicit wait with strategy: " + waitstrategy + " for List<WebElement>.");
        WebDriver activeDriver = resolveDriver();
        if (activeDriver == null) {
            throw new RuntimeException("Explicit wait failed: WebDriver is not initialized. Call openBrowser() before interacting with elements.");
        }
        List<WebElement> webElements = null;
        WebDriverWait wait = new WebDriverWait(activeDriver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        try {
            if (waitstrategy == WaitStrategy.PRESENCE) {
                webElements = wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            } else if (waitstrategy == WaitStrategy.VISIBLE) {
                webElements = wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            } else if (waitstrategy == WaitStrategy.NONE) {
                webElements = elements;
            } else if (waitstrategy == WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS) {
                webElements = wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            } else {
                log.warn("Unsupported wait strategy for List<WebElement>: " + waitstrategy);
            }
            log.info("Explicit wait completed successfully for List<WebElement>. Size: " + (webElements != null ? webElements.size() : "null"));
        } catch (Exception e) {
            log.error("Explicit wait failed for List<WebElement>. Strategy: " + waitstrategy + ". Error: " + e.getMessage());
            throw new RuntimeException("Explicit wait failed for List<WebElement> [strategy=" + waitstrategy + "]", e);
        }
        return webElements;
    }
}
