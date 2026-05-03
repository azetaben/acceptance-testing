package com.saucedemo.helperUtilities.javaScream;

import com.saucedemo.webdriverutilities.WebDrv;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamActions {
    private static final Logger logger = Logger.getLogger(StreamActions.class.getName());
    private WebDriver driver = WebDrv.getInstance().getWebDriver();
    private Actions actions;

    public StreamActions(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public void clickElement(WebElement element) {
        logger.log(Level.INFO, "Clicking on element: {0}", element);
        actions.click(element).perform();
    }

    public void doubleClickElement(WebElement element) {
        logger.log(Level.INFO, "Double clicking on element: {0}", element);
        actions.doubleClick(element).perform();
    }

    public void rightClickElement(WebElement element) {
        logger.log(Level.INFO, "Right clicking on element: {0}", element);
        actions.contextClick(element).perform();
    }

    public void moveToElement(WebElement element) {
        logger.log(Level.INFO, "Moving to element: {0}", element);
        actions.moveToElement(element).perform();
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        logger.log(Level.INFO, "Dragging element: {0} to target: {1}", new Object[]{source, target});
        actions.dragAndDrop(source, target).perform();
    }


    public void sendKeysToElement(WebElement element, String keys) {
        logger.log(Level.INFO, "Sending keys '{0}' to element: {1}", new Object[]{keys, element});
        actions.sendKeys(element, keys).perform();
    }

    public void performActions(List<WebElement> elements) {
        logger.log(Level.INFO, "Performing actions on multiple elements");
        elements.forEach(this::clickElement);
    }


    public void scrollToElement(WebElement element) {
        logger.log(Level.INFO, "Scrolling to element: {0}", element);
        actions.moveToElement(element).perform();
    }

    public void hoverOverTheElement(WebElement element) {
        logger.log(Level.INFO, "Hovering over element: {0}", element);
        actions.moveToElement(element).perform();
    }


    public void clickAndHold(WebElement element) {
        logger.log(Level.INFO, "Clicking and holding element: {0}", element);
        actions.clickAndHold(element).perform();
    }

    public void releaseElement(WebElement element) {
        logger.log(Level.INFO, "Releasing held element: {0}", element);
        actions.release(element).perform();
    }

    public void moveByOffset(int xOffset, int yOffset) {
        logger.log(Level.INFO, "Moving by offset: x={0}, y={1}", new Object[]{xOffset, yOffset});
        actions.moveByOffset(xOffset, yOffset).perform();
    }

    public void performKeyboardAction(WebElement element, String keys) {
        logger.log(Level.INFO, "Performing keyboard action '{0}' on element: {1}", new Object[]{keys, element});
        actions.sendKeys(element, keys).perform();
    }

    public void clickOnMultipleElements(List<WebElement> elements) {
        logger.log(Level.INFO, "Clicking on multiple elements");
        elements.forEach(this::clickElement);
    }

    public String getTextFromElement(WebElement element) {
        String text = element.getText();
        logger.log(Level.INFO, "Getting text from element: {0}, Text: {1}", new Object[]{element, text});
        return text;
    }

    public String getAttributeFromElement(WebElement element, String attribute) {
        String value = element.getDomAttribute(attribute);
        logger.log(Level.INFO, "Getting attribute '{0}' from element: {1}, Value: {2}", new Object[]{attribute, element, value});
        return value;
    }

    public boolean isElementDisplayed(WebElement element) {
        boolean displayed = element.isDisplayed();
        logger.log(Level.INFO, "Checking if element is displayed: {0}, Result: {1}", new Object[]{element, displayed});
        return displayed;
    }

    public boolean isElementEnabled(WebElement element) {
        boolean enabled = element.isEnabled();
        logger.log(Level.INFO, "Checking if element is enabled: {0}, Result: {1}", new Object[]{element, enabled});
        return enabled;
    }

    public boolean isElementSelected(WebElement element) {
        boolean selected = element.isSelected();
        logger.log(Level.INFO, "Checking if element is selected: {0}, Result: {1}", new Object[]{element, selected});
        return selected;
    }

    public int getSizeOfElements(List<WebElement> elements) {
        int size = elements.size();
        logger.log(Level.INFO, "Getting size of elements list, Size: {0}", size);
        return size;
    }

    public List<WebElement> filterElements(List<WebElement> elements, String condition) {
        logger.log(Level.INFO, "Filtering elements based on condition: {0}", condition);
        return elements.stream().filter(e -> e.getText().contains(condition)).collect(Collectors.toList());
    }

    public void clickOnFirstMatchingElement(List<WebElement> elements, String condition) {
        logger.log(Level.INFO, "Clicking on the first matching element with condition: {0}", condition);
        elements.stream().filter(e -> e.getText().contains(condition)).findFirst().ifPresent(this::clickElement);
    }

    public int getIndexOfElement(List<WebElement> elements, WebElement element) {
        int index = IntStream.range(0, elements.size()).filter(i -> elements.get(i).equals(element)).findFirst().orElse(-1);
        logger.log(Level.INFO, "Getting index of element: {0}, Index: {1}", new Object[]{element, index});
        return index;
    }

    public List<String> getTextsFromElements(List<WebElement> elements) {
        logger.log(Level.INFO, "Getting texts from elements");
        return elements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getAttributesFromElements(List<WebElement> elements, String attribute) {
        logger.log(Level.INFO, "Getting attributes '{0}' from elements", attribute);
        return elements.stream().map(e -> e.getDomAttribute(attribute)).collect(Collectors.toList());
    }

    public void scrollToBottom() {
        logger.log(Level.INFO, "Scrolling to the bottom of the page");
        actions.moveToElement(driver.findElement(By.xpath("//html"))).sendKeys(Keys.END).perform();
    }

    public void scrollToTop() {
        logger.log(Level.INFO, "Scrolling to the top of the page");
        actions.moveToElement(driver.findElement(By.xpath("//html"))).sendKeys(Keys.HOME).perform();
    }

    public void waitForElementToBeClickable(WebElement element) {
        logger.log(Level.INFO, "Waiting for element to be clickable: {0}", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeVisible(WebElement element) {
        logger.log(Level.INFO, "Waiting for element to be visible: {0}", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBeSelected(WebElement element) {
        logger.log(Level.INFO, "Waiting for element to be selected: {0}", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeSelected(element));
    }

    public void performActionsOnMultipleElements(List<WebElement> elements) {
        logger.log(Level.INFO, "Performing actions on multiple elements");
        elements.forEach(this::clickElement);
    }

    public String getCssValue(WebElement element, String propertyName) {
        String value = element.getCssValue(propertyName);
        logger.log(Level.INFO, "Getting CSS value '{0}' from element: {1}, Value: {2}", new Object[]{propertyName, element, value});
        return value;
    }

    public Dimension getElementSize(WebElement element) {
        Dimension size = element.getSize();
        logger.log(Level.INFO, "Getting size of element: {0}, Size: {1}", new Object[]{element, size});
        return size;
    }

    public Point getElementLocation(WebElement element) {
        Point location = element.getLocation();
        logger.log(Level.INFO, "Getting location of element: {0}, Location: {1}", new Object[]{element, location});
        return location;
    }

    public String getElementTagName(WebElement element) {
        String tagName = element.getTagName();
        logger.log(Level.INFO, "Getting tag name of element: {0}, Tag Name: {1}", new Object[]{element, tagName});
        return tagName;
    }

    public String getInputValue(WebElement element) {
        String value = element.getDomAttribute("value");
        logger.log(Level.INFO, "Getting input value from element: {0}, Value: {1}", new Object[]{element, value});
        return value;
    }

    public void clearInputValue(WebElement element) {
        logger.log(Level.INFO, "Clearing input value of element: {0}", element);
        element.clear();
    }

    public void sendKeysToInput(WebElement element, String keys) {
        logger.log(Level.INFO, "Sending keys '{0}' to input element: {1}", new Object[]{keys, element});
        element.sendKeys(keys);
    }

    public void hoverOverElement(WebElement element) {
        logger.log(Level.INFO, "Hovering over element: {0}", element);
        actions.moveToElement(element).perform();
    }

    public void clickAndHoldElement(WebElement element) {
        logger.log(Level.INFO, "Clicking and holding element: {0}", element);
        actions.clickAndHold(element).perform();
    }

    public void releaseHeldElement() {
        logger.log(Level.INFO, "Releasing held element");
        actions.release().perform();
    }

    public void moveToElementAndClick(WebElement element) {
        logger.log(Level.INFO, "Moving to element and clicking: {0}", element);
        actions.moveToElement(element).click().perform();
    }

}
