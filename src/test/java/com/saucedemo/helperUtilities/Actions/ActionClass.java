package com.saucedemo.helperUtilities.Actions;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ActionClass {
    private static final Logger log = LoggerHelper.getLogger(ActionClass.class);

    private static WebDriver getDriver() {
        return WebDrv.getInstance().getWebDriver();
    }

    public static void selectFirstWord(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, 0, 0).click().doubleClick().build().perform(); // selects first word
    }

    public static void selectLastWord(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, elementName.getText().length(), 0).click().doubleClick().build().perform(); // selects last word
    }

    public static void selectWordAtStartOfLine(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, 0, 0).click().doubleClick().build().perform(); // selects word at start of line
    }

    public static void selectWordAtEndOfLine(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, elementName.getText().length(), 0).click().doubleClick().build().perform(); // selects word at end of line
    }

    public static void selectWordAtPosition(WebElement elementName, int x, int y) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, x, y).click().doubleClick().build().perform(); // selects word at given position
    }

    public static void selectAllWords(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, 0, 0).click().keyDown(Keys.SHIFT).sendKeys(Keys.END).keyUp(Keys.SHIFT).build().perform(); // selects all words
    }

    public static void selectAllText(WebElement elementName) {
        Actions action = new Actions(getDriver());
        action.moveToElement(elementName, 0, 0).click().keyDown(Keys.CONTROL).sendKeys(Keys.END).keyUp(Keys.CONTROL).build().perform(); // selects all text
    }

    public static void hoverAndClickChildByText(WebElement parentElement, List<WebElement> childElements, String targetText) {
        // Validate input parameters
        if (parentElement == null) {
            throw new IllegalArgumentException("Parent element cannot be null.");
        }
        if (childElements == null || childElements.isEmpty()) {
            throw new IllegalArgumentException("List of child elements cannot be null or empty.");
        }
        if (targetText == null || targetText.trim().isEmpty()) {
            throw new IllegalArgumentException("Target text cannot be null or empty.");
        }

        Actions actions = new Actions(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(FrameworkConstants.getExplicitWait()));

        try {
            // Hover over the parent element
            wait.until(ExpectedConditions.visibilityOf(parentElement));
            actions.moveToElement(parentElement).perform();
            log.info("Hovered over element: " + parentElement);

            // Find the child element with the matching text
            boolean found = false;
            for (WebElement childElement : childElements) {
                wait.until(ExpectedConditions.visibilityOf(childElement));
                String childText = childElement.getText().trim();
                if (childText.equals(targetText)) {
                    // Move to the child element
                    actions.moveToElement(childElement).perform();
                    log.info("Moved to child element with text: " + targetText);
                    wait.until(ExpectedConditions.elementToBeClickable(childElement));
                    // Click the child element
                    childElement.click();
                    log.info("Clicked on child element with text: " + targetText);
                    found = true;
                    break; // Exit the loop after clicking
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No child element found with the text: '" + targetText + "'");
            }
        } catch (NoSuchElementException e) {
            log.error("Element not found during hover or click: " + e.getMessage(), e);
            throw e;
        } catch (StaleElementReferenceException e) {
            log.error("Element got stale while trying to interact with it" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("An error occurred during hover and click: " + e.getMessage(), e);
            throw e;
        }
    }
}
