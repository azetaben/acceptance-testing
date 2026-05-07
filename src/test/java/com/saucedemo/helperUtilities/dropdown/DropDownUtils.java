package com.saucedemo.helperutilities.dropdown;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class DropDownUtils {

    public void selectByValueFromDropDown(WebElement dropDownElement, String value) {
        Select dropdown = new Select(dropDownElement);
        if (value != null && !(value.isEmpty())) {
            dropdown.selectByValue(value.trim());
        }
    }

    public boolean checkDropdownHasAllTheValues(WebElement dropdownElement, List<List<String>> data) {
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

        if (counter == data.get(0).size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean assertFirstValueInDropDown(WebElement dropdownElement, String value) {
        boolean result = true;
        Select dropdown = new Select(dropdownElement);
        result = dropdown.getOptions().get(0).getText().equals(value);
        return result;
    }
}
