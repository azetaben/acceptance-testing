package com.saucedemo.helperUtilities.wait;


import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WaitHelper {

    private final WebDriver driver;
    private final Logger log = LoggerHelper.getLogger(WaitHelper.class);

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        ExplicitWaitFactory.setDriver(driver);
    }

    public void setImplicitWait(long timeout, TimeUnit unit) {
        log.info("Implicit Wait has been set to: " + timeout);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
    }

    public void WaitForElementVisibleWithPollingTime(WebElement element, int timeOutInSeconds, int pollingEveryInMiliSec) {
        log.info("waiting for :" + element.toString() + " for :" + timeOutInSeconds + " seconds");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("element is visible now");
    }

    public void WaitForElementClickable(WebElement element, int timeOutInSeconds) {
        log.info("waiting for :" + element.toString() + " for :" + timeOutInSeconds + " seconds");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("element is clickable now");
    }

    public boolean waitForElementNotPresent(WebElement element, long timeOutInSeconds) {
        log.info("waiting for :" + element.toString() + " for :" + timeOutInSeconds + " seconds");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_INVISIBLE, element);
        log.info("element is invisible now");
        return true;
    }

    public void waitForframeToBeAvailableAndSwitchToIt(WebElement element, long timeOutInSeconds) {
        log.info("waiting for :" + element.toString() + " for :" + timeOutInSeconds + " seconds");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
        log.info("frame is available and switched");
    }

    public WebElement waitForElement(WebElement element, int timeOutInSeconds, int pollingEveryInMiliSec) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        return element;
    }

    public void pageLoadTime(long timeout) {
        log.info("page load time has been set to: " + timeout);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeout));
        log.info("page is loaded");
    }

    public void waitForElement(WebElement element) {
        log.info("waiting for :" + element.toString() + " for visibility");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("element is visible now");
    }
}
