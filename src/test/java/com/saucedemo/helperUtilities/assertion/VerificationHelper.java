package com.saucedemo.helperUtilities.assertion;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

public class VerificationHelper {
    private static final Logger log = LogManager.getLogger(VerificationHelper.class);
    private final WebDriver driver = WebDrv.getInstance().getWebDriver();

    public static Map<String, String> HexAndGetCssValue(WebElement element) {
        Map<String, String> cssValues = new HashMap<>();
        cssValues.put("color", element.getCssValue("color"));
        cssValues.put("background-color", element.getCssValue("background-color"));
        return cssValues;
    }

    public static String rgbToHex(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255.");
        }
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public static int[] hexToRgb(String hex) {
        if (hex == null || !hex.matches("^#([0-9A-Fa-f]{6})$")) {
            throw new IllegalArgumentException("Invalid hex color format. Use #RRGGBB.");
        }
        int red = Integer.parseInt(hex.substring(1, 3), 16);
        int green = Integer.parseInt(hex.substring(3, 5), 16);
        int blue = Integer.parseInt(hex.substring(5, 7), 16);
        return new int[]{red, green, blue};
    }

    public boolean isDisplayed(WebElement element) {
        log.info("Checking if element is displayed");
        try {
            boolean displayed = element.isDisplayed();
            if (displayed) {
                log.info("Element is displayed:: " + element.getText());
                return true;
            }
            log.info("Element is present but not displayed.");
            return false;
        } catch (NoSuchElementException e) {
            log.error("Element not found: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unable to determine display state for element: " + element + ". Error: " + e.getMessage());
            return false;
        }
    }

    public boolean areElementsDisplayed(List<WebElement> elements) {
        log.info("Checking if all elements are displayed.");
        if (elements == null || elements.isEmpty()) {
            log.warn("List of WebElements is null or empty.");
            return false;
        }
        boolean allDisplayed = elements.stream().allMatch(this::isDisplayed);
        if (!allDisplayed) {
            log.error("One or more elements are not displayed.");
            return false;
        }
        log.info("All elements are displayed:: " + elements);
        return true;
    }

    public boolean assertItemDisplayedFromList(List<WebElement> listElements, String item) {
        log.info("Asserting if item: " + item + " is displayed in the list.");
        boolean isDisplayed = listElements.stream().anyMatch(s -> s.getText().equalsIgnoreCase(item));
        log.info("Is item: " + item + " displayed? " + isDisplayed);
        return isDisplayed;
    }

    public boolean isSelected(WebElement element) {
        log.info("Checking if element is selected: " + element);
        try {
            boolean selected = element.isSelected();
            log.info("Element is selected:: " + element.getText());
            return selected;
        } catch (NoSuchElementException e) {
            log.error("Element not found:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Element is not Selected");
            return false;
        }
    }

    public boolean verifyNotSelected(WebElement element) {
        log.info("Checking if element is NOT selected: " + element);
        try {
            boolean selected = element.isSelected();
            log.info("Element is selected:: " + element);
            return !selected;
        } catch (NoSuchElementException e) {
            log.error("Element not found:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Element is not present");
            return false;
        }
    }

    public boolean isDisplayedAndEnabled(WebElement element) {
        log.info("Checking if element is displayed and enabled...");
        try {
            boolean displayed = element.isDisplayed();
            boolean enabled = element.isEnabled();
            log.info("Element is displayed and enabled.");
            return displayed && enabled;
        } catch (NoSuchElementException e) {
            log.error("Element not found:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Element is not displayed and enabled:: " + e.getMessage());
            return false;
        }
    }

    public boolean isEnabled(WebElement element) {
        log.info("Checking if element is enabled: " + element);
        try {
            boolean enabled = element.isEnabled();
            log.info("Element is enabled:: " + element.getText());
            return enabled;
        } catch (NoSuchElementException e) {
            log.error("Element not found:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference:: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Element is not enabled :: " + e.getMessage());
            return false;
        }
    }

