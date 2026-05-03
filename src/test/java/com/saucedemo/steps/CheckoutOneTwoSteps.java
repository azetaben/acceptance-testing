package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.When;
import org.testng.Assert;

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

    @When("I click on the {string} button")
    public void iClickOnTheContinueButton(String continueButtonName) {
        if (SauceDemoConstants.BUTTON_LABEL_CONTINUE.equalsIgnoreCase(continueButtonName)) {
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
