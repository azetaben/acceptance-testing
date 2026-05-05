package com.saucedemo.steps;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.PageManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @And("I can see checkout complete page controls:")
    public void iCanSeeCheckoutCompletePageControls(DataTable controlsTable) {
        List<Map<String, String>> rows = controlsTable.asMaps(String.class, String.class);
        Assert.assertFalse(rows.isEmpty(), "Controls table must include at least one expected checkout complete control.");

        for (Map<String, String> row : rows) {
            String control = row.get("control");
            Assert.assertNotNull(control, "Controls table must include a 'control' column.");
            String normalizedControl = control.trim().toLowerCase(Locale.ROOT);

            switch (normalizedControl) {
                case "back home" -> {
                    Assert.assertTrue(checkoutFinishPage().isBackHomeControlDisplayed(),
                            "Expected Back Home control to be displayed on checkout complete page.");
                    Assert.assertTrue(checkoutFinishPage().getBackButtonText().trim().equalsIgnoreCase("Back Home"),
                            "Expected Back Home button label to be 'Back Home'.");
                }
                case "twitter", "facebook", "linkedin" -> Assert.assertTrue(
                        checkoutFinishPage().hasSocialControl(control),
                        "Expected social control to be visible on checkout complete page: " + control);
                default -> throw new IllegalArgumentException("Unsupported checkout complete control expectation: " + control);
            }
        }
    }

    @And("I confirm {string} message")
    public void iCanSeeTheCheckoutCompleteMessageDisplayed(String messagePart) {
        Assert.assertTrue(checkoutFinishPage().containsCheckoutCompleteMessage(messagePart),
                "Expected checkout complete message to contain: " + messagePart);
    }

    @And("I can see Pony Express image with src {string}")
    public void iCanSeePonyExpressImageWithSrc(String expectedSrc) {
        String actualSrc = checkoutFinishPage().getPonyExpressImageSrc();

        if (actualSrc != null && actualSrc.trim().toLowerCase(Locale.ROOT).startsWith("data:image/")) {
            Assert.assertTrue(checkoutFinishPage().isGoodGreenImageDisplayed(),
                    "Pony Express image should be displayed when inline image src is used.");
            Assert.assertTrue(actualSrc.toLowerCase(Locale.ROOT).contains("base64,"),
                    "Expected inline Pony Express image src to be base64-encoded.");
            return;
        }

        Assert.assertEquals(normalizeImageSrcForComparison(actualSrc), normalizeImageSrcForComparison(expectedSrc),
                "Pony Express image src mismatch. expected [" + expectedSrc + "] but found [" + actualSrc + "]");
    }

    @And("I can see the success goodbye messages:")
    public void iCanSeeTheSuccessGoodbyeMessages(DataTable dataTable) {
        List<String> expectedMessages = dataTable.asList(String.class);
        Assert.assertFalse(expectedMessages.isEmpty(), "Success goodbye messages table must include at least one row.");

        String actualMessages = normalizeWhitespace(checkoutFinishPage().getSuccessGoodbyeMessagesText()).toLowerCase(Locale.ROOT);
        for (String expectedMessage : expectedMessages) {
            String normalizedExpected = normalizeWhitespace(expectedMessage).toLowerCase(Locale.ROOT);
            if (normalizedExpected.isBlank()) {
                continue;
            }
            Assert.assertTrue(actualMessages.contains(normalizedExpected),
                    "Expected checkout complete content to include message: " + expectedMessage);
        }
    }

    private String normalizeWhitespace(String value) {
        return value == null ? "" : value.replaceAll("\\s+", " ").trim();
    }

    private String normalizeImageSrcForComparison(String rawSrc) {
        if (rawSrc == null || rawSrc.isBlank()) {
            return "";
        }

        String trimmed = rawSrc.trim();
        String pathOnly = trimmed;
        try {
            URI uri = URI.create(trimmed);
            if (uri.getScheme() != null && uri.getPath() != null && !uri.getPath().isBlank()) {
                pathOnly = uri.getPath();
            }
        } catch (Exception ignored) {
            // Keep raw value when src is not a valid URI.
        }

        String normalized = pathOnly.replace('\\', '/').trim().toLowerCase(Locale.ROOT);
        return normalized.replaceFirst("(?i)(\\.[a-f0-9]{6,})(\\.[a-z0-9]+)$", "$2");
    }
}

