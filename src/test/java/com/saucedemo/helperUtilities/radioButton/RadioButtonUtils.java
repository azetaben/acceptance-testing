package com.saucedemo.helperutilities.radiobutton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class RadioButtonUtils {
    private final Logger log = LogManager.getLogger(RadioButtonUtils.class);

    public RadioButtonUtils() {
        log.info("RadioButtonUtils initialized.");

    }

    public void assertRadioButtonsAreUnTickedByDefaults(List<WebElement> radioButtons) {
        log.info("Asserting radio buttons are unticked by default.");
        for (int i = 0; i < radioButtons.size(); i++) {
            boolean isSelected = radioButtons.get(i).isSelected();
            Assert.assertFalse(isSelected, "Radio button at index " + i + " Is Ticked By Default");
            log.info("Radio button at index " + i + " is unticked by default: " + isSelected);
        }
        log.info("All radio buttons are unticked by default.");
    }

    public void assertRadioButtonsAreDisplayed(List<WebElement> radioButtons) {
        log.info("Asserting radio buttons are displayed.");
        for (int i = 0; i < radioButtons.size(); i++) {
            boolean isDisplayed = radioButtons.get(i).isDisplayed();
            Assert.assertTrue(isDisplayed, "Radio button at index " + i + " Is Not Displayed");
            log.info("Radio button at index " + i + " is displayed: " + isDisplayed);
        }
        log.info("All radio buttons are displayed.");
    }

    public void selectRadioButtons(List<WebElement> radioButtons, int i) {
        log.info("Selecting radio buttons.");
        for (i = 0; i < radioButtons.size(); i++) {
            log.info("Clicking radio button at index: " + i);
            radioButtons.get(i).click();
            log.info("Clicked radio button at index: " + i);
        }
        log.info("All radio buttons selected.");
    }

    public void assertRadioButtonsAreMutuallyExclusive(List<WebElement> radioList) {
        log.info("Asserting radio buttons are mutually exclusive.");
        for (int i = 0; i < radioList.size(); i++) {
            WebElement selected = radioList.get(i);
            log.info("Clicking radio button at index: " + i);
            selected.click();
            log.info("Clicked radio button at index: " + i);
            for (int j = 0; j < radioList.size(); j++) {
                if (!(radioList.get(j).equals(selected))) {
                    boolean isSelected = radioList.get(j).isSelected();
                    Assert.assertFalse(isSelected, "Radio button at index " + j + " is selected when it should not be.");
                    log.info("Radio button at index " + j + " is not selected: " + isSelected);
                }
            }
        }
        log.info("All radio buttons are mutually exclusive.");
    }
}
