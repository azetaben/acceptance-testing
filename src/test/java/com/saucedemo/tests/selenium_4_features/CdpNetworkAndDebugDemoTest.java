package com.saucedemo.tests.selenium_4_features;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CdpNetworkAndDebugDemoTest extends Selenium4FeatureBaseTest {
    private ChromeDriver chrome;

    @BeforeMethod
    public void setUp() {
        chrome = createChromeDriver();
        driver = chrome;
    }

    @Test(description = "Selenium 4 CDP: trace network + toggle offline/online")
    public void shouldUseCdpForNetworkAndOfflineOnlineSwitch() {
        chrome.get(baseUrl);
        Assert.assertTrue(chrome.getTitle().contains("Swag Labs"), "Expected login page title to load.");

        // Native CDP command support in Selenium 4 Chromium drivers.
        chrome.executeCdpCommand("Network.enable", Map.of());

        Map<String, Object> offline = new HashMap<>();
        offline.put("offline", true);
        offline.put("latency", 100);
        offline.put("downloadThroughput", 0);
        offline.put("uploadThroughput", 0);
        offline.put("connectionType", "none");
        chrome.executeCdpCommand("Network.emulateNetworkConditions", offline);

        boolean offlineBlockedNavigation = false;
        try {
            chrome.navigate().to("https://example.com");
        } catch (WebDriverException expected) {
            offlineBlockedNavigation = true;
        }

        // Fallback: if no exception is thrown, verify the browser did not actually reach the target page.
        if (!offlineBlockedNavigation) {
            String currentUrl = chrome.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("example.com"), "Expected offline mode to block real navigation.");
        }

        Map<String, Object> online = new HashMap<>();
        online.put("offline", false);
        online.put("latency", 20);
        online.put("downloadThroughput", 50000);
        online.put("uploadThroughput", 50000);
        online.put("connectionType", "wifi");
        chrome.executeCdpCommand("Network.emulateNetworkConditions", online);

        chrome.navigate().to(baseUrl);
        List<WebElement> loginButtons = chrome.findElements(By.id("login-button"));
        Assert.assertFalse(loginButtons.isEmpty(), "Expected page to load again after online mode is restored.");
        Assert.assertTrue(loginButtons.get(0).isDisplayed(), "Expected login button to be visible after online mode is restored.");
    }
}
