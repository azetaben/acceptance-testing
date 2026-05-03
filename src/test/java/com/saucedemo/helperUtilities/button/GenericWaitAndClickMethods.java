package com.saucedemo.helperUtilities.button;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.helperUtilities.logger.LoggerHelper;
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

    /**
     * Waits for a WebElement to be visible within the specified timeout.
     *
     * @param element          The WebElement to wait for.
     * @param timeoutInSeconds The maximum time to wait for the element to be visible, in seconds.
     */
    public static void waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Element is visible: " + element);
        // test.log(Status.INFO, "Element is visible: " + element);
    }

    /**
     * Waits for a WebElement to be visible within the default timeout. Default timeout will be taken
     * from GlobalVarsHelper class
     *
     * @param element The WebElement to wait for.
     */
    public static void waitForElementToBeVisible(WebElement element) {
        waitForElementToBeVisible(element, GlobalVarsHelper.getExplicitTimeout());
    }

    /**
     * Clicks an element from a list of WebElements based on its text content.
     *
     * @param elements The list of WebElements to search within.
     * @param text     The text content of the element to click.
     * @throws IllegalArgumentException If the list of elements is null or empty, if the text is null
     *                                  or empty, or if no element with the given text is found.
     */
    public static void clickElementByText(List<WebElement> elements, String text) {
        // Validate input parameters
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("List of WebElements cannot be null or empty.");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }

        boolean found = false;
        for (WebElement element : elements) {
            try {
                String elementText = element.getText().trim(); // Trim for whitespace robustness
                if (elementText.equals(text)) {
                    waitForElementToBeClickable(element);
                    element.click();
                    log.info("Clicked on element with text: '" + text + "'");
                    // test.log(Status.INFO, "Clicked on element with text: '" + text + "'");
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

    /**
     * Waits for an element to be clickable within the specified timeout.
     *
     * @param element The WebElement to wait for.
     */
    public static void waitForElementToBeClickable(WebElement element) {
        waitForElementToBeClickable(element, GlobalVarsHelper.getExplicitTimeout());
    }

    /**
     * Waits for an element to be clickable within the specified timeout.
     *
     * @param element          The WebElement to wait for.
     * @param timeoutInSeconds The maximum time to wait for the element to be clickable, in seconds.
     */
    public static void waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("Element is clickable: " + element);
        // test.log(Status.INFO, "Element is clickable: " + element);
    }

    /**
     * Waits for an element located by a By locator to be visible within the specified timeout.
     *
     * @param locator          The By locator for the element.
     * @param timeoutInSeconds The maximum time to wait for the element to be visible, in seconds.
     */
    public void waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        log.info("Element located by " + locator + " is visible.");
        // test.log(Status.INFO, "Element located by " + locator + " is visible.");
    }

    /**
     * Waits for an element located by a By locator to be visible within the default timeout. Default
     * timeout will be taken from GlobalVarsHelper class
     *
     * @param locator The By locator for the element.
     */
    public void waitForElementToBeVisible(By locator) {
        waitForElementToBeVisible(locator, GlobalVarsHelper.getExplicitTimeout());
    }

    /**
     * Clicks an element located by a By locator and identified by its text content.
     *
     * @param locator The By locator for the elements.
     * @param text    The text content of the element to click.
     * @throws IllegalArgumentException If the locator is null, if the text is null or empty, or if no
     *                                  element with the given text is found.
     */
    public void clickElementByText(By locator, String text) {
        // Validate input parameters
        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        List<WebElement> elements = driver.findElements(locator);
        clickElementByText(elements, text);
    }

    /**
     * This method is just a wrapper over the click method in the page class
     */
    public void click(By xpath, WaitStrategy clickable) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(clickable, xpath);
        if (element == null) {
            throw new IllegalStateException("Unable to click element using wait strategy " + clickable + ": " + xpath);
        }
        element.click();
        // test.log(Status.INFO, "Clicked on element: " + xpath);
    }

    public void click(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
        // test.log(Status.INFO, "Clicked on element: " + element);
    }
}
