package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @And("I can see checkout information form controls:")
    public void iCanSeeCheckoutInformationFormControls(DataTable controlsTable) {
        List<Map<String, String>> rows = controlsTable.asMaps(String.class, String.class);
        Assert.assertFalse(rows.isEmpty(), "Controls table must include at least one expected checkout control.");

        for (Map<String, String> row : rows) {
            String control = row.get("control");
            Assert.assertNotNull(control, "Controls table must include a 'control' column.");
            String normalizedControl = control.trim().toLowerCase(Locale.ROOT);

            switch (normalizedControl) {
                case "first name" -> Assert.assertTrue(
                        checkoutYourInformationPage().isFirstNameInputDisplayedEnabledAndPresent(),
                        "Expected First Name field to be displayed, enabled, and present on checkout step one.");
                case "last name" -> Assert.assertTrue(
                        checkoutYourInformationPage().isLastNameInputDisplayedEnabledAndPresent(),
                        "Expected Last Name field to be displayed, enabled, and present on checkout step one.");
                case "postal code", "zip code" -> Assert.assertTrue(
                        checkoutYourInformationPage().isPostalOrZipCodeInputDisplayedEnabledAndPresent(),
                        "Expected Postal Code field to be displayed, enabled, and present on checkout step one.");
                case "continue" -> {
                    Assert.assertTrue(
                            checkoutYourInformationPage().isContinueButtonDisplayedEnabledAndPresent(),
                            "Expected Continue button to be displayed, enabled, and present on checkout step one.");
                    Assert.assertTrue(
                            matchesButtonLabelIgnoreCase(checkoutYourInformationPage().getContinueButtonText(), "Continue"),
                            "Expected Continue button label to be 'Continue'.");
                }
                case "cancel" -> {
                    Assert.assertTrue(
                            checkoutYourInformationPage().isCancelButtonDisplayedEnabledAndPresent(),
                            "Expected Cancel button to be displayed, enabled, and present on checkout step one.");
                    Assert.assertTrue(
                            matchesButtonLabelIgnoreCase(checkoutYourInformationPage().getCancelButtonText(), "Cancel"),
                            "Expected Cancel button label to be 'Cancel'.");
                }
                default ->
                        throw new IllegalArgumentException("Unsupported checkout information control expectation: " + control);
            }
        }
    }

    @And("I can see checkout overview page controls:")
    public void iCanSeeCheckoutOverviewPageControls(DataTable controlsTable) {
        List<Map<String, String>> rows = controlsTable.asMaps(String.class, String.class);
        Assert.assertFalse(rows.isEmpty(), "Controls table must include at least one expected checkout overview control.");

        for (Map<String, String> row : rows) {
            String control = row.get("control");
            Assert.assertNotNull(control, "Controls table must include a 'control' column.");

            String normalizedControl = control.trim().toLowerCase(Locale.ROOT);
            switch (normalizedControl) {
                case "finish" -> {
                    Assert.assertTrue(checkoutOverviewPage().isFinishButtonDisplayed(),
                            "Expected Finish button to be displayed on checkout overview page.");
                    Assert.assertTrue(matchesButtonLabelIgnoreCase(checkoutOverviewPage().getFinishButtonText(), "Finish"),
                            "Expected Finish button label to be 'Finish'.");
                }
                case "cancel" -> {
                    Assert.assertTrue(checkoutOverviewPage().isCancelButtonDisplayed(),
                            "Expected Cancel button to be displayed on checkout overview page.");
                    Assert.assertTrue(matchesButtonLabelIgnoreCase(checkoutOverviewPage().getCancelButtonText(), "Cancel"),
                            "Expected Cancel button label to be 'Cancel'.");
                }
                default ->
                        throw new IllegalArgumentException("Unsupported checkout overview control expectation: " + control);
            }
        }
    }

    @And("I can see the product details in the overview page:")
    public void iCanSeeTheProductDetailsInTheOverviewPage(DataTable productDetailsTable) {
        List<List<String>> rows = productDetailsTable.asLists(String.class);
        Assert.assertFalse(rows.isEmpty(), "Product details table must include at least one row.");

        Map<String, String> expectedDetailsByField = new HashMap<>();
        for (List<String> row : rows) {
            Assert.assertTrue(row.size() >= 2,
                    "Each product details row must contain exactly two columns: field and expected value.");
            String field = row.get(0) == null ? "" : row.get(0).trim();
            String expectedValue = row.get(1) == null ? "" : row.get(1).trim();
            expectedDetailsByField.put(normalizeProductDetailsField(field), expectedValue);
        }

        String expectedName = expectedDetailsByField.get("name");
        Assert.assertNotNull(expectedName, "Expected product details table to include 'Name'.");
        Assert.assertTrue(checkoutOverviewPage().getItemNames().stream().anyMatch(name -> name.trim().equals(expectedName)),
                "Expected product name to be present in checkout overview: " + expectedName);

        String expectedDescription = expectedDetailsByField.get("description");
        if (expectedDescription != null) {
            Assert.assertTrue(checkoutOverviewPage().getItemDescriptionByName(expectedName).trim().contains(expectedDescription),
                    "Product description did not match on checkout overview page.");
        }

        String expectedPrice = expectedDetailsByField.get("price");
        if (expectedPrice != null) {
            Assert.assertEquals(normalizePriceText(checkoutOverviewPage().getItemPriceByName(expectedName)),
                    normalizePriceText(expectedPrice),
                    "Product price did not match on checkout overview page.");
        }
    }

    @And("I can see the following product shipment, payment and price details:")
    public void iCanSeeTheFollowingProductShipmentPaymentAndPriceDetails(DataTable detailsTable) {
        List<List<String>> rows = detailsTable.asLists(String.class);
        Assert.assertFalse(rows.isEmpty(), "Checkout summary details table must include at least one row.");

        for (List<String> row : rows) {
            Assert.assertTrue(row.size() >= 2,
                    "Each checkout summary details row must contain exactly two columns: label and expected value.");

            String label = row.get(0) == null ? "" : row.get(0).trim();
            String expectedValue = row.get(1) == null ? "" : row.get(1).trim();

            switch (normalizeSummaryLabel(label)) {
                case "payment information" -> Assert.assertTrue(
                        containsTextIgnoreCase(checkoutOverviewPage().getSauceCardText(), expectedValue),
                        "Payment information did not match on checkout overview page.");
                case "shipping information" -> Assert.assertTrue(
                        containsTextIgnoreCase(checkoutOverviewPage().getFreePonyExpressText(), expectedValue),
                        "Shipping information did not match on checkout overview page.");
                case "item total" -> Assert.assertEquals(
                        extractCurrencyValue(checkoutOverviewPage().getSubtotalText()),
                        extractCurrencyValue(expectedValue),
                        "Item total did not match on checkout overview page.");
                case "tax" -> Assert.assertEquals(
                        extractCurrencyValue(checkoutOverviewPage().getTaxText()),
                        extractCurrencyValue(expectedValue),
                        "Tax did not match on checkout overview page.");
                case "total" -> Assert.assertEquals(
                        extractCurrencyValue(checkoutOverviewPage().getTotalText()),
                        extractCurrencyValue(expectedValue),
                        "Total did not match on checkout overview page.");
                default -> throw new IllegalArgumentException("Unsupported checkout summary detail label: " + label);
            }
        }
    }

    private String normalizeProductDetailsField(String rawField) {
        String normalized = rawField == null ? "" : rawField.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("price") || normalized.equals("price ($)") || normalized.equals("$")) {
            return "price";
        }
        return normalized;
    }

    private String normalizeSummaryLabel(String rawLabel) {
        return rawLabel == null ? "" : rawLabel.trim().replace(":", "").replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private String normalizePriceText(String rawPrice) {
        return rawPrice == null ? "" : rawPrice.replace("$", "").trim();
    }

    private String extractCurrencyValue(String rawText) {
        if (rawText == null) {
            return "";
        }

        String normalized = rawText.trim().replace(',', '.');
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(-?\\d+(?:\\.\\d{1,2})?)").matcher(normalized);
        String lastMatch = "";
        while (matcher.find()) {
            lastMatch = matcher.group(1);
        }
        return lastMatch;
    }
}