    public boolean isNotDisplayed(WebElement element) {
        log.info("Checking if element is NOT displayed: " + element);
        if (element == null) {
            log.info("Element reference is null; treating as not displayed.");
            return true;
        }
        try {
            boolean displayed = element.isDisplayed();
            if (displayed) {
                log.info("Element is displayed.");
                return false;
            }
            log.info("Element is present but not displayed.");
            return true;
        } catch (NoSuchElementException e) {
            log.info("Element is not present in DOM; treating as not displayed. Error: " + e.getMessage());
            return true;
        } catch (StaleElementReferenceException e) {
            log.info("Element is stale; treating as not displayed. Error: " + e.getMessage());
            return true;
        } catch (Exception e) {
            log.error("Unable to determine display state for element: " + element + ". Error: " + e.getMessage());
            return false;
        }
    }

    public String readValueFromElement(WebElement element) {
        log.info("Reading value from element: " + element);
        if (element == null) {
            log.info("WebElement is null..");
            return null;
        }
        boolean status = isDisplayed(element);
        if (status) {
            log.info("Element text is :: " + element.getText());
            return element.getText();
        } else {
            return null;
        }
    }

    public String getText(WebElement element) {
        log.info("Getting text from element: " + element.getText());
        boolean status = isDisplayed(element);
        if (status) {
            return element.getText().trim();
        } else {
            return null;
        }
    }

    public String getText(List<WebElement> elements) {
        log.info("Getting text from list of elements.");
        if (elements == null || elements.isEmpty()) {
            log.warn("List of WebElement is null or empty..");
            return null;
        }
        if (!areElementsDisplayed(elements)) {
            return null;
        }
        WebElement element = elements.get(0);
        log.info("Element is displayed:: " + element.getText());
        return element.getText().trim();
    }

    public String getDomAttribute(WebElement element, String attribute) {
        log.info("Getting DOM attribute: " + attribute + " from element: " + element);
        if (element == null) {
            log.info("WebElement is null..");
            return null;
        }
        boolean status = isDisplayed(element);
        if (status) {
            log.info("Element attribute value is displayed:: " + element);
            return Objects.requireNonNull(element.getDomAttribute(attribute));
        } else {
            return null;
        }
    }

    public String getTitle() {
        log.info("Getting current page title.");
        String title = driver.getTitle();
        log.info("Current page title:: " + title);
        return title;
    }

    public String getCurrentPageTitle() {
        log.info("Getting current page title.");
        String title = driver.getTitle();
        log.info("Current page title is :: " + title);
        return title;
    }

    public String getCurrentPageUrl() {
        String url = driver.getCurrentUrl();
        log.info("Current page url:: " + url);
        return url;
    }

    public boolean isElementPresent(By locator) {
        try {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
            log.info("Element is present");
            return true;
        } catch (Exception e) {
            log.error("Element not found " + e);
            return false;
        }
    }

    public synchronized boolean isElementPresent(WebElement element) {
        log.info("Checking if element is present...");
        try {
            boolean displayed = element.isDisplayed();
            log.info("Element is present");
            return displayed;
        } catch (NoSuchElementException e) {
            log.error("Element not found " + e);
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("An error occurred while checking if the element is displayed: " + element + ". Error: " + e.getMessage());
            return false;
        }
    }

    public String getTheText(WebElement element) {
        return element.isDisplayed() ? element.getText() : "";
    }

