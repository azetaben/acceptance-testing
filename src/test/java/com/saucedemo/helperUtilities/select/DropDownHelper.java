package com.saucedemo.helperutilities.select;


import com.saucedemo.helperutilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class DropDownHelper {

    private final Logger log = LoggerHelper.getLogger(DropDownHelper.class);

    public DropDownHelper() {
    }

    public void selectUsingValue(WebElement element, String value) {
        log.info("Selecting by value: " + value + " from element: " + element);
        Select select = new Select(element);
        select.selectByValue(value);
        log.info("Selected by value: " + value + " from element: " + element);
    }

    public void selectUsingIndex(WebElement element, int index) {
        log.info("Selecting by index: " + index + " from element: " + element);
        Select select = new Select(element);
        select.selectByIndex(index);
        log.info("Selected by index: " + index + " from element: " + element);
    }

    public String getSelectedValue(WebElement element) {
        log.info("Getting selected value from element: " + element);
        String value = new Select(element).getFirstSelectedOption().getText();
        log.info("Selected value from element: " + element + " is: " + value);
        return value;
    }

    public void selectUsingVisibleText(WebElement element, String visibleText) {
        log.info("Selecting by visible text: " + visibleText + " from element: " + element);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
        log.info("Selected by visible text: " + visibleText + " from element: " + element);
    }

    public void deSelectUsingValue(WebElement element, String value) {
        log.info("Deselecting by value: " + value + " from element: " + element);
        Select select = new Select(element);
        select.deselectByValue(value);
        log.info("Deselected by value: " + value + " from element: " + element);
    }

    public void deSelectUsingIndex(WebElement element, int index) {
        log.info("Deselecting by index: " + index + " from element: " + element);
        Select select = new Select(element);
        select.deselectByIndex(index);
        log.info("Deselected by index: " + index + " from element: " + element);
    }

    public void deSelectUsingVisibleText(WebElement element, String visibleText) {
        log.info("Deselecting by visible text: " + visibleText + " from element: " + element);
        Select select = new Select(element);
        select.deselectByVisibleText(visibleText);
        log.info("Deselected by visible text: " + visibleText + " from element: " + element);
    }

    public List<String> getAllDropDownData(WebElement element) {
        log.info("Getting all dropdown data from element: " + element);
        Select select = new Select(element);
        List<WebElement> elementList = select.getOptions();
        List<String> valueList = new LinkedList<>();
        for (WebElement ele : elementList) {
            String text = ele.getText();
            log.info("Dropdown data: " + text);
            valueList.add(text);
        }
        log.info("All dropdown data retrieved.");
        return valueList;
    }

    public void selectByValueFromDropDown(WebElement dropDownElement, String value) {
        log.info("Selecting by value: " + value + " from dropdown element: " + dropDownElement);
        Select dropdown = new Select(dropDownElement);
        if (value != null && !value.isEmpty()) {
            dropdown.selectByValue(value.trim());
            log.info("Selected by value: " + value + " from dropdown element: " + dropDownElement);
        }
    }

    public boolean checkDropdownHasAllTheValues(WebElement dropdownElement, List<List<String>> data) {
        log.info("Checking if dropdown has all the values.");
        int counter = 0;
        Select dropdown = new Select(dropdownElement);
        List<WebElement> options = dropdown.getOptions();
        for (WebElement valueInDropdown : options) {
            loop:
            for (int i = 0; i < data.get(0).size(); i++) {
                if (valueInDropdown.getText().equals((data.get(0).get(i).trim()))) {
                    counter++;
                    break loop;
                }
            }
        }

        boolean hasAllValues = counter == data.get(0).size();
        log.info("Does dropdown have all the values? " + hasAllValues);
        return hasAllValues;
    }

    public boolean assertFirstValueInDropDown(WebElement dropdownElement, String value) {
        log.info("Asserting if first value in dropdown is: " + value);
        boolean result;
        Select dropdown = new Select(dropdownElement);
        result = dropdown.getOptions().get(0).getText().equals(value);
        log.info("Is first value in dropdown: " + value + "? " + result);
        return result;
    }

    public void selectFromDropdownByVisibleTextOrValueOrIndex(WebElement element, String visibleTextOrValueOrIndex) {
        selectFromDropdown(s -> s.selectByVisibleText(visibleTextOrValueOrIndex), element);
        selectFromDropdown(s -> s.selectByValue(visibleTextOrValueOrIndex), element);
        selectFromDropdown(s -> s.selectByIndex(Integer.parseInt(visibleTextOrValueOrIndex)), element);
    }

    public void selectFromDropdown(Consumer<Select> consumer, WebElement element) {
        consumer.accept(new Select(element));
    }
}
