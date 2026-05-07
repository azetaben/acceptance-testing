package com.saucedemo.tests.selenium4features;

import com.saucedemo.constants.SauceDemoConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class WindowSwitchingDemoTest extends Selenium4FeatureBaseTest {

    @BeforeMethod
    public void setUp() {
        driver = createChromeDriver();
    }

    @Test(description = "Selenium 4 newWindow(): switch between tab/window handles")
    public void shouldCreateAndSwitchAcrossWindowsAndTabs() {
        driver.get(baseUrl);
        String originalHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        String tabHandle = driver.getWindowHandle();
        driver.get("https://example.com");

        driver.switchTo().newWindow(WindowType.WINDOW);
        String windowHandle = driver.getWindowHandle();
        driver.get("https://www.selenium.dev/");

        Set<String> handles = driver.getWindowHandles();
        Assert.assertTrue(handles.size() >= 3, "Expected at least three handles after opening one tab and one window.");

        driver.switchTo().window(originalHandle);
        List<WebElement> loginButtons = driver.findElements(By.id(SauceDemoConstants.LOGIN_BUTTON_ID));
        if (!loginButtons.isEmpty()) {
            Assert.assertTrue(loginButtons.get(0).isDisplayed(), "Expected login button in original Saucedemo window.");
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo"), "Expected to return to original Saucedemo window.");
        }

        if (driver.getWindowHandles().contains(tabHandle)) {
            driver.switchTo().window(tabHandle).close();
        }
        if (driver.getWindowHandles().contains(windowHandle)) {
            driver.switchTo().window(windowHandle).close();
        }

        driver.switchTo().window(originalHandle);
        Assert.assertTrue(driver.getWindowHandle().equals(originalHandle), "Expected original window handle to remain active.");
    }
}
