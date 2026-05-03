package com.saucedemo.helperUtilities.window;

import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class WindowHelper {

    private final WebDriver driver;
    private final Logger log = LogManager.getLogger(WindowHelper.class);

    public WindowHelper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits until the page is fully loaded.
     *
     * @param loopCount (not used, just added for compatibility)
     * @throws Exception if the page does not load in the specified time.
     */
    public void checkPageIsReady(int loopCount) throws Exception {
        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
            assert driver != null;
            return Objects.equals(((JavascriptExecutor) driver).executeScript("return document.readyState"), "complete");
        };
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.explicitWait));
        wait.until(pageLoadCondition);
    }

    /**
     * Switches the driver focus to the parent (main) window or frame.
     */
    public void switchToParentWindowOrFrame() {
        log.info("Switching to parent window/frame...");
        driver.switchTo().defaultContent();
    }

    /**
     * Switches the driver focus to a specific window by its index.
     *
     * @param index The index of the window to switch to (starting from 1).
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public void switchToWindowByIndex(int index) {
        log.info("Switching to window with index: " + index);
        Set<String> windowHandles = driver.getWindowHandles();
        if (index > windowHandles.size() || index <= 0) {
            throw new IndexOutOfBoundsException("Invalid window index: " + index + ". There are only " + windowHandles.size() + " windows open.");
        }
        List<String> windowHandlesList = new ArrayList<>(windowHandles);
        driver.switchTo().window(windowHandlesList.get(index - 1));
    }

    /**
     * Switches the driver focus to a window by it's title
     *
     * @param windowTitle the title of the window to switch to
     * @throws RuntimeException if there is no window with the specified title
     */
    public void switchToWindowByTitle(String windowTitle) {
        log.info("Switching to window with title: " + windowTitle);
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            if (Objects.requireNonNull(driver.getTitle()).contains(windowTitle)) {
                log.info("Switched to window with title: " + windowTitle);
                return;
            }
        }
        throw new RuntimeException("No window found with title: " + windowTitle);
    }

    /**
     * Closes all child windows/tabs and switches the driver focus back to the main window.
     */
    public void closeAllChildWindowsAndSwitchToMainWindow() {
        log.info("Closing all child windows and switching to main window...");
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();

        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                driver.close();
                log.info("Closed child window with handle: " + windowHandle);
            }
        }
        driver.switchTo().window(mainWindowHandle);
        log.info("Switched to main window with handle: " + mainWindowHandle);
    }

    /**
     * Navigates the browser back to the previous page.
     */
    public void navigateBack() {
        log.info("Navigating back...");
        driver.navigate().back();
    }

    /**
     * Navigates the browser forward to the next page.
     */
    public void navigateForward() {
        log.info("Navigating forward...");
        driver.navigate().forward();
    }

    /**
     * Opens a new tab and switches the driver focus to it.
     *
     * @throws Exception if there is any problem when switching.
     */
    public void openNewTabAndSwitchToIt() throws Exception {
        log.info("Opening a new tab and switching to it...");
        ((JavascriptExecutor) driver).executeScript("window.open();");
        switchToLastWindow();
    }

    /**
     * Switches the driver focus to the last opened window.
     */
    public void switchToLastWindow() {
        log.info("Switching to last window...");
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowHandlesList = new ArrayList<>(windowHandles);
        driver.switchTo().window(windowHandlesList.get(windowHandlesList.size() - 1));
    }

    /**
     * Closes the current window/tab and switches the driver focus to the parent window/tab. If
     * there's only one window, it just closes it.
     *
     * @throws Exception if there is any problem when switching.
     */
    public void closeCurrentWindowAndSwitchToParent() throws Exception {
        log.info("Closing current window and switching to parent...");
        Set<String> windowHandles = driver.getWindowHandles();
        String currentWindowHandle = driver.getWindowHandle();

        if (windowHandles.size() > 1) {
            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(currentWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            driver.close();
            switchToLastWindow();
            log.info("Closed a child window and switched to parent");

        } else {
            driver.close();
            log.info("Closed the only window");
        }
    }

    /**
     * Closes the current window/tab
     */
    public void closeCurrentWindow() {
        log.info("Closing current window...");
        driver.close();
    }

    /**
     * get all window handles
     */
    public Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * get current window handle
     */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }
}
