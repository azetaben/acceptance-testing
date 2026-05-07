package com.saucedemo.helperutilities.generic;

import com.saucedemo.helperutilities.logger.LoggerHelper;
import com.saucedemo.interfaces.IWebComponent;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Reporter;

import java.io.IOException;

public class GenericHelper implements IWebComponent {
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

        if (IsElementPresentQuick(locator)) {
            WebElement element = driver.findElement(locator);
            log.info("Element found: " + element);

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

        try {
            WebElement element = driver.findElement(locator);
            log.info("Element found: " + element);

            return element;
        } catch (NoSuchElementException e) {
            log.info("Element not found: " + locator);


        }
        return null;
    }

    public boolean IsElementPresentQuick(By locator) {
        boolean flag = !driver.findElements(locator).isEmpty();
        log.info("Is element present quickly? " + flag + " for locator: " + locator);

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

            Reporter.log(e.fillInStackTrace().toString());
            return null;
        }

        if (!displayed) {
            log.info("Element is not displayed");

            return null;
        }
        String text = element.getText();
        log.info("Element value is: " + text);

        return text;
    }

    public String readValueFromInput(WebElement element) {
        if (null == element) {
            log.info("WebElement is null");

            return null;
        }
        if (!isDisplayed(element)) {
            log.info("Element is not displayed");

            return null;
        }
        String value = element.getDomAttribute("value");
        log.info("Element value is: " + value);

        return value;
    }

    public boolean isDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            log.info("Element is displayed: " + isDisplayed + " for element: " + element);

            return isDisplayed;
        } catch (Exception e) {
            log.info("Element is not displayed: " + e.getMessage());

            Reporter.log(e.fillInStackTrace().toString());
            return false;
        }
    }

    protected boolean isNotDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            log.info("Element is displayed: " + isDisplayed + " for element: " + element);

            return !isDisplayed;
        } catch (Exception e) {
            log.error("Element is not displayed: " + e.getMessage());

            Reporter.log(e.fillInStackTrace().toString());
            return true;
        }
    }

    protected String getDisplayText(WebElement element) {
        if (null == element) {
            log.info("WebElement is null");

            return null;
        }
        if (!isDisplayed(element)) {
            log.info("Element is not displayed");

            return null;
        }
        String text = element.getText();
        log.info("Element text is: " + text);

        return text;
    }
}
