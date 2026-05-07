package com.saucedemo.steps;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.And;
import org.testng.Assert;

public class CheckoutCompleteSteps {
    private final PageManager pm;

    public CheckoutCompleteSteps() {
        this.pm = PageManager.getInstance();
    }

    private CheckoutCompletePage checkoutCompletePage() {
        return this.pm.getPage(CheckoutCompletePage.class);
    }

    @And("tap on the {string} button")
    public void tapOnTheButton(String buttonText) {
        String actualButtonText = checkoutCompletePage().getBackButtonText();
        Assert.assertTrue(actualButtonText != null && actualButtonText.trim().equalsIgnoreCase(buttonText.trim()),
                "Expected checkout complete button text to be [" + buttonText + "] but found [" + actualButtonText + "]");
        checkoutCompletePage().clickBackHomeButton();
    }
}
