package com.saucedemo.helperUtilities.radioButton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class RadioButtonHelper {
    private final Logger log = LogManager.getLogger(RadioButtonHelper.class);

    public RadioButtonHelper() {
        log.info("RadioButtonHelper initialized.");
    }

    public void assertRadioButtonsAreUnTickedByDefaults(List<WebElement> radioButtons) {
        log.info("Asserting radio buttons are unticked by default.");
        for (WebElement radioButton : radioButtons) {
            boolean isSelected = radioButton.isSelected();
            Assert.assertFalse(isSelected, "Radio button Is Ticked By Default");
            log.info("Radio button: " + radioButton + " is unticked by default: " + isSelected);
        }
        log.info("All radio buttons are unticked by default.");
    }

    public void assertRadioButtonsAreDisplayed(List<WebElement> radioButtons) {
        log.info("Asserting radio buttons are displayed.");
        for (WebElement radioButton : radioButtons) {
            boolean isDisplayed = radioButton.isDisplayed();
            Assert.assertTrue(isDisplayed, "Radio button: " + radioButton + " Is Not Displayed");
            log.info("Radio button: " + radioButton + " is displayed: " + isDisplayed);
        }
        log.info("All radio buttons are displayed.");
    }

    public void selectRadioButtons(List<WebElement> radioButtons, int i) {
        log.info("Selecting radio buttons.");
        for (i = 0; i < radioButtons.size(); i++) {
            WebElement radioButton = radioButtons.get(i);
            log.info("Clicking radio button: " + radioButton);
            radioButton.click();
            log.info("Clicked radio button: " + radioButton);
        }
        log.info("All radio buttons selected.");
    }

    public void assertRadioButtonsAreMutuallyExclusive(List<WebElement> radioList) {
        log.info("Asserting radio buttons are mutually exclusive.");
        for (int i = 0; i < radioList.size(); i++) {
            WebElement selected = radioList.get(i);
            log.info("Clicking radio button: " + selected);
            selected.click();
            log.info("Clicked radio button: " + selected);
            for (WebElement webElement : radioList) {
                if (!(webElement.equals(selected))) {
                    boolean isSelected = webElement.isSelected();
                    Assert.assertFalse(isSelected, "Radio button: " + webElement + " is selected when it should not be.");
                    log.info("Radio button: " + webElement + " is not selected: " + isSelected);
                }
            }
        }
        log.info("All radio buttons are mutually exclusive.");
    }
}
