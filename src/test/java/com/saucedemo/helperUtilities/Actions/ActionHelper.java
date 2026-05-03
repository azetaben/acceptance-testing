package com.saucedemo.helperUtilities.Actions;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class ActionHelper {

    private final Logger log = LogManager.getLogger(ActionHelper.class);
    private final WebDriver driver = WebDrv.getInstance().getWebDriver();
    Actions actions = new Actions(WebDrv.getInstance().getWebDriver());

    public ActionHelper() {
        ExplicitWaitFactory.setDriver(driver);
    }

    public void clickElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("About to click on the element: " + element.toString());
        actions.moveToElement(element).click().build().perform();
        log.info("Clicked on element");

    }

    public void doubleClickElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        actions.moveToElement(element).doubleClick().build().perform();
        log.info("Double-clicked on the element");
    }

    public void contextClickElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        actions.moveToElement(element).contextClick().build().perform();
        log.info("Right-clicked on element: " + element.getText());
    }

    public void hoverOverElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("About to hovered over the element: " + element.toString());
        actions.moveToElement(element).build().perform();
        log.info("Hovered over element" + element.getText());
    }

    public void dragAndDropElement(WebElement sourceElement, WebElement targetElement) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, sourceElement);
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, targetElement);
        actions.dragAndDrop(sourceElement, targetElement).build().perform();
    }

    public void dragAndDropByOffset(WebElement element, int xOffset, int yOffset) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.dragAndDropBy(element, xOffset, yOffset).build().perform();
        log.info("Dragged element by offset: (" + xOffset + ", " + yOffset + ")");
    }

    public void sendKeysToElement(WebElement element, String keys) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).click().sendKeys(keys).build().perform();
        log.info("Sent keys to the input field: " + keys);

    }

    public void sendKeysToElementWithSpecialKeys(WebElement element, Keys keys) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        log.info("Sending special keys to element: " + element.toString());
        actions.moveToElement(element).click().sendKeys(keys).build().perform();
        log.info("Sent keys to the input field" + keys);
    }

    public void clickAndHoldElement(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        actions.moveToElement(element).clickAndHold().build().perform();
        log.info("Clicked and held on element: " + element.getText());
    }

    public void releaseMouse() {
        actions.release().build().perform();
        log.info("Released the mouse button");
    }

    public void moveToElementAndClick(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        log.info("Moving to the element to be clicked: " + element.toString());
        actions.moveToElement(element).click().build().perform();
        log.info("Moved to element and clicked");

    }

    public void moveToElementWithOffsetAndClick(WebElement element, int xOffset, int yOffset) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
        actions.moveToElement(element, xOffset, yOffset).click().build().perform();
        log.info("Moved to element with offset and clicked: " + element.getText());
    }

    public void performComplexAction(WebElement element, String keys) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).click().sendKeys(keys).doubleClick().build().perform();
        log.info("Performed complex action on element: " + element.getText());
    }

    public void scrollToElement(WebElement element) {
        log.info("Scrolling to the element: " + element.toString());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        log.info("Scrolling to the element: " + element.getText());
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        log.info("Scrolled to the top of the page.");
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        log.info("Scrolled to the bottom of the page.");
    }

    public void clearInputField(WebElement element) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        element.clear();
        log.info("Cleared the input field.");
    }

    public void sendKeysToActiveElement(String keys) {
        actions.sendKeys(keys).build().perform();
        log.info("Sent keys to active element: " + keys);
    }

    public void sendSpecialKeysToActiveElement(Keys keys) {
        actions.sendKeys(keys).build().perform();
    }

    public void moveMouseByOffset(int xOffset, int yOffset) {
        actions.moveByOffset(xOffset, yOffset).build().perform();
        log.info("Moved the mouse by offset: (" + xOffset + ", " + yOffset + ")");
    }

    public void clickAtCurrentLocation() {
        actions.click().build().perform();
        log.info("Clicked at current location.");
    }

    public void doubleClickAtCurrentLocation() {
        actions.doubleClick().build().perform();
        log.info("Double-clicked at current location.");
    }

    public void contextClickAtCurrentLocation() {
        actions.contextClick().build().perform();
        log.info("Right-clicked at current location.");
    }

    /**
     * Performs a click and hold at the current mouse location.
     */
    public void clickAndHoldAtCurrentLocation() {
        actions.clickAndHold().build().perform();
        log.info("Clicked and held at current location.");
    }

    public void pauseAction(long duration) {
        actions.pause(Duration.ofMillis(duration)).build().perform();
        log.info("Paused for " + duration + " milliseconds.");
    }

    // --- Multiple Elements Actions ---

    /**
     * Clicks on each WebElement in a list.
     *
     * @param elements The list of WebElements to click.
     */
    public void clickOnEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            actions.moveToElement(element).click().build().perform();

        }
    }

    public void hoverOverEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            hoverOverElement(element);
            log.info("Hovered over element: " + element.getText());
        }
    }

    public void sendKeysToEachElement(List<WebElement> elements, String keys) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            sendKeysToElement(element, keys);
        }
    }

    public void dragAndDropEachElement(List<WebElement> elements, WebElement targetElement) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, targetElement);
            log.info("Dragged element: " + element.getText() + " to target element: " + targetElement.getText());
            dragAndDropElement(element, targetElement);
        }
    }

    public void doubleClickOnEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            log.info("Double-clicked on element: " + element.getText());
            doubleClickElement(element);

        }
    }

    public void contextClickOnEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            log.info("Right-clicked on element: " + element.getText());
            contextClickElement(element);

        }
    }

    public void clickAndHoldOnEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            log.info("Clicked and held on element: " + element.getText());
            clickAndHoldElement(element);

        }
    }

    /**
     * Scrolls to each element in a list.
     *
     * @param elements The list of WebElements to scroll to.
     */
    public void scrollToEachElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Scrolled to element: " + element.getText());
            scrollToElement(element);
        }
    }

    /**
     * Clears the text from each input field in a list.
     *
     * @param elements The list of WebElements representing the input fields.
     */
    public void clearEachInputField(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Cleared the input field.");
            clearInputField(element);
        }
    }

    /**
     * Selects multiple elements by clicking on them sequentially.
     *
     * @param elements The list of WebElements to select.
     */
    public void selectMultipleElements(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            log.info("Clicked on element: " + element.getText());
            clickElement(element);
        }
    }

    public void selectMultipleElementsWithShift(List<WebElement> elements) {
        actions.keyDown(Keys.SHIFT).build().perform();
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            log.info("Clicked on element: " + element.getText());
            clickElement(element);
        }
        actions.keyUp(Keys.SHIFT).build().perform();
    }

    public void selectMultipleElementsWithControl(List<WebElement> elements) {
        actions.keyDown(Keys.CONTROL).build().perform();
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            clickElement(element);
        }
        actions.keyUp(Keys.CONTROL).build().perform();
    }

    public void dragAndDropEachElementByOffset(List<WebElement> elements, int xOffset, int yOffset) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Dragged element: " + element.getText());
            dragAndDropByOffset(element, xOffset, yOffset);
        }
    }

    public void performComplexActionOnEachElement(List<WebElement> elements, String keys) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Performed complex action on element: " + element.getText());
            performComplexAction(element, keys);
        }
    }

    public void moveToEachElementAndClick(List<WebElement> elements) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            log.info("Moved to element and clicked: " + element.getText());
            moveToElementAndClick(element);
        }
    }

    public void moveToEachElementWithOffsetAndClick(List<WebElement> elements, int xOffset, int yOffset) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            log.info("Moved to element with offset and clicked: " + element.getText());
            moveToElementWithOffsetAndClick(element, xOffset, yOffset);
        }
    }

    public void sendKeysToEachElementWithSpecialKeys(List<WebElement> elements, Keys keys) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            log.info("Sent special keys to element: " + element.getText());
            sendKeysToElementWithSpecialKeys(element, keys);
        }
    }

    public void pressAndReleaseKeyOnElement(WebElement element, Keys key) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).sendKeys(key).build().perform();
        log.info("Pressed and released key on element: " + element.getText());
    }

    public void keyDownOnElement(WebElement element, Keys key) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).keyDown(key).build().perform();
    }

    public void keyUpOnElement(WebElement element, Keys key) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).keyUp(key).build().perform();
        log.info("Released key on element: " + element.getText());
    }

    public void pressAndReleaseKeyOnActiveElement(Keys key) {
        actions.sendKeys(key).build().perform();
        log.info("Pressed and released key on active element: " + key);
    }

    public void keyDownOnActiveElement(Keys key) {
        actions.keyDown(key).build().perform();
        log.info("Pressed key on active element: " + key);
    }

    public void keyUpOnActiveElement(Keys key) {
        actions.keyUp(key).build().perform();
        log.info("Released key on active element: " + key);
    }

    public void performComplexActionWithKeys(WebElement element, String keys, Keys keyPress, Keys keyRelease) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).click().sendKeys(keys).keyDown(keyPress).keyUp(keyRelease).doubleClick().build().perform();
        log.info("Performed complex action with keys on element: " + element.getText());

    }

    public void performComplexActionWithKeysOnEachElement(List<WebElement> elements, String keys, Keys keyPress, Keys keyRelease) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            performComplexActionWithKeys(element, keys, keyPress, keyRelease);
            log.info("Performed complex action with keys on element: " + element.getText());


        }
    }

    public void dragAndDropToCoordinates(WebElement element, int x, int y) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
        actions.moveToElement(element).clickAndHold().moveByOffset(x, y).release().build().perform();
        log.info("Dragged element to coordinates: (" + x + ", " + y + ")");
    }

    public void dragAndDropEachElementToCoordinates(List<WebElement> elements, int x, int y) {
        for (WebElement element : elements) {
            dragAndDropToCoordinates(element, x, y);
            log.info("Dragged element to coordinates: (" + x + ", " + y + ")");
        }
    }

    public void hoverAndClickSubElement(WebElement parentElement, WebElement childElement) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, parentElement);
        log.info("Hovered and clicked on sub-element: " + childElement.getText());
        actions.moveToElement(parentElement).moveToElement(childElement).click().build().perform();

    }

    public void hoverAndClickSubElementOnEachElement(List<WebElement> parentElements, WebElement childElement) {
        for (WebElement parentElement : parentElements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, parentElement);
            hoverAndClickSubElement(parentElement, childElement);
            log.info("Hovered and clicked on sub-element: " + childElement.getText());
        }
    }

    public void doubleClickOnEachElementWithOffset(List<WebElement> elements, int xOffset, int yOffset) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            actions.moveToElement(element, xOffset, yOffset).doubleClick().build().perform();
            log.info("Double-clicked on element: " + element.getText());
        }
    }

    public void contextClickOnEachElementWithOffset(List<WebElement> elements, int xOffset, int yOffset) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            actions.moveToElement(element, xOffset, yOffset).contextClick().build().perform();
            log.info("Right-clicked on element: " + element.getText());
        }
    }

    public void clickAndHoldOnEachElementWithOffset(List<WebElement> elements, int xOffset, int yOffset) {
        for (WebElement element : elements) {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            actions.moveToElement(element, xOffset, yOffset).clickAndHold().build().perform();
            log.info("Clicked and held on element: " + element.getText());
        }
    }
}