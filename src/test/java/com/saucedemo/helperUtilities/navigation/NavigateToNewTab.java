package com.saucedemo.helperutilities.navigation;

import com.saucedemo.constants.FrameworkConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class NavigateToNewTab {

    private static final Logger log = LogManager.getLogger(NavigateToNewTab.class);
    private static WebDriver driver;

    public static void setDriver(WebDriver driver) {
        NavigateToNewTab.driver = driver;
    }

    public static void navigateToNewTab() {
        log.info("Navigating to new tab.");
        String parent = driver.getWindowHandle();
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!parent.equals(handle)) {
                driver.switchTo().window(handle);
                log.info("Switched to child window: " + handle);
                break;
            }
        }
    }

    public static void closeChildWindow() {
        log.info("Closing child window.");
        String parent = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!parent.equals(handle)) {
                driver.switchTo().window(handle).close();
                log.info("Closed child window: " + handle);
            }
        }
        driver.switchTo().window(parent);
        log.info("Switched back to parent window.");
    }
}
