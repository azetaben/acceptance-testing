package com.saucedemo.helperUtilities.Actions;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SeleniumActions {
    private static final Logger log = LogManager.getLogger(SeleniumActions.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    public SeleniumActions(WebDriver driver) {
        this.driver = WebDrv.getInstance().getWebDriver();
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        ExplicitWaitFactory.setDriver(this.driver);
    }

    public void clickElement(WebElement element) {
        log.info("Clicking on element: " + element);
        WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        if (clickable != null) {
            clickable.click();
        }
        log.info("Element clicked successfully.");
    }

    /**
     * 2. Clicks on an element located by 'by' after ensuring it's clickable.
     */
    public void clickElement(By locator) {
        log.info("Clicking on element with locator: " + locator);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
        if (element != null) {
            element.click();
        }
        log.info("Element clicked successfully.");
    }

    /**
     * 3. Sends keys to a WebElement after ensuring its visible and clearing it first.
     */
    public void sendKeysToElement(WebElement element, String text) {
        WebElement visibleElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        if (visibleElement == null) {
            throw new NoSuchElementException("Unable to locate element for send keys: " + element);
        }
        visibleElement.clear();
        log.info("Sending keys: " + text + " to element: " + element);
        visibleElement.sendKeys(text);
        log.info("Keys sent successfully: " + text);
    }

    /**
     * 4. Sends keys to an element located by 'by' after ensuring its visible and clearing it first.
     */
    public void sendKeysToElement(By locator, String text) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) {
            throw new NoSuchElementException("Unable to locate element for send keys: " + locator);
        }
        element.clear();
        log.info("Sending keys: " + text + " to element with locator: " + locator);
        element.sendKeys(text);
        log.info("Keys sent successfully: " + text);
    }

    /**
     * 5. Clears the text from an input field or textarea element.
     */
    public void clearElement(WebElement element) {
        WebElement visible = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        if (visible != null) {
            visible.clear();
        }
        log.info("Text cleared from element: " + element);
    }

    /**
     * 6. Clears the text from an input field or textarea element located by 'by'.
     */
    public void clearElement(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) element.clear();
        log.info("Text cleared from element with locator: " + locator);
    }

    /**
     * 7. Submits a form via a WebElement (usually a button within the form).
     */
    public void submitElement(WebElement element) {
        log.info("Form submitted via element: " + element.getText());
        WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        if (clickable != null) {
            clickable.submit();
        }
    }

    /**
     * 8. Submits a form via an element located by 'by'.
     */
    public void submitElement(By locator) {
        log.info("Form submitted via element with locator: " + locator);
        WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
        if (clickable != null) {
            clickable.submit();
        }
        log.info("Form submitted successfully.");
    }

    /**
     * 9. Performs a mouse hover action over a WebElement.
     */
    public void hoverOverElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).perform();
        log.info("Hovered over element: " + element.getText());
    }


    /**
     * 11. Performs a double-click action on a WebElement.
     */
    public void doubleClickElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("Double-clicking on element: " + element.getText());
        actions.doubleClick(element).perform();
        log.info("Double-click action performed successfully.");
    }

    /**
     * 12. Performs a right-click (context click) action on a WebElement.
     */
    public void rightClickElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        actions.contextClick(element).perform();
        log.info("Right-click action performed successfully.");
    }

    /**
     * 13. Drags a source WebElement and drops it onto a target WebElement.
     */
    public void dragAndDrop(WebElement source, WebElement target) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, source);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, target);
        log.info("Dragging element from: " + source.getText() + " to: " + target.getText());
        actions.dragAndDrop(source, target).perform();
        log.info("Drag and drop action performed successfully.");
    }

    /**
     * 14. Clicks on a WebElement using JavaScript (useful for obscured elements).
     */
    public void jsClick(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.info("Element clicked using JavaScript: " + element.getText());
    }

    /**
     * 15. Gets the visible text of a WebElement after ensuring it's visible.
     */
    public String getTextFromElement(WebElement element) {
        WebElement visible = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        if (visible == null) return "";
        log.info("Getting text from element: " + visible.getText());
        return visible.getText();
    }

    /**
     * 16. Gets the visible text of an element located by 'by' after ensuring it's visible.
     */
    public String getTextFromElement(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) {
            throw new NoSuchElementException("Unable to locate element for text read: " + locator);
        }
        log.info("Getting text from element with locator: " + locator);
        return element.getText();
    }

    /**
     * 17. Gets the value of a specific attribute from a WebElement.
     */
    public String getAttributeFromElement(WebElement element, String attributeName) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Getting attribute value: " + attributeName + " from element: " + element.getText());
        return element.getDomAttribute(attributeName);
    }

    /**
     * 18. Gets the value of a specific attribute from an element located by 'by'.
     */
    public String getAttributeFromElement(By locator, String attributeName) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, locator);
        if (element == null) {
            throw new NoSuchElementException("Unable to locate element for attribute read: " + locator);
        }
        log.info("Getting attribute value: " + attributeName + " from element with locator: " + locator);
        return element.getDomAttribute(attributeName);
    }

    /**
     * 19. Checks if a WebElement is displayed.
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Checking if element is displayed: " + element.getText());
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            log.error("Element is not displayed: " + e.getMessage());
            return false;
        }
    }


    /**
     * 21. Checks if a WebElement is enabled.
     */
    public boolean isElementEnabled(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Checking if element is enabled: " + element.getText());
        return element.isEnabled();
    }

    /**
     * 22. Checks if a WebElement (like checkbox or radio button) is selected.
     */
    public boolean isElementSelected(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Checking if element is selected: " + element.getText());
        return element.isSelected();
    }

    // --- Dropdown (Select) Handling ---

    /**
     * 23. Selects an option from a dropdown WebElement by its visible text.
     */
    public void selectDropdownByVisibleText(WebElement dropdownElement, String visibleText) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, dropdownElement);
        Select select = new Select(dropdownElement);
        log.info("Selecting option by visible text: " + visibleText);
        select.selectByVisibleText(visibleText);
        log.info("Option selected successfully.");
    }

    // Element Interactions


    public void sendKeys(By locator, String keys) {
        log.info("Sending keys: " + keys + " to element with locator: " + locator);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) {
            throw new NoSuchElementException("Unable to locate element for send keys: " + locator);
        }
        element.sendKeys(keys);
        log.info("Keys sent successfully: " + keys);
    }

    public String getText(By locator) {
        log.info("Getting text from element with locator: " + locator);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) {
            throw new NoSuchElementException("Unable to locate element for text read: " + locator);
        }
        return element.getText();
    }

    public void clearField(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) element.clear();
        log.info("Field cleared successfully.");
    }

    public boolean isElementDisplayed(By locator) {
        try {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, locator);
            return driver.findElement(locator).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
            log.error("Element not found for locator " + locator + ": " + e.getMessage());
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        return driver.findElement(locator).isEnabled();
    }

    public boolean isElementSelected(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        log.info("Checking if element is selected: " + locator);
        return driver.findElement(locator).isSelected();
    }

    // Navigation
    public void navigateTo(String url) {
        driver.get(url);
        log.info("Navigated to URL: " + url);
    }

    public void refreshPage() {
        driver.navigate().refresh();
        log.info("Page refreshed.");
    }

    public void back() {
        driver.navigate().back();
        log.info("Navigated back.");
    }

    public void forward() {
        driver.navigate().forward();
        log.info("Navigated forward.");
    }

    // Waits
    public void waitForElementToBeVisible(By locator) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        log.info("Element with locator: " + locator + " is visible.");
    }

    public void waitForElementToBeClickable(By locator) {
        log.info("Waiting for element with locator: " + locator + " to be clickable.");
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
    }

    // Window and Frame Handling
    public void switchToWindow(String windowHandle) {
        log.info("Switching to window with handle: " + windowHandle);
        driver.switchTo().window(windowHandle);
        log.info("Switched to window successfully.");
    }

    public void switchToFrame(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
        log.info("Switched to frame with locator: " + locator);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        log.info("Switched to default content.");
    }

    // JavaScript Executor
    public Object executeJavaScript(String script, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script);
    }

    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
        log.info("Scrolled to element with locator: " + locator);
    }

    // Alerts and Pop-ups
    public void acceptAlert() {
        log.info("Accepting alert.");
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
        log.info("Alert dismissed.");
    }

    public String getAlertText() {
        log.info("Getting alert text.");
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    // Cookies
    public void addCookie(String name, String value) {
        driver.manage().addCookie(new Cookie(name, value));
        log.info("Cookie added with name: " + name + " and value: " + value);
    }

    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
        log.info("Cookie deleted with name: " + name);
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
        log.info("All cookies deleted.");
    }

    // Screenshots
    public String takeScreenshot(String filePath) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destinationPath = filePath + "/" + System.currentTimeMillis() + ".png";
        Files.copy(screenshot.toPath(), Paths.get(destinationPath));
        return destinationPath;
    }

    // File Uploads
    public void uploadFile(By locator, String filePath) {
        driver.findElement(locator).sendKeys(filePath);
        log.info("File uploaded successfully.");
    }

    // Miscellaneous
    public Set<String> getWindowHandles() {
        log.info("Getting window handles.");
        return driver.getWindowHandles();
    }

    public String getCurrentUrl() {
        log.info("Getting current URL.");
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        log.info("Getting page title.");
        return driver.getTitle();
    }

    // Additional Element Interactions
    public void doubleClick(By locator) {
        Actions actions = new Actions(driver);
        log.info("Double-clicked on element with locator: " + locator);
        actions.doubleClick(driver.findElement(locator)).perform();

    }

    public void rightClick(By locator) {
        Actions actions = new Actions(driver);
        log.info("Right-clicked on element with locator: " + locator);
        actions.contextClick(driver.findElement(locator)).perform();
    }

    public void dragAndDrop(By sourceLocator, By targetLocator) {
        Actions actions = new Actions(driver);
        log.info("Dragged element from: " + sourceLocator + " to: " + targetLocator);
        actions.dragAndDrop(driver.findElement(sourceLocator), driver.findElement(targetLocator)).perform();

    }

    public void hoverOverElement(By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(locator)).perform();
        log.info("Hovered over element with locator: " + locator);
    }

    public void sendKeysWithActions(By locator, String keys) {
        Actions actions = new Actions(driver);
        log.info("Sending keys: " + keys + " to element with locator: " + locator);
        actions.moveToElement(driver.findElement(locator)).click().sendKeys(keys).perform();
        log.info("Keys sent successfully: " + keys);
    }

    public void selectDropdownByVisibleText(By locator, String visibleText) {
        WebElement dropdown = driver.findElement(locator);
        dropdown.click();
        dropdown.findElement(By.xpath("//option[text()='" + visibleText + "']")).click();
        log.info("Selected option with visible text: " + visibleText);
    }

    public void selectDropdownByValue(By locator, String value) {
        WebElement dropdown = driver.findElement(locator);
        dropdown.click();
        dropdown.findElement(By.xpath("//option[@value='" + value + "']")).click();
        log.info("Selected option with value: " + value);
    }

    public void selectDropdownByIndex(By locator, int index) {
        WebElement dropdown = driver.findElement(locator);
        dropdown.click();
        dropdown.findElements(By.tagName("option")).get(index).click();
        log.info("Selected option at index: " + index);
    }

    public List<WebElement> getAllDropdownOptions(By locator) {
        WebElement dropdown = driver.findElement(locator);
        return dropdown.findElements(By.tagName("option"));
    }

    public void checkCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (!checkbox.isSelected()) {
            checkbox.click();
            log.info("Checkbox checked.");
        }
    }

    public void uncheckCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (checkbox.isSelected()) {
            checkbox.click();
            log.info("Checkbox unchecked.");
        }
    }

    public void switchToAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();
        log.info("Switched to alert.");
    }


    public void switchToNewWindow() {
        String currentWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public void closeCurrentWindow() {
        driver.close();
        log.info("Current window closed.");
        switchToNewWindow();
        log.info("Switched to new window.");

    }

    public void switchToPreviousWindow(String previousWindow) {
        driver.switchTo().window(previousWindow);
        log.info("Switched to previous window.");
    }

    public void takeScreenshotWithTimestamp(String directory) throws IOException {
        String filePath = directory + "/" + System.currentTimeMillis() + ".png";
        takeScreenshot(filePath);
        log.info("Screenshot taken and saved to: " + filePath);
    }

    public void scrollDown(int pixels) {
        executeJavaScript("window.scrollBy(0," + pixels + ");");
        log.info("Scrolled down by " + pixels + " pixels.");
    }

    public void scrollUp(int pixels) {
        executeJavaScript("window.scrollBy(0,-" + pixels + ");");
        log.info("Scrolled up by " + pixels + " pixels.");
    }

    public void scrollToBottom() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
        log.info("Scrolled to the bottom of the page.");
    }

    public void scrollToTop() {
        executeJavaScript("window.scrollTo(0, 0);");
        log.info("Scrolled to the top of the page.");
    }

    public void waitForElementToBeInvisible(By locator) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.INVISIBILITY, locator);
        log.info("Element with locator: " + locator + " is invisible.");
    }

    public void waitForElementToBeSelected(By locator) {
        wait.until(ExpectedConditions.elementToBeSelected(locator));
        log.info("Element with locator: " + locator + " is selected.");
    }

    public void waitForTextToBePresentInElement(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        log.info("Text: " + text + " is present in element with locator: " + locator);
    }

    public void waitForTitleToBe(String title) {
        wait.until(ExpectedConditions.titleIs(title));
        log.info("Title is: " + title);
    }

    public void waitForUrlToContain(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
        log.info("URL contains: " + urlFragment);
    }

    public void switchToIframe(By locator) {
        driver.switchTo().frame(driver.findElement(locator));
        log.info("Switched to iframe with locator: " + locator);
    }

    public void switchToDefaultFrame() {
        driver.switchTo().defaultContent();
        log.info("Switched to default content.");
    }

    public void highlightElement(By locator) {
        WebElement element = driver.findElement(locator);
        executeJavaScript("arguments[0].style.border='3px solid red'", element);
        log.info("Element with locator: " + locator + " is highlighted.");
    }

    public void removeHighlight(By locator) {
        WebElement element = driver.findElement(locator);
        executeJavaScript("arguments[0].style.border=''", element);
    }

    public void waitForPageToLoad() {
        wait.until(webDriver -> Objects.equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"), "complete"));
        log.info("Page loaded.");
    }

    public void navigateToPreviousPage() {
        driver.navigate().back();
        log.info("Navigated back.");
    }

    public void navigateToNextPage() {
        driver.navigate().forward();
        log.info("Navigated forward.");
    }

    public void switchToParentWindow() {
        String parentWindow = driver.getWindowHandles().iterator().next();
        driver.switchTo().window(parentWindow);
    }

    public void uploadFileUsingRobot(String filePath) throws AWTException {
        Robot robot = new Robot();
        robot.setAutoDelay(500);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.setAutoDelay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        log.info("File uploaded using Robot class.");
    }

    public void clearBrowserCache() {
        driver.manage().deleteAllCookies();
        log.info("Browser cache cleared.");
    }

    public void navigateTo(String url, String expectedTitle) {
        driver.get(url);
        wait.until(ExpectedConditions.titleIs(expectedTitle));
        log.info("Navigated to URL: " + url + " with expected title: " + expectedTitle);
    }

    public void switchToWindowByTitle(String title) {
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (Objects.equals(driver.getTitle(), title)) {
                break;
            }
        }
    }

    public void closeAllWindowsExcept(String windowHandle) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(windowHandle)) {
                driver.switchTo().window(handle);
                driver.close();
                driver.switchTo().window(windowHandle);
            }
        }
        driver.switchTo().window(windowHandle);
        log.info("All windows except " + windowHandle + " are closed.");
    }

    public void navigateBackToPreviousPage() {
        driver.navigate().back();
        log.info("Navigated back to the previous page.");
    }

    public void navigateForwardToNextPage() {
        driver.navigate().forward();
        log.info("Navigated forward to the next page.");
    }

    public void switchToNewTab() {
        String currentTab = driver.getWindowHandle();
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(currentTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }
    }

    public void closeCurrentTab() {
        driver.close();
        switchToNewTab();
        log.info("Current tab closed.");
    }

    public void switchToTabByIndex(int index) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
        log.info("Switched to tab with index: " + index);
    }

    public void switchToTabByTitle(String title) {
        for (String tab : driver.getWindowHandles()) {
            driver.switchTo().window(tab);
            if (Objects.equals(driver.getTitle(), title)) {
                break;
            }
        }
    }

    public void switchToTabByUrl(String url) {
        for (String tab : driver.getWindowHandles()) {
            driver.switchTo().window(tab);
            if (Objects.equals(driver.getCurrentUrl(), url)) {
                break;
            }
        }
    }


    // JavaScript Executor
    public Object executeJavaScript(String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script);
    }

}
