package com.saucedemo.helperutilities.elements;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class ElementHelper {
    private static final Logger log = LogManager.getLogger(ElementHelper.class);
    WebDriver driver;


    public ElementHelper(WebDriver driver, Duration timeout) {
        this.driver = driver;
        ExplicitWaitFactory.setDriver(WebDrv.getInstance().getWebDriver());
    }


    public boolean isElementVisibleEnabledAndPresent(WebElement element) {
        try {
            WebElement visible = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            if (visible == null) return false;
            boolean isDisplayed = visible.isDisplayed();
            boolean isEnabled = visible.isEnabled();
            return isDisplayed && isEnabled;
        } catch (Exception e) {
            log.error("Element is not present or not interactable: " + e.getMessage());
        }
        return false;
    }


    public String isVisibleEnabledPresentAndGetText(WebElement element) {
        try {
            WebElement visible = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            if (visible != null && visible.isDisplayed() && visible.isEnabled()) {
                log.info("Element is displayed and enabled. Retrieving text.");
                return visible.getText();
            } else {
                return "Element is either not displayed or not enabled.";
            }
        } catch (Exception e) {
            log.error("Error checking element visibility, enablement, or presence: " + e.getMessage());
            return "Error retrieving text.";
        }
    }


    public String isVisibleEnabledPresentAndGetTextClick(WebElement element) {
        try {
            WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickable != null && clickable.isDisplayed() && clickable.isEnabled()) {
                String text = clickable.getText();
                clickable.click();
                log.info("Element clicked: " + text);
                return text;
            } else {
                log.warn("Element is either not displayed or not enabled.");
                return "Element is not displayed or enabled.";
            }
        } catch (Exception e) {
            log.error("Error checking element visibility, enablement, or presence: " + e.getMessage());
            return "Error retrieving text.";
        }
    }


    public void waitForDuration(long seconds) {
        log.info("Waiting for " + seconds + " seconds");
        try {
            Thread.sleep(java.time.Duration.ofSeconds(seconds).toMillis());
            log.info("Wait completed for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Wait interrupted after request for " + seconds + " seconds");
        }
    }
}
