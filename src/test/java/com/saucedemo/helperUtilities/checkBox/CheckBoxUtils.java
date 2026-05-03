package com.saucedemo.helperUtilities.checkBox;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class CheckBoxUtils {

    public static void assertCheckBoxesAreUnTickedByDefaults(List<WebElement> checkBoxesList) {
        for (WebElement webElement : checkBoxesList) {
            Assert.assertFalse(webElement.isSelected());
        }
    }
}
