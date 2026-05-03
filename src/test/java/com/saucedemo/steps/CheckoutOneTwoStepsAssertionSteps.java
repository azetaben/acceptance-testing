package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CheckoutOneTwoStepsAssertionSteps {
    private final PageManager pm;

    public CheckoutOneTwoStepsAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private CheckoutStepOnePage checkoutYourInformationPage() {
        return this.pm.getPage(CheckoutStepOnePage.class);
    }

    private CheckoutStepTwoPage checkoutOverviewPage() {
        return this.pm.getPage(CheckoutStepTwoPage.class);
    }

    private boolean matchesButtonLabelIgnoreCase(String actualText, String expectedText) {
        return actualText != null
                && expectedText != null
                && actualText.trim().equalsIgnoreCase(expectedText.trim());
    }

    private boolean containsTextIgnoreCase(String actualText, String expectedText) {
        if (actualText == null || expectedText == null) {
            return false;
        }

        String normalizedActual = actualText.trim().replaceAll("\\s+", " ").toLowerCase();
        String normalizedExpected = expectedText.trim().replaceAll("\\s+", " ").toLowerCase();
        return normalizedActual.contains(normalizedExpected);
    }

    @Then("I should be taken to the next step in the checkout process")
    public void iShouldBeTakenToTheNextStepInTheCheckoutProcess() {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        assert currentUrl != null;
        Assert.assertTrue(currentUrl.contains(SauceDemoConstants.CHECKOUT_OVERVIEW_PAGE_PATH),
                "User is not taken to the next step in the checkout process.");
    }

    @When("I check the quantity of {string}")
    public void iCheckTheQuantityOf(String productName) {
        String actualQuantity = checkoutOverviewPage().getItemQuantityByName(productName);
        Assert.assertEquals(actualQuantity, SauceDemoConstants.DEFAULT_PRODUCT_QUANTITY, "Quantity does not match!");
    }

    @When("I check the price of {string}")
    public void iCheckThePriceOf(String productName) {
        String actualPrice = checkoutOverviewPage().getItemPriceByName(productName);
        Assert.assertEquals(actualPrice, SauceDemoConstants.DEFAULT_PRODUCT_PRICE, "Price does not match!");
    }

    @When("I check the total amount")
    public void iCheckTheTotalAmount() {
        String actualTotal = checkoutOverviewPage().getTotalAmount();
        Assert.assertEquals(actualTotal, SauceDemoConstants.DEFAULT_ORDER_TOTAL, "Total amount does not match!");
    }

    @Then("I verify that all elements are present and visible")
    public void isElementVisibleEnabledAndPresent() {
        Assert.assertTrue(checkoutYourInformationPage().isFirstNameInputDisplayedEnabledAndPresent(), "First name input is not visible or enabled");
        Assert.assertTrue(checkoutYourInformationPage().isLastNameInputDisplayedEnabledAndPresent(), "Last name input is not visible or enabled");
        Assert.assertTrue(checkoutYourInformationPage().isPostalOrZipCodeInputDisplayedEnabledAndPresent(), "Postal code input is not visible or enabled");
        Assert.assertTrue(checkoutYourInformationPage().isCancelButtonDisplayedEnabledAndPresent(), "Cancel button is not visible or enabled");
        Assert.assertTrue(checkoutYourInformationPage().isContinueButtonDisplayedEnabledAndPresent(), "Continue button is not visible or enabled");
    }

    @And("I should see first name field, last name field, zip code field and {string} button are present, displayed and enabled")
    public void iShouldSeeFirstNameAndLastArePresentDisplayedAndEnabled(String cancelButtonText) {
        Assert.assertTrue(checkoutYourInformationPage().isFirstNameInputDisplayedEnabledAndPresent(), "First name input is not displayed, present or enabled.");
        Assert.assertTrue(checkoutYourInformationPage().isLastNameInputDisplayedEnabledAndPresent(), "Last name input is not displayed, present or enabled.");
        Assert.assertTrue(checkoutYourInformationPage().isPostalOrZipCodeInputDisplayedEnabledAndPresent(), "Postal code input is not displayed, present or enabled.");
        Assert.assertTrue(checkoutYourInformationPage().isCancelButtonDisplayedEnabledAndPresent(), "Cancel button is not displayed, present or enabled.");
        Assert.assertTrue(checkoutYourInformationPage().isContinueButtonDisplayedEnabledAndPresent(), "Continue button is not displayed, present or enabled.");
        String actualCancelButtonText = checkoutYourInformationPage().getCancelButtonText();
        Assert.assertTrue(matchesButtonLabelIgnoreCase(actualCancelButtonText, cancelButtonText),
                "Cancel button text does not match. expected [" + cancelButtonText.trim() + "] but found [" + String.valueOf(actualCancelButtonText).trim() + "]");
    }

    @Then("I should see an {string} displayed")
    public void iShouldSeeAnErrorWaringMessageDisplayed(String errorMessage) {
        Assert.assertTrue(checkoutYourInformationPage().isErrorWarningRedMessageDisplayed(), "Error message is not displayed.");
        Assert.assertTrue(checkoutYourInformationPage().getErrorWarningRedMessageText().contains(errorMessage), "Error message does not match!");
    }

    @Then("I verify that the Checkout Overview heading is displayed")
    public void verifyCheckoutSummaryIsDisplayed() {
        Assert.assertTrue(checkoutOverviewPage().isCheckoutSummaryVisibleDisplayed(), "Checkout summary is not visible.");
    }

    @Then("I verify the item names, quantities, and prices")
    public void verifyItemDetails() {
        Assert.assertTrue(checkoutOverviewPage().areItemNamesDisplayed(), "Item names are not displayed");
        Assert.assertTrue(checkoutOverviewPage().areItemQuantitiesDisplayed(), "Item quantities are not displayed");
        Assert.assertTrue(checkoutOverviewPage().areItemPricesDisplayed(), "Item prices are not displayed");
    }

    @Then("I verify the subtotal, tax, and total")
    public void verifyTotals() {
        Assert.assertTrue(checkoutOverviewPage().getSubtotalText().contains(SauceDemoConstants.CURRENCY_SYMBOL), "Subtotal does not match.");
        Assert.assertTrue(checkoutOverviewPage().getTaxText().contains(SauceDemoConstants.CURRENCY_SYMBOL), "Tax does not match.");
        Assert.assertTrue(checkoutOverviewPage().getTotalText().contains(SauceDemoConstants.CURRENCY_SYMBOL), "Total does not match.");
    }

    @Then("I verify the Payment Information as {string}")
    public void iVerifyThePaymentInformationAs(String paymentInformation) {
        String actualPaymentInfo = checkoutOverviewPage().getSauceCardText();
        Assert.assertTrue(containsTextIgnoreCase(actualPaymentInfo, paymentInformation),
                "Payment information does not match. expected to contain [" + paymentInformation + "] but found [" + actualPaymentInfo + "]");
    }

    @Then("I verify the Shipping Information as {string}")
    public void iVerifyTheShippingInformationAs(String shippingInformation) {
        String actualShippingInfo = checkoutOverviewPage().getFreePonyExpressText();
        Assert.assertTrue(containsTextIgnoreCase(actualShippingInfo, shippingInformation),
                "Shipping information does not match. expected to contain [" + shippingInformation + "] but found [" + actualShippingInfo + "]");
    }

    @And("I can see the {string} button")
    public void iCanSeeTheCancelButton(String cancelButtonText) {
        String actualCancelButtonText = checkoutOverviewPage().getCancelButtonText();
        Assert.assertTrue(matchesButtonLabelIgnoreCase(actualCancelButtonText, cancelButtonText), "Cancel button does not match.");
    }
}


