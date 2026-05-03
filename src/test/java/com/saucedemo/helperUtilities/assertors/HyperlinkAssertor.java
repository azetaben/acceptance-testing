package com.saucedemo.helperUtilities.assertors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class HyperlinkAssertor {
    private static WebDriver driver;

    public static void assertNewTabIsOpenedWithExpectedPage(String expectedPageName) {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        // switch to new tab
        driver.switchTo().window(browserTabs.get(1));
        // check is it correct page opened or not (e.g. check page's title)
        Assert.assertEquals(expectedPageName, "The opened page is not what was expected", driver.findElement(By.id("heading")).getText());
        // then close tab and get back
        driver.close();
        driver.switchTo().window(browserTabs.get(0));
    }

    public static void assertNewTabOpenedWithExpectedTitle(String pageTitle) {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        // switch to new tab
        driver.switchTo().window(browserTabs.get(1));
        // check is it correct page opened or not (check page's title)
        Assert.assertEquals(pageTitle, "The opened page title did not match", driver.getTitle());
        // then close tab and get back
        driver.close();
        driver.switchTo().window(browserTabs.get(0));
    }
}
