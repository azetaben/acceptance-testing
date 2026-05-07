package com.saucedemo.helperutilities.window;

import com.saucedemo.constants.FrameworkConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class NavigateToNewTab {

    private static WebDriver driver;

    public static void navigateToNewTab() {
        String parent = driver.getWindowHandle();
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!parent.equals(handle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public static void closeChildWindow() {
        String parent = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!parent.equals(handle)) {
                driver.switchTo().window(handle).close();
            }
        }
        driver.switchTo().window(parent);
    }
}
