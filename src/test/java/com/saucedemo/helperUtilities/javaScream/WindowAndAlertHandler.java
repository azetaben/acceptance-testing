package com.saucedemo.helperUtilities.javaScream;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WindowAndAlertHandler {
    private final Logger log = LoggerFactory.getLogger(WindowAndAlertHandler.class);
    private WebDriver driver;

    public WindowAndAlertHandler(WebDriver driver) {
        this.driver = driver;
    }

    // 1. Switch to a new window
    public void switchToNewWindow() {
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                log.info("Switched to new window: " + window);
                break;
            }
        }

    }

    // 2. Switch back to the original window
    public void switchToOriginalWindow(String originalWindow) {
        driver.switchTo().window(originalWindow);
        log.info("Switched back to original window: " + originalWindow);
    }

    // 3. Get the current window handle
    public String getCurrentWindowHandle() {
        log.info("Current window handle: " + driver.getWindowHandle());
        return driver.getWindowHandle();
    }

    // 4. Get all window handles
    public Set<String> getAllWindowHandles() {
        log.info("All window handles: " + driver.getWindowHandles());
        return driver.getWindowHandles();
    }

    // 5. Close the current window
    public void closeCurrentWindow() {
        log.info("Closing current window: " + driver.getWindowHandle());
        driver.close();
    }

    // 6. Switch to a specific window by handle
    public void switchToWindowByHandle(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    // 7. Switch to a new tab
    public void switchToNewTab() {
        driver.switchTo().newWindow(WindowType.TAB);
        log.info("Switched to new tab");
    }

    // 8. Switch to a new window
    public void switchToTheNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    // 9. Get the title of the current window
    public String getCurrentWindowTitle() {
        log.info("Current window title: " + driver.getTitle());
        return driver.getTitle();
    }

    // 10. Get the URL of the current window
    public String getCurrentWindowUrl() {
        log.info("Current window URL: " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    // 11. Switch to alert
    public void switchToAlert() {
        log.info("Switching to alert");
        driver.switchTo().alert();
    }

    // 12. Accept an alert
    public void acceptAlert() {
        Alert alert = driver.switchTo().alert();
        alert.accept();
        log.info("Alert accepted");
    }

    // 13. Dismiss an alert
    public void dismissAlert() {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
        log.info("Alert dismissed");
    }

    // 14. Get the text of an alert

    // 18. Get the number of open windows
    public int getNumberOfOpenWindows() {
        log.info("Number of open windows: " + driver.getWindowHandles().size());
        return driver.getWindowHandles().size();
    }

    // 19. Close all windows except the original
    public void closeAllWindowsExceptOriginal(String originalWindow) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                driver.close();
            }
        }
        driver.switchTo().window(originalWindow);
    }

    // 20. Switch to the last opened window
    public void switchToLastOpenedWindow() {
        String lastWindowHandle = driver.getWindowHandles().stream()
                .reduce((first, second) -> second).orElse(null);
        if (lastWindowHandle != null) {
            driver.switchTo().window(lastWindowHandle);
            log.info("Switched to last opened window: " + lastWindowHandle);
        } else {
            log.warn("No windows opened.");
        }
    }

    // 21. Switch to the first opened window
    public void switchToFirstOpenedWindow() {
        String firstWindowHandle = driver.getWindowHandles().stream()
                .findFirst().orElse(null);
        if (firstWindowHandle != null) {
            driver.switchTo().window(firstWindowHandle);
            log.info("Switched to first opened window: " + firstWindowHandle);
        } else {
            log.warn("No windows opened.");
        }
    }

    // 22. Get the window handles as a list
    public List<String> getWindowHandlesAsList() {
        log.info("Window handles: " + driver.getWindowHandles());
        return new ArrayList<>(driver.getWindowHandles());
    }

    // 23. Switch to a window by index
    public void switchToWindowByIndex(int index) {
        List<String> windowHandles = getWindowHandlesAsList();
        if (index >= 0 && index < windowHandles.size()) {
            driver.switchTo().window(windowHandles.get(index));
            log.info("Switched to window at index: " + index);
        } else {
            log.warn("Invalid window index: " + index);
        }
    }

    // 24. Get the window handle of the original window
    public String getOriginalWindowHandle() {
        return driver.getWindowHandle();
    }

    // 25. Print all window titles
    public void printAllWindowTitles() {
        for (String windowHandle : driver.getWindowHandles()) {
            switchToWindowByHandle(windowHandle);
            System.out.println("Window Title: " + getCurrentWindowTitle());
            switchToOriginalWindow(getOriginalWindowHandle());
        }
        switchToOriginalWindow(getOriginalWindowHandle());
        log.info("All window titles printed.");
    }

    // 26. Print all window URLs
    public void printAllWindowUrls() {
        for (String windowHandle : driver.getWindowHandles()) {
            switchToWindowByHandle(windowHandle);
            System.out.println("Window URL: " + getCurrentWindowUrl());
        }
        switchToOriginalWindow(getOriginalWindowHandle());
    }

    // 27. Switch to the next window
    public void switchToNextWindow() {
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                log.info("Switched to next window: " + window);
                break;
            }
        }
    }

    // 28. Switch to the previous window
    public void switchToPreviousWindow() {
        String currentWindow = driver.getWindowHandle();
        List<String> allWindows = new ArrayList<>(driver.getWindowHandles());
        int currentIndex = allWindows.indexOf(currentWindow);
        if (currentIndex > 0) {
            driver.switchTo().window(allWindows.get(currentIndex - 1));
            log.info("Switched to previous window: " + allWindows.get(currentIndex - 1));
        } else {
            log.warn("No previous window available.");
        }
    }

    // 29. Close all windows
    public void closeAllWindows() {
        for (String window : driver.getWindowHandles()) {
            driver.switchTo().window(window);
            log.info("Closing window: " + window);
            driver.close();
        }
    }

    // 30. Switch to a window and perform an action
    public void switchToWindowAndPerformAction(String windowHandle, Runnable action) {
        switchToWindowByHandle(windowHandle);
        log.info("Switched to window: " + windowHandle);
        action.run();
    }

    // 31. Get the window title of a specific window handle
    public String getWindowTitleByHandle(String windowHandle) {
        switchToWindowByHandle(windowHandle);
        return getCurrentWindowTitle();
    }

    // 32. Get the URL of a specific window handle
    public String getWindowUrlByHandle(String windowHandle) {
        switchToWindowByHandle(windowHandle);
        log.info("Window URL: " + getCurrentWindowUrl());
        return getCurrentWindowUrl();
    }

    // 33. Check if a specific window is open
    public boolean isWindowOpen(String windowHandle) {
        log.info("Checking if window is open: " + windowHandle);
        return driver.getWindowHandles().contains(windowHandle);
    }

    // 34. Switch to a window and return to the original
    public void switchToWindowAndReturn(String windowHandle) {
        String originalWindow = getCurrentWindowHandle();
        switchToWindowByHandle(windowHandle);
        log.info("Switched to window: " + windowHandle);
        switchToOriginalWindow(originalWindow);
        log.info("Switched back to original window: " + originalWindow);
    }

    // 35. Get the number of alerts shown
    public int getAlertCount() {
        // This method is a placeholder as Selenium does not provide a direct way to count alerts.
        // You can implement your own logic to track alerts if needed.
        return 0; // Placeholder
    }

    // 36. Wait for a specific window to be open
    public void waitForWindowToBeOpen(String windowHandle) {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> {
            log.info("Waiting for window to be open: " + windowHandle);
            return driver.getWindowHandles().contains(windowHandle);
        });
    }

    // 37. Wait for a specific alert to be present
    public void waitForAlertToBePresent() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.alertIsPresent());
    }

    // 38. Get the alert text and accept it
    public String getAlertTextAndAccept() {
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        log.info("Alert accepted with text: " + text);
        return text;
    }

    // 39. Get the alert text and dismiss it
    public String getAlertTextAndDismiss() {
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.dismiss();
        return text;
    }

    // 40. Send keys to an alert and accept it
    public void sendKeysToAlertAndAccept(String keys) {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(keys);
        alert.accept();
        log.info("Alert accepted with keys: " + keys);
    }

    // 41. Send keys to an alert and dismiss it
    public void sendKeysToAlertAndDismiss(String keys) {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(keys);
        alert.dismiss();
        log.info("Alert dismissed with keys: " + keys);
    }

    // 42. Get the alert text without accepting or dismissing
    public String getAlertText() {
        log.info("Alert text: " + driver.switchTo().alert().getText());
        return driver.switchTo().alert().getText();
    }


    // 45. Check if an alert is present
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            log.info("Alert is present");
            return true;
        } catch (NoAlertPresentException e) {
            log.info("Alert is not present");
            return false;
        }
    }
}
