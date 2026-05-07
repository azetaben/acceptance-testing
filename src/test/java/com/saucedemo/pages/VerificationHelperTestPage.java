package com.saucedemo.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class VerificationHelperTestPage extends Page {
    private static final Logger log = LogManager.getLogger(VerificationHelperTestPage.class);


    @FindBy(id = "visible-element")
    private WebElement visibleElement;

    @FindBy(id = "hidden-element")
    private WebElement hiddenElement;

    @FindBy(id = "disabled-element")
    private WebElement disabledElement;

    @FindBy(id = "enabled-element")
    private WebElement enabledElement;


    @FindBy(id = "text-content")
    private WebElement textContentElement;

    @FindBy(id = "empty-element")
    private WebElement emptyElement;


    @FindBy(id = "checkbox-selected")
    private WebElement checkboxSelected;

    @FindBy(id = "checkbox-unselected")
    private WebElement checkboxUnselected;

    @FindBy(id = "radio-selected")
    private WebElement radioSelected;

    @FindBy(id = "radio-unselected")
    private WebElement radioUnselected;


    @FindBy(id = "dropdown-select")
    private WebElement dropdownElement;

    @FindBy(xpath = "//select[@id='dropdown-select']/option")
    private List<WebElement> dropdownOptions;


    @FindBy(xpath = "//ul[@id='item-list']/li")
    private List<WebElement> itemList;

    @FindBy(xpath = "//ul[@id='sorted-asc']/li")
    private List<WebElement> sortedAscendingList;

    @FindBy(xpath = "//ul[@id='sorted-desc']/li")
    private List<WebElement> sortedDescendingList;


    @FindBy(id = "element-with-attr")
    private WebElement elementWithAttribute;


    @FindBy(id = "colored-element")
    private WebElement coloredElement;


    @FindBy(id = "clickable-button")
    private WebElement clickableButton;

    @FindBy(id = "non-clickable-element")
    private WebElement nonClickableElement;


    @FindBy(id = "displayed-enabled-element")
    private WebElement displayedEnabledElement;

    @FindBy(id = "displayed-disabled-element")
    private WebElement displayedDisabledElement;


    @FindBy(xpath = "//div[@class='display-test-items']/div")
    private List<WebElement> displayTestItems;

    @FindBy(xpath = "//div[@class='enabled-test-items']/div[@class='item']")
    private List<WebElement> enabledTestItems;


    public WebElement getVisibleElement() {
        return visibleElement;
    }

    public WebElement getHiddenElement() {
        return hiddenElement;
    }

    public WebElement getDisabledElement() {
        return disabledElement;
    }

    public WebElement getEnabledElement() {
        return enabledElement;
    }

    public WebElement getTextContentElement() {
        return textContentElement;
    }

    public WebElement getEmptyElement() {
        return emptyElement;
    }

    public WebElement getCheckboxSelected() {
        return checkboxSelected;
    }

    public WebElement getCheckboxUnselected() {
        return checkboxUnselected;
    }

    public WebElement getRadioSelected() {
        return radioSelected;
    }

    public WebElement getRadioUnselected() {
        return radioUnselected;
    }

    public WebElement getDropdownElement() {
        return dropdownElement;
    }

    public List<WebElement> getDropdownOptions() {
        return dropdownOptions;
    }

    public List<WebElement> getItemList() {
        return itemList;
    }

    public List<WebElement> getSortedAscendingList() {
        return sortedAscendingList;
    }

    public List<WebElement> getSortedDescendingList() {
        return sortedDescendingList;
    }

    public WebElement getElementWithAttribute() {
        return elementWithAttribute;
    }

    public WebElement getColoredElement() {
        return coloredElement;
    }

    public WebElement getClickableButton() {
        return clickableButton;
    }

    public WebElement getNonClickableElement() {
        return nonClickableElement;
    }

    public WebElement getDisplayedEnabledElement() {
        return displayedEnabledElement;
    }

    public WebElement getDisplayedDisabledElement() {
        return displayedDisabledElement;
    }

    public List<WebElement> getDisplayTestItems() {
        return displayTestItems;
    }

    public List<WebElement> getEnabledTestItems() {
        return enabledTestItems;
    }


    public void navigateToTestPage() {
        log.info("Navigating to VerificationHelper test page");
        load("/test/verification-helper");
    }


    public boolean verifyDisplayState(boolean expectedDisplayed) {
        log.info("Verifying display state. Expected displayed: " + expectedDisplayed);
        boolean actualDisplayed = verificationHelper.isDisplayed(visibleElement);
        return actualDisplayed == expectedDisplayed;
    }


    public boolean verifyEnabledState(boolean expectedEnabled) {
        log.info("Verifying enabled state. Expected enabled: " + expectedEnabled);
        boolean actualEnabled = verificationHelper.isEnabled(enabledElement);
        return actualEnabled == expectedEnabled;
    }


    public String getTestTextContent() {
        log.info("Getting test text content");
        return verificationHelper.getText(textContentElement);
    }


    public boolean verifySelectionState(boolean expectedSelected) {
        log.info("Verifying selection state. Expected selected: " + expectedSelected);
        boolean actualSelected = verificationHelper.isSelected(checkboxSelected);
        return actualSelected == expectedSelected;
    }


    public List<WebElement> getAllTestItems() {
        log.info("Getting all test items from list");
        return getItemList();
    }
}
