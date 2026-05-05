package com.saucedemo.helperUtilities.assertion;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class TextVerificationHelper {

    public static void verifyText(String expectedText, WebElement element, String errorMessage) {
        if (element == null) {
            Assert.fail("WebElement must be provided.");
            return;
        }
        String actualText = new VerificationHelper().getText(element);
        String message = (errorMessage == null || errorMessage.isEmpty())
                ? "Expected text '" + expectedText + "' not found in the provided element."
                : errorMessage;
        Assert.assertTrue(actualText.contains(expectedText), message + " Actual text: " + actualText);
    }

    public static void verifyText(String expectedText, WebElement element) {
        verifyText(expectedText, element, null);
    }
}
