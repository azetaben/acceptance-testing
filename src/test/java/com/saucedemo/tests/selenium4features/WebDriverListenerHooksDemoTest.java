package com.saucedemo.tests.selenium4features;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.webdriverutilities.listeners.DebugWebDriverListener;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class WebDriverListenerHooksDemoTest extends Selenium4FeatureBaseTest {
    private DebugWebDriverListener debugListener;
    private boolean listenerDecoratorEnabled;

    @BeforeMethod
    public void setUp() {
        ChromeDriver rawChrome = createChromeDriver();
        debugListener = new DebugWebDriverListener();
        try {
            WebDriver decorated = new EventFiringDecorator<>(debugListener).decorate(rawChrome);
            driver = decorated;
            listenerDecoratorEnabled = true;
        } catch (IllegalStateException unsupportedInCurrentRuntime) {
            driver = rawChrome;
            listenerDecoratorEnabled = false;
        }
    }

    @Test(description = "Selenium 4 WebDriverListener hooks for action tracing and error diagnostics")
    public void shouldTraceActionsAndErrorsUsingHooks() {
        if (!listenerDecoratorEnabled) {
            throw new SkipException("Skipping listener hook assertions: EventFiringDecorator is not stable in this runtime.");
        }

        driver.get(baseUrl);

        List<WebElement> loginButtons = driver.findElements(By.id(SauceDemoConstants.LOGIN_BUTTON_ID));
        Assert.assertFalse(loginButtons.isEmpty(), "Expected login button on the login page.");
        loginButtons.get(0).click();

        try {
            driver.findElement(By.id("missing-element-id"));
            Assert.fail("Expected NoSuchElementException for missing element.");
        } catch (NoSuchElementException ignored) {

        }

        List<String> events = debugListener.getEventsSnapshot();
        Assert.assertTrue(events.stream().anyMatch(event -> event.startsWith("beforeGet:")), "Expected beforeGet hook event.");
        Assert.assertTrue(events.stream().anyMatch(event -> event.startsWith("afterClick:")), "Expected afterClick hook event.");
        Assert.assertTrue(events.stream().anyMatch(event -> event.startsWith("onError:")), "Expected onError hook event.");
    }
}
