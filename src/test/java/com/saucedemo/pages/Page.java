package com.saucedemo.pages;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.assertion.VerificationHelper;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.helperutilities.javascript.JavaScriptHelper;
import com.saucedemo.helperutilities.navigation.NavigateToNewTab;
import com.saucedemo.helperutilities.webelement.WebElementOrderChecker;
import com.saucedemo.helperutilities.webelement.WebElementOrderCheckerImpl;
import com.saucedemo.utils.ConfigLoader;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Page {
    private static final Logger log = LogManager.getLogger(Page.class);
    protected final WebDriver driver = WebDrv.getInstance().getWebDriver();
    protected Actions actions = new Actions(WebDrv.getInstance().getWebDriver());
    protected VerificationHelper verificationHelper = new VerificationHelper();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));

    @FindBy(css = ".login_logo")
    private WebElement logo;
    @FindBy(xpath = ".//*")
    private WebElement pageContent;
    @FindBy(css = ".subheader")
    private WebElement subHeader;


    public static boolean elementExists(By locator) {
        try {
            getElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isTextDisplayedOnThePage(String str) {
        return elementExists(By.xpath("//*[normalize-space(.)='" + str + "']"));
    }

    public static WebElement getElement(By locator) {
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, locator);
    }


    private CheckoutStepOnePage checkoutYourInformationPage() {
        return PageManager.getInstance().getPage(CheckoutStepOnePage.class);
    }


    public void load(String endPoint) {
        driver.get(ConfigLoader.getInstance().getBaseUrl() + endPoint);
    }

    public void waitForOverlaysToDisappear(By overlay) {
        List<WebElement> overlays = driver.findElements(overlay);
        log.debug("Overlay count: " + overlays.size());
        if (!overlays.isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(overlays));
            log.debug("Overlays are invisible");
        } else {
            log.debug("No overlays found");
        }
    }

    public void navigateTo(String url) {
        WebDrv.getInstance().getWebDriver().navigate().to(url);
        log.info("Navigating to URL: " + url);
    }

    public void navigateToRelativeUrl(String relativeUrl) {
        log.info("Navigating to relative URL: " + relativeUrl);
        WebDrv.getInstance().getWebDriver().get(getCurrentUrl() + relativeUrl);
    }

    public String getCurrentUrl() {
        log.info("Getting current URL");
        return WebDrv.getInstance().getWebDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        log.info("Getting page title");
        return WebDrv.getInstance().getWebDriver().getTitle();
    }

    public String getThisPageTitle() {
        log.info("Page title: " + WebDrv.getInstance().getWebDriver().getTitle());
        return WebDrv.getInstance().getWebDriver().getTitle();
    }

    public String getThisPageUrl() {
        log.info("Page url: " + WebDrv.getInstance().getWebDriver().getCurrentUrl());
        return verificationHelper.getCurrentPageUrl();
    }

    public void refreshPage() {
        log.info("Refreshing the page");
        WebDrv.getInstance().getWebDriver().navigate().refresh();
    }

    public void goBack() {
        log.info("Navigating back");
        WebDrv.getInstance().getWebDriver().navigate().back();
    }

    public void goForward() {
        log.info("Navigating forward");
        WebDrv.getInstance().getWebDriver().navigate().forward();
    }

    public void clickOnBrowserBackButton() {
        WebDrv.getInstance().getWebDriver().navigate().back();
    }

    public String getDomainName() {
        log.info("Getting domain name");
        String currentUrl = getCurrentUrl();
        try {
            return new java.net.URL(currentUrl).getHost();
        } catch (java.net.MalformedURLException e) {
            log.error("Malformed URL: " + currentUrl, e);
            return null;
        }
    }

    public String getProtocol() {
        log.info("Getting protocol");
        String currentUrl = getCurrentUrl();
        try {
            return new java.net.URL(currentUrl).getProtocol();
        } catch (java.net.MalformedURLException e) {
            log.error("Malformed URL: " + currentUrl, e);
            return null;
        }
    }

    public String getPageSource() {
        log.info("Getting page source");
        return WebDrv.getInstance().getWebDriver().getPageSource();
    }


    public boolean isPageFullyLoaded() {
        String state = (String) tryJavascript("return document.readyState;");
        return state.matches("complete|loaded|interactive");
    }

    public void waitForLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = wd -> {
            if (wd == null) return false;
            return "complete".equals(((JavascriptExecutor) wd).executeScript("return document.readyState"));
        };
        new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.explicitWait))
                .until(pageLoadCondition);
        WebDrv.getInstance().getWebDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(GlobalVarsHelper.DEFAULT_IMPLICIT_TIMEOUT));
    }

    public void checkPageIsReady(int loopCount) {
        ExpectedCondition<Boolean> pageLoadCondition = driver ->
                Objects.equals(((JavascriptExecutor) WebDrv.getInstance().getWebDriver())
                        .executeScript("return document.readyState"), "complete");
        new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.explicitWait))
                .until(pageLoadCondition);
    }


    public boolean isLogoIsDisplayed() {
        return verificationHelper.isDisplayed(logo);
    }

    public String getLogoText() {
        return verificationHelper.getText(logo);
    }

    public void clickLogoImage() {
        waitAndClick(logo);
    }

    private WebElement resolvePageHeadingElement() {
        By[] headingLocators = {By.cssSelector(".title"), By.cssSelector(".subheader")};
        WebDriverWait headingWait = new WebDriverWait(
                WebDrv.getInstance().getWebDriver(),
                Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));
        for (By locator : headingLocators) {
            try {
                WebElement el = headingWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (el != null && el.isDisplayed()) return el;
            } catch (TimeoutException | NoSuchElementException ignored) {
            }
        }
        return null;
    }

    public boolean isThisPageSubHeaderDisplayed() {
        WebElement el = resolvePageHeadingElement();
        return el != null && verificationHelper.isDisplayed(el);
    }

    public String getThisPageSubHeaderText() {
        WebElement el = resolvePageHeadingElement();
        if (el == null) {
            log.warn("No page heading element found using selectors '.title' or '.subheader'.");
            return "";
        }
        return verificationHelper.getText(el);
    }

    public boolean isHeadingTextDisplayed(String headingText) {
        boolean displayed = isTextDisplayedOnThePage(headingText);
        if (displayed) log.info("Verified that the heading text is present.");
        return displayed;
    }


    public WebElement findElement(By by) {
        log.info("Finding element: " + by);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, by);
    }

    public List<WebElement> findElements(By by) {
        log.info("Finding elements: " + by);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        return WebDrv.getInstance().getWebDriver().findElements(by);
    }

    public boolean elementDoesNotExist(By locator) {
        try {
            getElement(locator);
            return false;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return true;
        }
    }


    public void click(WebElement element) {
        WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        if (clickable == null) {
            throw new org.openqa.selenium.NoSuchElementException("Unable to click element after wait: " + element);
        }
        clickable.click();
        log.info("Clicked on element: " + element);
    }

    public void clickWithWait(WebElement element) {
        click(element);
    }

    public void waitAndClick(WebElement element) {
        WebElement clickable = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        if (clickable != null) clickable.click();
        isPageFullyLoaded();
    }

    public void sendKeys(WebElement element, String keys) {
        WebElement visible = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        if (visible == null) {
            throw new org.openqa.selenium.NoSuchElementException("Unable to send keys to element after wait: " + element);
        }
        visible.clear();
        visible.sendKeys(keys);
        log.info("Sent keys to element: " + element + " | Keys: " + keys);
    }

    public void sendKeysToElement(WebElement element, String text) {
        log.info("Sending keys: " + text);
        actions.sendKeys(element, text).perform();
    }

    public void clear(WebElement element) {
        log.info("Clearing element: " + element);
        element.clear();
    }

    public void submit(WebElement element) {
        log.info("Submitting form element: " + element);
        element.submit();
    }

    public String getText(WebElement element) {
        log.info("Getting text from element: " + element.getText());
        return element.getText();
    }

    public String getAttribute(WebElement element, String attributeName) {
        log.info("Getting attribute: " + attributeName + " from element: " + element);
        return element.getDomAttribute(attributeName);
    }

    public String getCssValue(WebElement element, String propertyName) {
        log.info("Getting CSS value: " + propertyName + " from element: " + element);
        return element.getCssValue(propertyName);
    }

    public String getTagName(WebElement element) {
        log.info("Getting tag name of element: " + element);
        return element.getTagName();
    }

    public Dimension getSize(WebElement element) {
        log.info("Getting size of element: " + element);
        return element.getSize();
    }

    public Point getLocation(WebElement element) {
        log.info("Getting location of element: " + element);
        return element.getLocation();
    }

    public Rectangle getRect(WebElement element) {
        log.info("Getting rectangle of element: " + element);
        return element.getRect();
    }

    public String getPlaceholder(WebElement element) {
        log.info("Getting placeholder of element: " + element);
        return getAttribute(element, "placeholder");
    }


    public boolean isDisplayed(WebElement element) {
        log.info("Checking if element is displayed: " + element);
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.error("Element not displayed: " + element, e);
            return false;
        }
    }

    public boolean isEnabled(WebElement element) {
        log.info("Checking if element is enabled: " + element);
        try {
            return element.isEnabled();
        } catch (NoSuchElementException | TimeoutException e) {
            log.error("Element not enabled: " + element, e);
            return false;
        }
    }

    public boolean isSelected(WebElement element) {
        log.info("Checking if element is selected: " + element);
        try {
            return element.isSelected();
        } catch (NoSuchElementException | TimeoutException e) {
            log.error("Element not selected: " + element, e);
            return false;
        }
    }

    public boolean isElementClickable(WebElement element) {
        log.info("Checking if element is clickable: " + element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public void waitForElementToBeVisible(WebElement element) {
        log.info("Waiting for element to be visible: " + element);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    public void waitForElementToBeClickable(WebElement element) {
        log.info("Waiting for element to be clickable: " + element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeInvisible(WebElement element) {
        log.info("Waiting for element to be invisible: " + element);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForTextToBePresentInElement(WebElement element, String text) {
        log.info("Waiting for text: " + text + " to be present in element: " + element);
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitForElementToBeStale(WebElement element) {
        log.info("Waiting for element to be stale: " + element);
        wait.until(ExpectedConditions.stalenessOf(element));
    }

    public WebElement waitForVisibilityOfElement(WebElement element, long durationInSeconds) {
        try {
            return new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(durationInSeconds))
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (Throwable e) {
            log.error("Visibility wait failed: " + e.getMessage());
            return null;
        }
    }

    public void waitForElementInvisible(WebElement element) {
        new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.explicitWait))
                .until(ExpectedConditions.invisibilityOf(element));
        log.info("Element is invisible: " + element);
    }

    public void waitForElementAttributeToContain(WebElement element, String attribute, String value) {
        log.info("Waiting for element attribute '" + attribute + "' to contain: " + value);
        wait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForAllElementsToBeVisible(List<WebElement> elements) {
        log.info("Waiting for all elements to be visible");
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void waitForAllElementsToBeClickable(List<WebElement> elements) {
        log.info("Waiting for all elements to be clickable");
        elements.forEach(el -> wait.until(ExpectedConditions.elementToBeClickable(el)));
    }

    public void waitForAllElementsToBeInvisible(List<WebElement> elements) {
        log.info("Waiting for all elements to be invisible");
        wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    public void waitForAllElementsTextToBePresent(List<WebElement> elements, String text) {
        log.info("Waiting for all elements to contain text: " + text);
        elements.forEach(el -> wait.until(ExpectedConditions.textToBePresentInElement(el, text)));
    }

    public void waitForAllElementsAttributeToContain(List<WebElement> elements, String attribute, String value) {
        log.info("Waiting for all elements to contain attribute '" + attribute + "': " + value);
        elements.forEach(el -> wait.until(ExpectedConditions.attributeContains(el, attribute, value)));
    }

    public void waitForAnyElementToBeVisible(List<WebElement> elements) {
        wait.until(d -> elements.stream().anyMatch(el -> {
            try { return el.isDisplayed(); } catch (Exception e) { return false; }
        }));
    }

    public void waitForAnyElementToBeClickable(List<WebElement> elements) {
        wait.until(d -> elements.stream().anyMatch(el -> {
            try { return el.isDisplayed() && el.isEnabled(); } catch (Exception e) { return false; }
        }));
    }

    public void waitForAnyElementToBeInvisible(List<WebElement> elements) {
        wait.until(d -> elements.stream().anyMatch(el -> {
            try { return !el.isDisplayed(); } catch (Exception e) { return true; }
        }));
    }

    public void waitForAnyElementAttributeToContain(List<WebElement> elements, String attribute, String value) {
        wait.until(ExpectedConditions.or(
                elements.stream()
                        .map(el -> ExpectedConditions.attributeContains(el, attribute, value))
                        .toArray(ExpectedCondition[]::new)));
    }


    public void waitForElementToBePresent(By by) {
        log.info("Waiting for element to be present: " + by);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForNumberOfElementsToBe(By by, int number) {
        log.info("Waiting for " + number + " elements: " + by);
        wait.until(ExpectedConditions.numberOfElementsToBe(by, number));
    }

    public void waitForNumberOfElementsToBeMoreThan(By by, int number) {
        log.info("Waiting for more than " + number + " elements: " + by);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
    }

    public void waitForNumberOfElementsToBeLessThan(By by, int number) {
        log.info("Waiting for fewer than " + number + " elements: " + by);
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(by, number));
    }

    public void waitForUrlContains(String text) {
        log.info("Waiting for URL to contain: " + text);
        wait.until(ExpectedConditions.urlContains(text));
    }

    public void waitForUrlToBe(String url) {
        log.info("Waiting for URL to be: " + url);
        wait.until(ExpectedConditions.urlToBe(url));
    }

    public void waitForTitleContains(String title) {
        log.info("Waiting for title to contain: " + title);
        wait.until(ExpectedConditions.titleContains(title));
    }

    public void waitForTitleIs(String title) {
        log.info("Waiting for title to be: " + title);
        wait.until(ExpectedConditions.titleIs(title));
    }


    public void moveToElement(WebElement element) {
        log.info("Moving to element: " + element);
        actions.moveToElement(element).perform();
    }

    public void moveToElementAndClick(WebElement element) {
        Actions actions = new Actions(WebDrv.getInstance().getWebDriver());
        actions.moveToElement(element).click().build().perform();
        log.info("Moved to element and clicked");
    }

    public void moveToElementAndDoubleClick(WebElement element) {
        log.info("Moving to element and double-clicking: " + element);
        actions.moveToElement(element).doubleClick().perform();
    }

    public void moveToElementWithOffset(WebElement element, int xOffset, int yOffset) {
        log.info("Moving to element with offset x=" + xOffset + " y=" + yOffset);
        actions.moveToElement(element, xOffset, yOffset).perform();
    }

    public void moveToTheElement(WebElement element) {
        Actions actions = new Actions(WebDrv.getInstance().getWebDriver());
        actions.moveToElement(element).build().perform();
    }

    public void hoverOverElement(WebElement element) {
        Actions actions = new Actions(WebDrv.getInstance().getWebDriver());
        actions.moveToElement(element).perform();
        log.info("Hovered over element: " + element);
    }

    public void hoverAndClick(WebElement element) {
        hoverOverElement(element);
        click(element);
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        log.info("Dragging and dropping");
        actions.dragAndDrop(source, target).perform();
    }

    public void dragAndDropByOffset(WebElement element, int xOffset, int yOffset) {
        log.info("Dragging and dropping by offset x=" + xOffset + " y=" + yOffset);
        actions.dragAndDropBy(element, xOffset, yOffset).perform();
    }

    public void doubleClick(WebElement element) {
        log.info("Double-clicking on element: " + element);
        actions.doubleClick(element).perform();
    }

    public void contextClick(WebElement element) {
        log.info("Right-clicking on element: " + element);
        actions.contextClick(element).perform();
    }

    public void clickAndHold(WebElement element) {
        log.info("Clicking and holding on element: " + element);
        actions.clickAndHold(element).perform();
    }

    public void clickAndHoldWithOffset(WebElement element, int xOffset, int yOffset) {
        log.info("Clicking and holding with offset x=" + xOffset + " y=" + yOffset);
        actions.clickAndHold(element).moveByOffset(xOffset, yOffset).perform();
    }

    public void release(WebElement element) {
        log.info("Releasing on element...");
        actions.release(element).perform();
    }

    public void releaseWithOffset(WebElement element, int xOffset, int yOffset) {
        log.info("Releasing with offset x=" + xOffset + " y=" + yOffset);
        actions.release(element).perform();
    }

    public void performAction(Action action) {
        log.info("Performing custom action");
        action.perform();
    }


    public void selectByVisibleText(WebElement element, String text) {
        log.info("Selecting by visible text: " + text);
        new Select(element).selectByVisibleText(text);
    }

    public void selectByValue(WebElement element, String value) {
        log.info("Selecting by value: " + value);
        new Select(element).selectByValue(value);
    }

    public void selectByIndex(WebElement element, int index) {
        log.info("Selecting by index: " + index);
        new Select(element).selectByIndex(index);
    }

    public List<String> getAllSelectedOptions(WebElement element) {
        log.info("Getting all selected options");
        return new Select(element).getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getFirstSelectedOption(WebElement element) {
        log.info("Getting first selected option");
        return new Select(element).getFirstSelectedOption().getText();
    }


    public void clickAnItemFromListByText(List<WebElement> elements, String itemText) {
        elements.stream()
                .filter(e -> e.getText().equalsIgnoreCase(itemText))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Element with text '" + itemText + "' not present"))
                .click();
        log.info("Clicked on item: " + itemText);
    }

    public void clickAnElementMatchingText(List<WebElement> elements, String itemText) {
        elements.stream()
                .filter(e -> e.getText().equalsIgnoreCase(itemText))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Element with text '" + itemText + "' not present"))
                .click();
    }

    public void clickAnyElementMatchingText(List<WebElement> elements, Predicate<WebElement> predicate) {
        elements.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Element not present"))
                .click();
    }

    public void clickElementByText(List<WebElement> elements, String targetText) {
        if (elements == null || elements.isEmpty())
            throw new IllegalArgumentException("List of WebElements cannot be null or empty.");
        if (targetText == null || targetText.trim().isEmpty())
            throw new IllegalArgumentException("Target text cannot be null or empty.");

        WebDriverWait localWait = new WebDriverWait(
                WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        for (WebElement element : elements) {
            localWait.until(ExpectedConditions.visibilityOf(element));
            if (element.getText().trim().equals(targetText)) {
                localWait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                return;
            }
        }
        throw new IllegalArgumentException("No element found with the text: " + targetText);
    }

    public void clickElementByText(By locator, String targetText) {
        if (locator == null) throw new IllegalArgumentException("Locator cannot be null.");
        clickElementByText(WebDrv.getInstance().getWebDriver().findElements(locator), targetText);
    }

    public void clickAllElements(List<WebElement> elements) {
        log.info("Clicking all elements");
        elements.forEach(this::click);
    }

    public void hoverAndClickChildByText(WebElement parentElement,
                                         List<WebElement> childElements,
                                         String targetText) {
        if (parentElement == null)
            throw new IllegalArgumentException("Parent element cannot be null.");
        if (childElements == null || childElements.isEmpty())
            throw new IllegalArgumentException("List of child elements cannot be null or empty.");
        if (targetText == null || targetText.trim().isEmpty())
            throw new IllegalArgumentException("Target text cannot be null or empty.");

        Actions actions = new Actions(WebDrv.getInstance().getWebDriver());
        WebDriverWait localWait = new WebDriverWait(
                WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        try {
            localWait.until(ExpectedConditions.visibilityOf(parentElement));
            actions.moveToElement(parentElement).perform();
            for (WebElement child : childElements) {
                localWait.until(ExpectedConditions.visibilityOf(child));
                if (child.getText().trim().equals(targetText)) {
                    actions.moveToElement(child).perform();
                    localWait.until(ExpectedConditions.elementToBeClickable(child));
                    child.click();
                    log.info("Clicked child element with text: " + targetText);
                    return;
                }
            }
            throw new IllegalArgumentException("No child element found with the text: '" + targetText + "'");
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            log.error("Element issue during hover-and-click: " + e.getMessage(), e);
            throw e;
        }
    }


    public void ScrollToElementWaitUntilDisplayedAndClick(WebElement element) {
        try {
            new JavaScriptHelper().scrollIntoView(element);
            new WebDriverWait(WebDrv.getInstance().getWebDriver(),
                    Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()))
                    .until(ExpectedConditions.visibilityOf(element));
            element.click();
            log.info("Clicked on element after scroll");
        } catch (Exception e) {
            log.error("Error scrolling to element and clicking: ", e);
        }
    }

    public void waitUntilDisplayedAndClick(WebElement element) {
        try {
            new WebDriverWait(WebDrv.getInstance().getWebDriver(),
                    Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()))
                    .until(ExpectedConditions.visibilityOf(element));
            element.click();
            log.info("Clicked on element");
        } catch (Exception e) {
            log.error("Error waiting and clicking: ", e);
        }
    }

    public void scrollToElement(WebElement element) {
        log.info("Scrolling to element: " + element);
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToBottom() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToTop() {
        executeJavaScript("window.scrollTo(0, 0)");
    }

    public void highlightElement(WebElement element) {
        log.info("Highlighting element: " + element);
        executeJavaScript("arguments[0].style.border='3px solid red'", element);
    }

    public void removeHighlight(WebElement element) {
        log.info("Removing highlight from element: " + element);
        executeJavaScript("arguments[0].style.border=''", element);
    }

    public void clickElementWithJS(WebElement element) {
        log.info("Clicking element with JS: " + element);
        executeJavaScript("arguments[0].click();", element);
    }

    public void sendKeysWithJS(WebElement element, String text) {
        log.info("Sending keys with JS to element: " + element);
        executeJavaScript("arguments[0].value='" + text + "';", element);
    }


    public synchronized Object tryJavascript(String script, Object... args) {
        try {
            return execJavascript(script, args);
        } catch (Exception ignore) {
            return "";
        }
    }

    public synchronized Object execJavascript(String script, Object... args) {
        return ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript(script, args);
    }

    public void executeJavaScript(String script) {
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript(script);
        log.info("Executed JavaScript: " + script);
    }

    public void executeJavaScript(String script, Object... args) {
        log.info("Executing JavaScript: " + script);
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript(script, args);
    }

    public Object executeJavaScriptWithReturn(String script, Object... args) {
        log.info("Executing JavaScript with return: " + script);
        return ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript(script, args);
    }


    public boolean verifyPageContentByText(String expectedText) {
        try {
            WebElement body = WebDrv.getInstance().getWebDriver().findElement(By.cssSelector("*"));
            return body.getText().contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyPageContentText(String pageText) {
        log.info("Page content text verification: " + pageText);
        return verifyElementByText(pageText);
    }

    public boolean verifyElementByText(String text) {
        try {
            WebElement element = new WebDriverWait(
                    WebDrv.getInstance().getWebDriver(),
                    Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[text()='" + text + "']")));
            if (element.isDisplayed()) {
                log.info("Element verified with text: " + text);
                return true;
            }
            log.warn("Element with text '" + text + "' is not displayed.");
            return false;
        } catch (Exception e) {
            log.error("Element verification by text failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isExpectedPageContentPresentAndVisible(String expectedPageContent) {
        try {
            return WebDrv.getInstance().getWebDriver()
                    .findElement(By.xpath("//*[contains(text(),'" + expectedPageContent + "')]"))
                    .isDisplayed();
        } catch (NoSuchElementException e) {
            log.error("Page content text verification failed: " + e.getMessage());
            return false;
        }
    }


    public List<WebElement> getElements(List<WebElement> elements) {
        return elements;
    }

    public List<String> getElementsText(List<WebElement> elements) {
        return elements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getElementsAttribute(List<WebElement> elements, String attributeName) {
        return elements.stream()
                .map(el -> el.getDomAttribute(attributeName))
                .collect(Collectors.toList());
    }

    public List<String> getElementsCssValue(List<WebElement> elements, String cssValue) {
        return elements.stream()
                .map(el -> el.getCssValue(cssValue))
                .collect(Collectors.toList());
    }

    public List<String> getArrayListOfStringWithCommaSeparated(String fieldNames) {
        if (fieldNames.contains(",")) {
            return new ArrayList<>(Arrays.asList(fieldNames.split(",")));
        }
        List<String> result = new ArrayList<>();
        result.add(fieldNames);
        return result;
    }


    public void fileUpload(String filePath, WebElement element) {
        String path = filePath == null || filePath.isBlank() ? PathUtil.getTestDataDir() : filePath;
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        element.sendKeys(path);
    }

    public void uploadFile(WebElement element, String filePath) {
        log.info("Uploading file: " + filePath);
        File file = new File(filePath);
        if (file.exists()) {
            element.sendKeys(file.getAbsolutePath());
        } else {
            log.error("File not found: " + filePath);
            throw new RuntimeException("File not found: " + filePath);
        }
    }

    public void uploadTheFile(WebElement element, String filePath) {
        uploadFile(element, filePath);
    }

    public void uploadMultipleFiles(WebElement element, String... filePaths) {
        log.info("Uploading multiple files");
        Arrays.stream(filePaths).forEach(fp -> uploadFile(element, fp));
    }


    public void switchToNextTab() {
        ArrayList<String> tabs = new ArrayList<>(WebDrv.getInstance().getWebDriver().getWindowHandles());
        WebDrv.getInstance().getWebDriver().switchTo().window(tabs.get(1));
    }

    public void switchToPreviousTab() {
        ArrayList<String> tabs = new ArrayList<>(WebDrv.getInstance().getWebDriver().getWindowHandles());
        WebDrv.getInstance().getWebDriver().switchTo().window(tabs.get(0));
    }

    public void switchToParentWindow() {
        WebDrv.getInstance().getWebDriver().switchTo().defaultContent();
    }

    public void switchToNewWindow() {
        String originalWindow = WebDrv.getInstance().getWebDriver().getWindowHandle();
        for (String window : WebDrv.getInstance().getWebDriver().getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                WebDrv.getInstance().getWebDriver().switchTo().window(window);
                log.info("Switched to new window: " + window);
                break;
            }
        }
    }

    public void switchToWindow(String windowHandle) {
        log.info("Switching to window: " + windowHandle);
        WebDrv.getInstance().getWebDriver().switchTo().window(windowHandle);
    }

    public void switchToDefaultContent() {
        log.info("Switching to default content");
        WebDrv.getInstance().getWebDriver().switchTo().defaultContent();
    }

    public String getCurrentWindowHandle() {
        return WebDrv.getInstance().getWebDriver().getWindowHandle();
    }

    public String getWindowHandle() {
        log.info("Getting current window handle");
        return WebDrv.getInstance().getWebDriver().getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        log.info("Getting all window handles");
        return WebDrv.getInstance().getWebDriver().getWindowHandles();
    }


    public void switchToIframe(WebElement iframe) {
        WebDrv.getInstance().getWebDriver().switchTo().frame(iframe);
        log.info("Switched to iframe: " + iframe);
    }

    public void switchToFrame(int index) {
        log.info("Switching to frame with index: " + index);
        WebDrv.getInstance().getWebDriver().switchTo().frame(index);
    }

    public void switchToFrame(String nameOrId) {
        log.info("Switching to frame with name or ID: " + nameOrId);
        WebDrv.getInstance().getWebDriver().switchTo().frame(nameOrId);
    }

    public void switchToFrame(WebElement frameElement) {
        log.info("Switching to frame with element: " + frameElement);
        WebDrv.getInstance().getWebDriver().switchTo().frame(frameElement);
    }


    public void acceptAlert() {
        log.info("Accepting alert");
        WebDrv.getInstance().getWebDriver().switchTo().alert().accept();
    }

    public void dismissAlert() {
        log.info("Dismissing alert");
        WebDrv.getInstance().getWebDriver().switchTo().alert().dismiss();
    }

    public String getAlertText() {
        log.info("Getting alert text");
        return WebDrv.getInstance().getWebDriver().switchTo().alert().getText();
    }

    public void sendKeysToAlert(String text) {
        log.info("Sending keys to alert: " + text);
        WebDrv.getInstance().getWebDriver().switchTo().alert().sendKeys(text);
    }

    public boolean isAlertPresent() {
        log.info("Checking if alert is present");
        try {
            WebDrv.getInstance().getWebDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }


    public List<Cookie> getCookies() {
        log.info("Getting all cookies");
        return new ArrayList<>(WebDrv.getInstance().getWebDriver().manage().getCookies());
    }

    public Cookie getCookieNamed(String name) {
        log.info("Getting cookie named: " + name);
        return WebDrv.getInstance().getWebDriver().manage().getCookieNamed(name);
    }

    public void addCookie(Cookie cookie) {
        log.info("Adding cookie: " + cookie.getName());
        WebDrv.getInstance().getWebDriver().manage().addCookie(cookie);
    }

    public void deleteCookieNamed(String name) {
        log.info("Deleting cookie named: " + name);
        WebDrv.getInstance().getWebDriver().manage().deleteCookieNamed(name);
    }

    public void deleteAllCookies() {
        log.info("Deleting all cookies");
        WebDrv.getInstance().getWebDriver().manage().deleteAllCookies();
    }


    public void takeScreenshot(String fileName) {
        log.info("Taking screenshot: " + fileName);
        try {
            File screenshot = ((TakesScreenshot) WebDrv.getInstance().getWebDriver())
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot,
                    new File(PathUtil.getScreenshotsDirFileName(fileName + ".png")));
        } catch (Exception e) {
            log.error("Error taking screenshot: " + fileName, e);
        }
    }

    public List<LogEntry> getBrowserLogs() {
        log.info("Getting browser logs");
        return WebDrv.getInstance().getWebDriver().manage().logs().get(LogType.BROWSER).getAll();
    }

    public List<LogEntry> getNetworkLogs() {
        log.info("Getting network logs");
        return WebDrv.getInstance().getWebDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
    }


    public CheckoutStepTwoPage clickContinue() {
        checkoutYourInformationPage().clickOnContinueButton();
        return new CheckoutStepTwoPage();
    }

    public CheckoutStepTwoPage clickOnContinueButton() {
        checkoutYourInformationPage().clickOnContinueButton();
        return new CheckoutStepTwoPage();
    }

    public CartPage clickCancelButton() {
        WebDrv.getInstance().getWebDriver()
                .findElement(By.xpath("//a[normalize-space()='" + SauceDemoConstants.BUTTON_LABEL_CANCEL + "']"))
                .click();
        return PageManager.getInstance().getPage(CartPage.class);
    }

    public boolean hasSubHeadingText(String subHeading) {
        WebElement ele = WebDrv.getInstance().getWebDriver().findElement(By.linkText(subHeading));
        return subHeading.equals(ele.getText());
    }


    public boolean areWebElementsInOrderByListOrder(List<String> elementIds) {
        String idOfParentElement = elementIds.get(0);
        WebElement parentWebElement = WebDrv.getInstance().getWebDriver()
                .findElement(By.id(idOfParentElement));
        List<WebElement> childWebElements = parentWebElement.findElements(By.xpath(".//*"));
        WebElementOrderChecker checker = new WebElementOrderCheckerImpl(childWebElements);
        List<String> childIds = IntStream.range(1, elementIds.size())
                .mapToObj(elementIds::get)
                .toList();
        List<String> grandchildIds = IntStream.range(1, childIds.size())
                .mapToObj(childIds::get)
                .toList();
        return checker.areWebElementsOrderedLikeSpecifiedListByElementId(grandchildIds);
    }

    public void assertManageCookiesPage() {
        NavigateToNewTab.navigateToNewTab();

    }
}
