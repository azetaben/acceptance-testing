package com.saucedemo.helperutilities.assertion;

import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class AssertionHelper {
    private static final Logger log = LogManager.getLogger(AssertionHelper.class);

    private static WebDriver driver() {
        return WebDrv.getInstance().getWebDriver();
    }

    public static void verifyText(String s1, String s2) {
        log.info("Verifying testScripts: " + s1 + " with " + s2);
        Assert.assertEquals(s1, s2);
    }

    public static void markPass() {
        log.info("Making script PASS..");
        pass();
    }

    public static void markPass(String message) {
        log.info("Making script PASS => " + message);
        Assert.assertTrue(true, message);
    }

    public static void markFail() {
        log.info("Making script FAIL..");
        fail();
    }

    public static void markFail(String message) {
        log.info("Making script FAIL => " + message);
        Assert.fail(message);
    }

    public static void verifyTrue(boolean status) {
        log.info("Verify object is True..");
        Assert.assertTrue(status);
    }

    public static void verifyFalse(boolean status) {
        log.info("Verify object is False..");
        Assert.assertFalse(status);
    }

    public static void verifyNull(Object obj) {
        log.info("Verify object is null..");
        Assert.assertNull(obj);
    }

    public static void verifyNotNull(Object obj) {
        log.info("Verify object is not null..");
        Assert.assertNotNull(obj);
    }

    public static void fail() {
        Assert.fail();
    }

    public static void pass() {
        Assert.assertTrue(true);
    }

    public static void updateTestStatus(boolean status) {
        if (status) {
            log.info("UpdateTestStatus => PASSED");
            pass();
        } else {
            log.info("UpdateTestStatus => FAILED");
            fail();
        }
    }

    public static void assertNewTabIsOpenedWithExpectedPage(String expectedPageName) {
        List<String> browserTabs = new ArrayList<>(driver().getWindowHandles());
        if (browserTabs.size() < 2) {
            throw new IllegalStateException("Expected at least 2 browser tabs but found: " + browserTabs.size());
        }
        driver().switchTo().window(browserTabs.get(1));
        Assert.assertEquals(driver().findElement(By.id("heading")).getText(), expectedPageName, "The opened page is not what was expected");
        driver().close();
        driver().switchTo().window(browserTabs.get(0));
    }

    public static void assertNewTabOpenedWithExpectedTitle(String pageTitle) {
        List<String> browserTabs = new ArrayList<>(driver().getWindowHandles());
        if (browserTabs.size() < 2) {
            throw new IllegalStateException("Expected at least 2 browser tabs but found: " + browserTabs.size());
        }
        driver().switchTo().window(browserTabs.get(1));
        Assert.assertEquals(driver().getTitle(), pageTitle, "The opened page title did not match");
        driver().close();
        driver().switchTo().window(browserTabs.get(0));
    }

    public static List<String> getModifiableIdListOfExpectedChildElements(List<String> elementIds) {
        return new ArrayList<>(elementIds);
    }

    public static boolean verifyElementPresent(WebElement element) {
        boolean isDisplayed = false;
        try {
            isDisplayed = element.isDisplayed();
            log.info(element.getText() + " is displayed");
        } catch (Exception ex) {
            log.error("Element not found " + ex);
        }
        return isDisplayed;
    }

    public static boolean verifyElementNotPresent(WebElement element) {
        boolean isNotPresent = false;
        try {
            element.isDisplayed();
            log.info(element.getText() + " is displayed");
        } catch (Exception ex) {
            log.error("Element not found " + ex);
            isNotPresent = true;
        }
        return isNotPresent;
    }

    public static boolean verifyTextEquals(WebElement element, String expectedText) {
        try {
            String actualText = element.getText();
            if (actualText.equals(expectedText)) {
                log.info("actualText is :" + actualText + " expected text is: " + expectedText);
                return true;
            } else {
                log.error("actualText is :" + actualText + " expected text is: " + expectedText);
                return false;
            }
        } catch (Exception ex) {
            log.error("Could not read element text, expected: " + expectedText + ". Error: " + ex.getMessage());
            return false;
        }
    }
}
