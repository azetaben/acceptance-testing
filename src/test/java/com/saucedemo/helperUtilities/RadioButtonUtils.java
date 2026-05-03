package com.saucedemo.helperUtilities;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class RadioButtonUtils {

    public void assertRadioButtonsAreUnTickedByDefaults(List<WebElement> radioButtons) {
        for (int i = 0; i < radioButtons.size(); i++) {
            Assert.assertFalse(radioButtons.get(i).isSelected(), "Radio Button Is Ticked By Default");
        }
    }

    public void assertRadioButtonsAreDisplayed(List<WebElement> radioButtons) {
        for (int i = 0; i < radioButtons.size(); i++) {
            Assert.assertTrue(radioButtons.get(i).isDisplayed(), "Radio Button Is Not Displayed");
        }
    }

    public void selectRadioButtons(List<WebElement> radioButtons, int i) {
        for (i = 0; i < radioButtons.size(); i++) {
            radioButtons.get(i).click();
        }
    }

    public void assertRadioButtonsAreMutuallyExclusive(List<WebElement> radioList) {
        for (int i = 0; i < radioList.size(); i++) {
            WebElement selected = radioList.get(i);
            selected.click();
            for (int j = 0; j < radioList.size(); j++) {
                if (!(radioList.get(j).equals(selected))) {
                    Assert.assertFalse(radioList.get(j).isSelected());
                }
            }
        }

    }
}