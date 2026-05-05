package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Locale;
import java.util.Map;

public class CheckoutOneTwoSteps {
    private final PageManager pm;

    public CheckoutOneTwoSteps() {
        this.pm = PageManager.getInstance();
    }

    private CheckoutStepOnePage checkoutYourInformationPage() {
        return this.pm.getPage(CheckoutStepOnePage.class);
    }

    @When("I enter the first name {string}")
    public void iEnterTheFirstName(String firstName) {
        checkoutYourInformationPage().enterFirstName(firstName);
    }

    @When("I enter the last name {string}")
    public void iEnterTheLastName(String lastName) {
        checkoutYourInformationPage().enterLastName(lastName);
    }

    @When("I enter the postal code {string}")
    public void iEnterThePostalCode(String postalCode) {
        checkoutYourInformationPage().enterPostalCode(postalCode);
    }

    @When("I click the continue button")
    public void iClickTheContinueButton() {
        checkoutYourInformationPage().clickContinueButton();
    }

    @And("I fill the checkout information form with:")
    public void iFillTheCheckoutInformationFormWith(DataTable dataTable) {
        Map<String, String> formData = dataTable.asMap(String.class, String.class);

        String firstName = getRequiredFormValue(formData, "First Name");
        String lastName = getRequiredFormValue(formData, "Last Name");
        String postalCode = getRequiredFormValue(formData, "Postal Code", "Zip Code");

        checkoutYourInformationPage().enterFirstName(firstName);
        checkoutYourInformationPage().enterLastName(lastName);
        checkoutYourInformationPage().enterPostalCode(postalCode);
    }

    @When("I click on the {string} button")
    public void iClickOnTheContinueButton(String continueButtonName) {
        if (SauceDemoConstants.BUTTON_LABEL_CONTINUE.equalsIgnoreCase(continueButtonName)) {
            checkoutYourInformationPage().clickContinueButton();
        }
    }

    @And("I tap {string}")
    public void iTap(String buttonText) {
        if (SauceDemoConstants.BUTTON_LABEL_CONTINUE.equalsIgnoreCase(buttonText)) {
            checkoutYourInformationPage().clickContinueButton();
        }
    }

    @When("I click the finish button")
    public void iClickTheFinishButton() {
        pm.getPage(CheckoutStepTwoPage.class).clickFinishButton();
    }

    @When("I tap on the {string} button")
    public void iTapOnTheButton(String finishButtonText) {
        String actualFinishButtonText = pm.getPage(CheckoutStepTwoPage.class).getFinishButtonText();
        Assert.assertTrue(matchesButtonLabelIgnoreCase(actualFinishButtonText, finishButtonText),
                "Finish button text does not match. expected [" + finishButtonText.trim() + "] but found ["
                        + String.valueOf(actualFinishButtonText).trim() + "]");
        pm.getPage(CheckoutStepTwoPage.class).clickFinishButton();
    }

    @When("I enter first name {string}, last name {string}, and zip code {string}")
    public void iEnterFirstNameLastNameAndPostalCode(String firstName, String lastName, String zipCode) {
        checkoutYourInformationPage().enterFirstName(firstName);
        checkoutYourInformationPage().enterLastName(lastName);
        checkoutYourInformationPage().enterPostalCode(zipCode);
    }

    @When("I enter first name {string} last name {string} and zip code {string}")
    public void iEnterFirstNameLastNameAndZipCode(String firstName, String lastName, String zipCode) {
        checkoutYourInformationPage().enterFirstName(firstName);
        checkoutYourInformationPage().enterLastName(lastName);
        checkoutYourInformationPage().enterPostalCode(zipCode);
    }

    private boolean matchesButtonLabelIgnoreCase(String actualText, String expectedText) {
        return actualText != null
                && expectedText != null
                && actualText.trim().equalsIgnoreCase(expectedText.trim());
    }

    private String getRequiredFormValue(Map<String, String> formData, String... keys) {
        for (String key : keys) {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (entry.getKey() != null && entry.getKey().trim().toLowerCase(Locale.ROOT).equals(key.toLowerCase(Locale.ROOT))) {
                    return entry.getValue() == null ? "" : entry.getValue().trim();
                }
            }
        }
        throw new IllegalArgumentException("Missing required checkout form field. Expected one of: " + String.join(", ", keys));
    }

    @When("I click on the finish button")
    public void clickFinishButton() {
        pm.getPage(CheckoutStepTwoPage.class).clickFinishButton();
    }

    @When("I click on the cancel button")
    public void clickCancelButton() {
        pm.getPage(CheckoutStepTwoPage.class).clickCancelButton();
    }

    @When("I click {string} button")
    public void iClickOnTheCancelButton(String cancelButtonText) {
        String actualCancelButtonText = pm.getPage(CheckoutStepTwoPage.class).getCancelButtonText();
        Assert.assertTrue(matchesButtonLabelIgnoreCase(actualCancelButtonText, cancelButtonText), "Cancel button text does not match.");
        pm.getPage(CheckoutStepTwoPage.class).clickCancelButton();
    }

}
