package com.saucedemo.helperUtilities;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class CheckBoxUtils {

    public static void assertCheckBoxesAreUnTickedByDefaults(List<WebElement> checkBoxesList) {
        for (int i = 0; i < checkBoxesList.size(); i++) {
            Assert.assertFalse(checkBoxesList.get(i).isSelected());

        }
    }
}
