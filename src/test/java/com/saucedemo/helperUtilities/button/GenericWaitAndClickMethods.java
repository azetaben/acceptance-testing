package com.saucedemo.helperutilities.button;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.helperutilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GenericWaitAndClickMethods {
    private static final Logger log = LoggerHelper.getLogger(GenericWaitAndClickMethods.class);
    private static WebDriver driver;

    public GenericWaitAndClickMethods(WebDriver driver) {
        GenericWaitAndClickMethods.driver = driver;
        ExplicitWaitFactory.setDriver(driver);
    }


    public static void waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Element is visible: " + element);

    }


    public static void waitForElementToBeVisible(WebElement element) {
        waitForElementToBeVisible(element, GlobalVarsHelper.getExplicitTimeout());
    }


    public static void clickElementByText(List<WebElement> elements, String text) {

        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("List of WebElements cannot be null or empty.");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }

        boolean found = false;
        for (WebElement element : elements) {
            try {
                String elementText = element.getText().trim();
                if (elementText.equals(text)) {
                    waitForElementToBeClickable(element);
                    element.click();
                    log.info("Clicked on element with text: '" + text + "'");

                    found = true;
                    break;
                }
            } catch (NoSuchElementException e) {
                log.warn("Element in list went stale while checking. Continuing to next element.", e);
            } catch (Exception e) {
                log.error("Error occurred while clicking element with text: '" + text + "'", e);
            }
        }

        if (!found) {
            throw new IllegalArgumentException("No element found with the text: '" + text + "'");
        }
    }


    public static void waitForElementToBeClickable(WebElement element) {
        waitForElementToBeClickable(element, GlobalVarsHelper.getExplicitTimeout());
    }


    public static void waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("Element is clickable: " + element);

    }


    public void waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        log.info("Element located by " + locator + " is visible.");

    }


    public void waitForElementToBeVisible(By locator) {
        waitForElementToBeVisible(locator, GlobalVarsHelper.getExplicitTimeout());
    }


    public void clickElementByText(By locator, String text) {

        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        List<WebElement> elements = driver.findElements(locator);
        clickElementByText(elements, text);
    }


    public void click(By xpath, WaitStrategy clickable) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(clickable, xpath);
        if (element == null) {
            throw new IllegalStateException("Unable to click element using wait strategy " + clickable + ": " + xpath);
        }
        element.click();

    }

    public void click(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();

    }
}
