package com.saucedemo.steps;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CheckoutCompleteAssertionSteps {

    private final PageManager pm;

    public CheckoutCompleteAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private CheckoutCompletePage checkoutFinishPage() {
        return this.pm.getPage(CheckoutCompletePage.class);
    }

    @Then("I should see the header {string}")
    public void iShouldSeeTheHeader(String expectedHeader) {
        String actualHeader = checkoutFinishPage().getCompleteHeader();
        Assert.assertEquals(actualHeader, expectedHeader, "Header does not match!");
    }

    @Then("I should see the message containing {string}")
    public void iShouldSeeTheMessageContaining(String expectedMessage) {
        String actualText = checkoutFinishPage().getCompleteText();
        Assert.assertTrue(actualText.contains(expectedMessage), "Completion message does not match!");
    }

    @Then("I should see the pony express image")
    public void iShouldSeeThePonyExpressImage() {
        Assert.assertTrue(checkoutFinishPage().isGoodGreenImageDisplayed(), "Pony express image is not displayed!");
    }

    @Then("I verify that the checkout complete page is displayed")
    public void verifyCheckoutCompletePageIsDisplayed() {
        Assert.assertTrue(checkoutFinishPage().isCheckoutCompleteTextDisplayed(), "Checkout complete page is not visible.");
    }

    @Then("I verify the heading text is {string}")
    public void verifyHeadingText(String expectedHeader) {
        Assert.assertEquals(checkoutFinishPage().getHeaderText(), expectedHeader, "Header text does not match.");
    }

    @Then("I verify {string}")
    public void verifyCompleteText(String expectedText) {
        Assert.assertTrue(checkoutFinishPage().getCompleteText().contains(expectedText), "Complete text does not match.");
    }

    @Then("I verify that the good green image is displayed")
    public void verifyPonyImageIsDisplayed() {
        Assert.assertTrue(checkoutFinishPage().isGoodGreenImageDisplayed(), "Pony good green image is not visible.");
    }

    @When("I can on the {string} button")
    public void iCanOnTheButton(String buttonName) {
        Assert.assertTrue(checkoutFinishPage().getBackButtonText().contains(buttonName), "Back home button is not visible.");
    }
}

