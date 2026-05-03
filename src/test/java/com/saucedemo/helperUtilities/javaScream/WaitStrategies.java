package com.saucedemo.helperUtilities.javaScream;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class WaitStrategies {
    private final WebDriverWait wait;
    private final Logger log = LoggerFactory.getLogger(WaitStrategies.class);
    WebDriver driver;

    public WaitStrategies(WebDriver driver, long timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        ExplicitWaitFactory.setDriver(driver);
    }

    // 1. Wait for an element to be visible
    public WebElement waitForElementToBeVisible(By locator) {
        log.info("Waiting for element to be visible: {}", locator);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
    }

    // 2. Wait for an element to be clickable
    public WebElement waitForElementToBeClickable(By locator) {
        log.info("Waiting for element to be clickable: {}", locator);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
    }

    // 3. Wait for an element to be present in the DOM
    public WebElement waitForElementToBePresent(By locator) {
        log.info("Waiting for element to be present: {}", locator);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, locator);
    }

    // 4. Wait for an alert to be present
    public void waitForAlertToBePresent() {
        log.info("Waiting for alert to be present");
        wait.until(ExpectedConditions.alertIsPresent());
    }

    // 5. Wait for a specific text to be present in an element
    public Boolean waitForTextToBePresentInElement(By locator, String text) {
        log.info("Waiting for text '{}' to be present in element: {}", text, locator);
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // 6. Wait for a specific text to be present in an element attribute
    public Boolean waitForTextToBePresentInElementAttribute(By locator, String attribute, String text) {
        log.info("Waiting for text '{}' to be present in attribute '{}' of element: {}", text, attribute, locator);
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, text));
    }

    // 7. Wait for the title of the page to contain specific text
    public void waitForTitleToContain(String title) {
        log.info("Waiting for title to contain: {}", title);
        wait.until(ExpectedConditions.titleContains(title));
    }

    // 8. Wait for the title of the page to be equal to specific text
    public void waitForTitleToBe(String title) {
        log.info("Waiting for title to be: {}", title);
        wait.until(ExpectedConditions.titleIs(title));
    }

    // 9. Wait for the URL of the page to contain specific text
    public void waitForUrlToContain(String url) {
        log.info("Waiting for URL to contain: {}", url);
        wait.until(ExpectedConditions.urlContains(url));
    }

    // 10. Wait for the URL of the page to be equal to specific text
    public void waitForUrlToBe(String url) {
        log.info("Waiting for URL to be: {}", url);
        wait.until(ExpectedConditions.urlToBe(url));
    }

    // 11. Wait for an element to be selected
    public Boolean waitForElementToBeSelected(By locator) {
        log.info("Waiting for element to be selected: {}", locator);
        return wait.until(ExpectedConditions.elementToBeSelected(locator));
    }

    // 12. Wait for an element to be invisible
    public void waitForElementToBeInvisible(By locator) {
        log.info("Waiting for element to be invisible: {}", locator);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.INVISIBILITY, locator);
    }

    // 13. Wait for a specific condition to be true
    public void waitForCondition(java.util.function.Function<WebDriver, Boolean> condition) {
        log.info("Waiting for a custom condition");
        wait.until(condition);
    }

    // 14. Wait for a specific element to have a specific attribute value
    public Boolean waitForElementAttributeToBe(By locator, String attribute, String value) {
        log.info("Waiting for element's attribute '{}' to be '{}': {}", attribute, value, locator);
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    // 15. Wait for an element to have a specific CSS property value
    public WebElement waitForTheElementCssValue(By locator, String property, String value) {
        log.info("Waiting for element's CSS property '{}' to be '{}': {}", property, value, locator);
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            String cssValue = element.getCssValue(property);
            return cssValue.equals(value) ? element : null;
        });
    }

    // 16. Wait for an element to be enabled
    public WebElement waitForElementToBeEnabled(By locator) {
        log.info("Waiting for element to be enabled: {}", locator);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_ENABLED, locator);
    }

    // 17. Wait for an element to be displayed
    public WebElement waitForElementToBeDisplayed(By locator) {
        log.info("Waiting for element to be displayed: {}", locator);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
    }

    // 18. Wait for a specific number of elements to be present
    public void waitForNumberOfElementsToBePresent(By locator, int number) {
        log.info("Waiting for number of elements to be present: {} (expected: {})", locator, number);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number - 1));
    }

    // 19. Wait for a specific number of elements to be visible
    public void waitForNumberOfElementsToBeVisible(By locator, int number) {
        log.info("Waiting for number of elements to be visible: {} (expected: {})", locator, number);
        wait.until(driver -> {
            return driver.findElements(locator).stream().filter(WebElement::isDisplayed).count() >= number;
        });
    }

    // 20. Wait for a specific element to be clickable
    public WebElement waitForTheElementToBeClickable(WebElement element) {
        log.info("Waiting for element to be clickable: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
    }

    // 21. Wait for a specific element to be visible
    public WebElement waitForTheElementToBeVisible(WebElement element) {
        log.info("Waiting for element to be visible: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    // 22. Wait for a specific element to be present
    public WebElement waitForTheElementToBePresent(WebElement element) {
        log.info("Waiting for element to be present: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    // 23. Wait for a specific element to be invisible
    public void waitForTheElementToBeInvisible(WebElement element) {
        log.info("Waiting for element to be invisible: {}", element);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_INVISIBLE, element);
    }

    // 24. Wait for a specific element to have a specific text
    public Boolean waitForTheElementToHaveText(WebElement element, String text) {
        log.info("Waiting for element to have text '{}' : {}", text, element);
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // 25. Wait for a specific element to have a specific attribute value
    public Boolean waitForTheElementToHaveAttribute(WebElement element, String attribute, String value) {
        log.info("Waiting for element's attribute '{}' to be '{}': {}", attribute, value, element);
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    // 26. Wait for a list of elements to be visible
    public List<WebElement> waitForElementsToBeVisible(List<WebElement> elements) {
        log.info("Waiting for list of elements to be visible");
        return wait.until(driver -> {
            return elements.stream().filter(WebElement::isDisplayed).collect(Collectors.toList());
        });
    }

    // 27. Wait for a list of elements to be present
    public List<WebElement> waitForElementsToBePresent(By locator) {
        log.info("Waiting for list of elements to be present: {}", locator);
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() ? elements : null;
        });
    }

    // 28. Wait for a list of elements to be clickable
    public List<WebElement> waitForElementsToBeClickable(By locator) {
        log.info("Waiting for list of elements to be clickable: {}", locator);
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return elements.stream().filter(e -> e.isEnabled() && e.isDisplayed()).collect(Collectors.toList());
        });
    }

    // 29. Wait for a specific element to have a specific CSS property value
    public WebElement waitForElementCssValue(By locator, String property, String value) {
        log.info("Waiting for element's CSS property '{}' to be '{}': {}", property, value, locator);
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            String cssValue = element.getCssValue(property);
            return cssValue.equals(value) ? element : null;
        });
    }

    // 30. Wait for an element to be enabled
    public WebElement waitForElementToBeEnabled(WebElement element) {
        log.info("Waiting for element to be enabled: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_ENABLED, element);
    }

    // 31. Wait for an element to be displayed
    public WebElement waitForElementToBeDisplayed(WebElement element) {
        log.info("Waiting for element to be displayed: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    // 32. Wait for a specific number of elements to be present
    public void waitForNumberOfElementsToBePresent(WebElement element, int number) {
        log.info("Waiting for number of elements to be present: {} (expected: {})", element, number);
        wait.until(driver -> {
            return driver.findElements(By.xpath("//*")).size() >= number;
        });
    }

    // 33. Wait for a specific number of elements to be visible
    public void waitForNumberOfElementsToBeVisible(WebElement element, int number) {
        log.info("Waiting for number of elements to be visible: {} (expected: {})", element, number);
        wait.until(driver -> {
            return driver.findElements(By.xpath("//*")).stream().filter(WebElement::isDisplayed).count() >= number;
        });
    }

    // 34. Wait for a specific element to be clickable
    public WebElement waitForElementToBeClickable(WebElement element) {
        log.info("Waiting for element to be clickable: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
    }

    // 35. Wait for a specific element to be visible
    public WebElement waitForElementToBeVisible(WebElement element) {
        log.info("Waiting for element to be visible: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    // 36. Wait for a specific element to be present
    public WebElement waitForElementToBePresent(WebElement element) {
        log.info("Waiting for element to be present: {}", element);
        return ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
    }

    // 37. Wait for a specific element to be invisible
    public void waitForElementToBeInvisible(WebElement element) {
        log.info("Waiting for element to be invisible: {}", element);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_INVISIBLE, element);
    }

}