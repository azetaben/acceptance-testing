package com.saucedemo.helperutilities.wait;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.saucedemo.enums.WaitStrategy;

import java.time.Duration;

public class WaitHelper {

    private final WebDriver driver;
    private final Logger log = LoggerHelper.getLogger(WaitHelper.class);

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        ExplicitWaitFactory.setDriver(driver);
    }

    public void setImplicitWait(long timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
        log.info("Implicit wait set to: " + timeoutSeconds + "s");
    }

    public void waitForElementVisible(WebElement element, int timeoutSeconds, int pollIntervalMs) {
        log.info("Waiting for element to be visible (timeout=" + timeoutSeconds + "s, poll=" + pollIntervalMs + "ms)");
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollIntervalMs))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementClickable(WebElement element, int timeoutSeconds) {
        log.info("Waiting for element to be clickable (timeout=" + timeoutSeconds + "s)");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForElementNotPresent(WebElement element, long timeoutSeconds) {
        log.info("Waiting for element to be invisible (timeout=" + timeoutSeconds + "s)");
        boolean invisible = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOf(element));
        log.info("Element invisible: " + invisible);
        return invisible;
    }

    public void waitForFrameAndSwitchToIt(WebElement frameElement, long timeoutSeconds) {
        log.info("Waiting for frame to be available (timeout=" + timeoutSeconds + "s)");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
        log.info("Switched to frame");
    }

    public WebElement waitForElement(WebElement element, int timeoutSeconds, int pollIntervalMs) {
        log.info("Waiting for element (timeout=" + timeoutSeconds + "s, poll=" + pollIntervalMs + "ms)");
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollIntervalMs))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void pageLoadTime(long timeoutSeconds) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutSeconds));
        log.info("Page load timeout set to: " + timeoutSeconds + "s");
    }

    public void waitForElement(WebElement element) {
        log.info("Waiting for element to be visible");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Element is visible");
    }
}
