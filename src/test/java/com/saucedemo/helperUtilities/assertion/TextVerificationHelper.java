package com.saucedemo.helperUtilities.assertion;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class TextVerificationHelper {

    public static void verifyText(String expectedText, WebElement element, String errorMessage) {
        String actualText;

        try {
            if (element == null) {
                throw new IllegalArgumentException("WebElement must be provided.");
            }
            // If a WebElement is provided, use it directly.
            actualText = new VerificationHelper().getText(element);

            // Perform the text verification.
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = "Expected text '" + expectedText + "' not found in the provided element.";
            }

            Assert.assertTrue(actualText.contains(expectedText), errorMessage + " Actual text: " + actualText);

        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Overloaded method for verifyText with WebElement without a custom error message.
     *
     * @param expectedText The text to be verified.
     * @param element      The WebElement where the text should be present.
     */
    public static void verifyText(String expectedText, WebElement element) {
        verifyText(expectedText, element, null);
    }
}
