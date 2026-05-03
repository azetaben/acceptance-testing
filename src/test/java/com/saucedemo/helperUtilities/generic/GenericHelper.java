package com.saucedemo.helperUtilities.generic;

import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.interfaces.IwebComponent;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Reporter;

import java.io.IOException;

public class GenericHelper implements IwebComponent {
    private static final Logger log = LoggerHelper.getLogger(GenericHelper.class);
    WebDriver driver;

    public static synchronized String getElementText(WebElement element) {
        if (null == element) {
            log.info("enablement is null");
            return null;
        }
        String elementText = null;
        try {
            elementText = element.getText();
        } catch (Exception ex) {
            log.info("Element not found " + ex);
            Reporter.log(ex.fillInStackTrace().toString());
        }
        return elementText;
    }

    public WebElement getElement(By locator) {
        log.info("Getting element with locator: " + locator);
        // test.log(Status.INFO, "Getting element with locator: " + locator);
        if (IsElementPresentQuick(locator)) {
            WebElement element = driver.findElement(locator);
            log.info("Element found: " + element);
            // test.log(Status.INFO, "Element found: " + element);
            return element;
        }

        try {
            throw new NoSuchElementException("Element Not Found : " + locator);
        } catch (RuntimeException re) {
            log.error("Error finding element: " + re.getMessage(), re);
            throw re;
        }
    }

    public WebElement getElementWithNull(By locator) {
        log.info("Getting element with null with locator: " + locator);
        // test.log(Status.INFO, "Getting element with null with locator: " + locator);
        try {
            WebElement element = driver.findElement(locator);
            log.info("Element found: " + element);
            // test.log(Status.INFO, "Element found: " + element);
            return element;
        } catch (NoSuchElementException e) {
            log.info("Element not found: " + locator);
            // test.log(Status.INFO, "Element not found: " + locator);
            // Ignore
        }
        return null;
    }

    public boolean IsElementPresentQuick(By locator) {
        boolean flag = !driver.findElements(locator).isEmpty();
        log.info("Is element present quickly? " + flag + " for locator: " + locator);
        // test.log(Status.INFO, "Is element present quickly? " + flag + " for locator: " + locator);
        return flag;
    }


    public String takeScreenShot() throws IOException {
        log.info("Taking screenshot as Base64");
        String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        log.info("Screenshot taken as Base64");
        return base64Screenshot;
    }

    public String readValueFromElement(WebElement element) {

        if (null == element) {
            log.info("WebElement is null");
            return null;
        }

        boolean displayed = false;
        try {
            displayed = isDisplayed(element);
        } catch (Exception e) {
            log.error("Error checking if element is displayed: " + e.getMessage(), e);
            // test.log(Status.FAIL, "Error checking if element is displayed: " + e.getMessage());
            Reporter.log(e.fillInStackTrace().toString());
            return null;
        }

        if (!displayed) {
            log.info("Element is not displayed");
            // test.log(Status.INFO, "Element is not displayed");
            return null;
        }
        String text = element.getText();
        log.info("Element value is: " + text);
        // test.log(Status.INFO, "Element value is: " + text);
        return text;
    }

    public String readValueFromInput(WebElement element) {
        if (null == element) {
            log.info("WebElement is null");
            // test.log(Status.INFO, "WebElement is null");
            return null;
        }
        if (!isDisplayed(element)) {
            log.info("Element is not displayed");
            // test.log(Status.INFO, "Element is not displayed");
            return null;
        }
        String value = element.getDomAttribute("value");
        log.info("Element value is: " + value);
        // test.log(Status.INFO, "Element value is: " + value);
        return value;
    }

    public boolean isDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            log.info("Element is displayed: " + isDisplayed + " for element: " + element);
            // test.log(Status.INFO, "Element is displayed: " + isDisplayed + " for element: " + element);
            return isDisplayed;
        } catch (Exception e) {
            log.info("Element is not displayed: " + e.getMessage());
            // test.log(Status.INFO, "Element is not displayed: " + e.getMessage());
            Reporter.log(e.fillInStackTrace().toString());
            return false;
        }
    }

    protected boolean isNotDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            log.info("Element is displayed: " + isDisplayed + " for element: " + element);
            // test.log(Status.INFO, "Element is displayed: " + isDisplayed + " for element: " + element);
            return !isDisplayed;
        } catch (Exception e) {
            log.error("Element is not displayed: " + e.getMessage());
            // test.log(Status.INFO, "Element is not displayed: " + e.getMessage());
            Reporter.log(e.fillInStackTrace().toString());
            return true;
        }
    }

    protected String getDisplayText(WebElement element) {
        if (null == element) {
            log.info("WebElement is null");
            // test.log(Status.INFO, "WebElement is null");
            return null;
        }
        if (!isDisplayed(element)) {
            log.info("Element is not displayed");
            // test.log(Status.INFO, "Element is not displayed");
            return null;
        }
        String text = element.getText();
        log.info("Element text is: " + text);
        // test.log(Status.INFO, "Element text is: " + text);
        return text;
    }
}