    public synchronized boolean verifyElementPresent(WebElement element) {
        log.info("Verifying if element is present: " + element);
        try {
            boolean displayed = element.isDisplayed();
            log.info(element.getText() + " is displayed");
            return displayed;
        } catch (NoSuchElementException e) {
            log.error("Element not found " + e);
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("An error occurred while checking if the element is displayed: " + element + ". Error: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean verifyElementNotPresent(WebElement element) {
        log.info("Verifying if element is NOT present: " + element);
        try {
            element.isDisplayed();
            log.info(element.getText() + " is displayed");
            return false;
        } catch (NoSuchElementException e) {
            log.info("Element not found " + e);
            return true;
        } catch (StaleElementReferenceException e) {
            log.warn("Element is stale and is not present:: " + e.getMessage());
            return true;
        } catch (Exception e) {
            log.error("An error occurred while checking if the element is not present: " + e.getMessage());
            return true;
        }
    }

    public synchronized boolean verifyTextEquals(WebElement element, String expectedText) {
        log.info("Verifying if element text equals: " + expectedText);
        try {
            String actualText = element.getText();
            if (actualText.equals(expectedText)) {
                log.info("actualText is :" + actualText + " expected text is: " + expectedText);
                return true;
            } else {
                log.error("actualText is :" + actualText + " expected text is: " + expectedText);
                return false;
            }
        } catch (NoSuchElementException e) {
            log.error("Element not found " + e);
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("actualText is :" + element.getText() + " expected text is: " + expectedText);
            log.info("text not matching" + e);
            return false;
        }
    }

    public boolean verifyElementContainsText(WebElement element, String text, String message) {
        log.info("Verifying if element contains text: " + text);
        try {
            String actualText = element.getText().trim();
            boolean containsText = actualText.contains(text);
            if (containsText) {
                log.info("Element contains text:: '" + text + "'");
            } else {
                log.error("Element does not contain text:: '" + text + "'. Actual text: '" + actualText + "'");
            }
            return containsText;
        } catch (Exception e) {
            log.error(message + e.getMessage());
            return false;
        }
    }

    public boolean verifyElementSelectedFromDropdownList(WebElement dropdownElement, String expectedOptionText) {
        log.info("Verifying if element is selected from dropdown list: " + expectedOptionText);
        try {
            Select select = new Select(dropdownElement);
            WebElement selectedOption = select.getFirstSelectedOption();
            boolean isSelected = selectedOption.getText().trim().equalsIgnoreCase(expectedOptionText);
            if (isSelected) {
                log.info("Option '" + expectedOptionText + "' is selected in the dropdown.");
            } else {
                log.error("Option '" + expectedOptionText + "' is NOT selected. Selected option is '" + selectedOption.getText() + "'.");
            }
            return isSelected;
        } catch (NoSuchElementException e) {
            log.error("Dropdown element not found: " + dropdownElement + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + dropdownElement + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error while verifying if an element is selected in the dropdown: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyListOfElementInAscendingOrder(List<WebElement> elements) {
        log.info("Verifying if list of elements is in ascending order.");
        if (elements == null || elements.isEmpty()) {
            log.warn("List of WebElements is null or empty.");
            return false;
        }
        List<String> elementTexts = elements.stream().map(WebElement::getText).map(String::trim).toList();
        List<String> sortedTexts = elementTexts.stream().sorted().toList();
        boolean isInOrder = elementTexts.equals(sortedTexts);
        if (isInOrder) {
            log.info("List of elements is in ascending order: " + elementTexts);
        } else {
            log.error("List of elements is NOT in ascending order. Original: " + elementTexts + ", Sorted: " + sortedTexts);
        }
        return isInOrder;
    }

    public boolean verifyListOfElementInDescendingOrder(List<WebElement> elements) {
        log.info("Verifying if list of elements is in descending order.");
        if (elements == null || elements.isEmpty()) {
            log.warn("List of WebElements is null or empty.");
            return false;
        }
        List<String> elementTexts = elements.stream().map(WebElement::getText).map(String::trim).toList();
        List<String> sortedTexts = elementTexts.stream().sorted(Collections.reverseOrder()).toList();
        boolean isInOrder = elementTexts.equals(sortedTexts);
        if (isInOrder) {
            log.info("List of elements is in descending order: " + elementTexts);
        } else {
            log.error("List of elements is NOT in descending order. Original: " + elementTexts + ", Sorted: " + sortedTexts);
        }
        return isInOrder;
    }

    public boolean verifyElementIsClickable(WebElement element) {
        log.info("Verifying if element is clickable: " + element);
        try {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            log.info("Element is clickable: " + element);
            return true;
        } catch (NoSuchElementException e) {
            log.error("Element not found: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (StaleElementReferenceException e) {
            log.error("Stale element reference: " + element + ". Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Element is NOT clickable: " + element + ". Error: " + e.getMessage());
            return false;
        }
    }

    public boolean isElementVisibleEnabledAndPresent(WebElement element) {
        try {
            if (element != null) {
                return element.isDisplayed() && element.isEnabled();
            }
        } catch (Exception e) {
            log.error("Element is not present or not interactable: " + e.getMessage());
        }
        return false;
    }
}
