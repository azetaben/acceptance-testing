package com.saucedemo.steps;

import com.saucedemo.constants.AppError;
import com.saucedemo.models.ExternalLoginDataRow;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.Locale;

public class LoginAssertionSteps {
    private static final Logger log = LogManager.getLogger(LoginAssertionSteps.class);
    private final PageManager pm;
    private final LoginPage loginPage;

    public LoginAssertionSteps() {
        this.pm = PageManager.getInstance();
        this.loginPage = this.pm.getPage(LoginPage.class);
    }

    private static String normalizeWhitespace(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\s+", " ").trim();
    }

    private static String canonicalizeLabel(String value) {
        String normalized = normalizeWhitespace(value).toLowerCase(Locale.ROOT);
        normalized = normalized.replace(":", "");
        normalized = normalized.replaceAll("\\bis\\b", "");
        return normalizeWhitespace(normalized);
    }

    private LoginPage loginPage() {
        return this.loginPage;
    }

    private String currentUrl() {
        return WebDrv.getInstance().getWebDriver().getCurrentUrl();
    }

    private void assertLoginErrorMessage(String expectedMessage) {
        Assert.assertTrue(loginPage().isLoginErrorMessageDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Login error message element not found");
        Assert.assertEquals(loginPage().getLoginErrorMessageText(), expectedMessage,
                "Expected error message: '" + expectedMessage + "' but got: '" + loginPage().getLoginErrorMessageText() + "'");
    }

    @Then("the login outcome should match the external data")
    public void theLoginOutcomeShouldMatchTheExternalData() {
        ExternalLoginDataRow activeExternalRow = LoginSteps.getActiveExternalRow();
        Assert.assertNotNull(activeExternalRow,
                "No active external login data row. Did you execute the external-data login step?");

        if ("SUCCESS".equalsIgnoreCase(activeExternalRow.expectedResult())) {
            InventoryPage resolvedInventoryPage = PageManager.getInstance().getPage(InventoryPage.class);
            Assert.assertEquals(resolvedInventoryPage.getHeaderText(), "Products",
                    "Unexpected products header after successful login");
            Assert.assertTrue(resolvedInventoryPage.getProductCount() > 0,
                    "Expected products to be visible after successful login");
            return;
        }

        Assert.assertTrue(loginPage().isLoginErrorMessageDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Expected login failure but no error message was shown");
        if (!activeExternalRow.expectedMessage().isBlank()) {
            assertLoginErrorMessage(activeExternalRow.expectedMessage());
        }
    }

    @And("I should see the login form displayed")
    @And("the login form is displayed")
    @And("I should see the login form")
    public void loginFormIsDisplayed() {
        Assert.assertTrue(loginPage().isLoginFormDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Login form is not displayed");
    }

    @And("I should see the login form with the following input fields and buttons")
    public void iShouldSeeTheLoginFormWithTheFollowingInputFieldsAndButtons(DataTable dataTable) {

        List<String> items = dataTable.asList();
        Assert.assertFalse(items.isEmpty(), "Expected at least one login form item in the data table");

        for (String raw : items) {
            String item = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
            if (item.isBlank()) {
                continue;
            }

            if ("username".equals(item)) {
                Assert.assertTrue(loginPage().isUsernameFieldDisplayed(),
                        AppError.ELEMENT_NOT_FOUND_ERROR + " - Username field is not displayed.");
                continue;
            }
            if ("password".equals(item)) {
                Assert.assertTrue(loginPage().isPasswordInputFieldDisplayed(),
                        AppError.ELEMENT_NOT_FOUND_ERROR + " - Password field is not displayed.");
                continue;
            }


            String normalizedButton = item.replaceAll("\\s+", "");
            if ("login".equals(normalizedButton) || "logIn".equalsIgnoreCase(normalizedButton)) {
                Assert.assertTrue(loginPage().isLoginButtonDisplayed("Login") || loginPage().isLoginButtonDisplayed("Log In"),
                        AppError.ELEMENT_NOT_FOUND_ERROR + " - Login button is not displayed.");
                continue;
            }

            throw new IllegalArgumentException("Unsupported login form item: " + raw);
        }
    }

    @Then("the password field should have the type {string}")
    public void thePasswordFieldShouldHaveTheType(String expectedType) {
        String actualType = loginPage().getPasswordInputFieldType();
        Assert.assertEquals(actualType, expectedType, "Password field type mismatch");
    }

    @Then("any typed character in the password field should be masked")
    public void anyTypedCharacterInThePasswordFieldShouldBeMasked() {

        Assert.assertFalse(loginPage().isPasswordVisible(), "Password field should remain masked/hidden");
    }

    @And("Accepted usernames and Password for all users are displayed")
    @And("login usernames and password are displayed")
    public void loginUsernamesAndPasswordAreDisplayed() {


        Assert.assertTrue(loginPage().getAcceptedUsernamesPasswordForAllUsersWrap() != null && !loginPage().getAcceptedUsernamesPasswordForAllUsersWrap().isBlank(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Accepted usernames text is not found or empty");
    }

    @And("I can see Accepted usernames are:")
    public void iCanSeeAcceptedUsernamesAre(DataTable dataTable) {
        List<String> expectedUsernames = dataTable.asList();
        Assert.assertFalse(expectedUsernames.isEmpty(), "Expected at least one username in the data table.");

        String acceptedUsernames = loginPage().getAcceptedUsernames();
        for (String expectedUsername : expectedUsernames) {
            String username = expectedUsername == null ? "" : expectedUsername.trim();
            if (username.isBlank()) {
                continue;
            }
            Assert.assertTrue(acceptedUsernames.contains(username),
                    "Expected accepted usernames to contain '" + username + "' but got: '" + acceptedUsernames + "'");
        }
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessageForEmptyFields(String expectedMessage) {
        String actualMessage = loginPage().getLoginErrorMessageText();
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match!");
    }

    @And("I can see Password for all users:")
    public void iCanSeePasswordForAllUsers(DataTable dataTable) {
        Assert.assertEquals(loginPage().getCommonPassword(), dataTable.cell(0, 0));
    }

    @Then("{string} should be displayed")
    public void isDisplayed(String loginErrorMessage) {
        assertLoginErrorMessage(loginErrorMessage);
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageIsisDisplayed(String loginErrorMessage) {
        assertLoginErrorMessage(loginErrorMessage);
    }

    @Then("I should see {string} is displayed")
    public void iShouldSeeIsDisplayed(String confirmationMessage) {
        InventoryPage inventoryPage = PageManager.getInstance().getPage(InventoryPage.class);
        Assert.assertEquals(confirmationMessage, inventoryPage.getHeaderText(), "Header is not matching");
    }

    @Then("I should see an error message {string} displayed")
    public void i_should_see_an_error_message_displayed(String errorMsg) {
        assertLoginErrorMessage(errorMsg);
    }

    @Then("I should see the username input field")
    public void iShouldSeeTheUsernameInputField() {
        Assert.assertTrue(loginPage().isUsernameFieldDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Username field is not displayed.");
    }

    @Then("I should see the {string} button")
    public void iShouldSeeTheButton(String buttonName) {
        Assert.assertTrue(loginPage().isLoginButtonDisplayed(buttonName),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - " + buttonName + " button is not displayed.");
    }

    @Then("I should see the password input field")
    public void iShouldSeeThePasswordInputField() {
        Assert.assertTrue(loginPage().isPasswordInputFieldDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Password field is not displayed.");
    }

    @And("I can see login form input field attribute value as:")
    @And("I can see the login form has username and password fields")
    public void iCanSeeTheLoginFormHasUsernameAndPasswordFields(DataTable dataTable) {
        List<String> expectedFields = dataTable.asList().stream()
                .map(field -> field == null ? "" : field.trim().toLowerCase(Locale.ROOT))
                .toList();

        Assert.assertEquals(expectedFields.size(), 2,
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Expected exactly two login fields in the data table.");
        Assert.assertTrue(expectedFields.contains("username"),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Expected the login form fields to include 'username'.");
        Assert.assertTrue(expectedFields.contains("password"),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Expected the login form fields to include 'password'.");

        iShouldSeeTheUsernameInputField();
        iShouldSeeThePasswordInputField();
    }

    @Then("the password should be hidden")
    public void thePasswordShouldBeHidden() {
        Assert.assertFalse(loginPage().isPasswordVisible(), "Password is still visible.");
    }

    @And("I tap {string} button")
    public void iTapLogBtn(String loginButton) {
        Assert.assertTrue(loginPage().isLoginButtonDisplayed(loginButton));
        loginPage().clickLoginButton();
    }

    @And("I see {string} button is present and visible")
    public void iSeeButtonIsPresentAndVisible(String loginButton) {
        Assert.assertTrue(loginPage().isLoginButtonDisplayed(loginButton));
    }

    @Then("I should get an Error Message {string}")
    public void i_should_get_an_Error_Message(String errorMessage) {
        loginPage().isLoginErrorMessageDisplayed();
        Assert.assertTrue(loginPage().getLoginErrorMessageText().contains(errorMessage));
    }

    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        Assert.assertTrue(loginPage().isLoginFormDisplayed(), "Login form is not displayed after logout");
    }

    @Then("the current url should contain {string}")
    public void theCurrentUrlShouldContain(String expectedUrlPart) {
        Assert.assertTrue(currentUrl().contains(expectedUrlPart),
                "Expected current URL to contain '" + expectedUrlPart + "' but was '" + currentUrl() + "'.");
    }

    @And("the current url should not contain {string}")
    public void theCurrentUrlShouldNotContain(String unexpectedUrlPart) {
        Assert.assertFalse(currentUrl().contains(unexpectedUrlPart),
                "Expected current URL not to contain '" + unexpectedUrlPart + "' but was '" + currentUrl() + "'.");
    }

    @Then("the login form should contain:")
    public void theLoginFormOuterHtmlShouldContain(DataTable dataTable) {
        String outerHtml = loginPage().getLoginFormOuterHtml();
        Assert.assertNotNull(outerHtml, "Login form was null.");
        List<String> expectedFragments = dataTable.asList();
        for (String expectedFragment : expectedFragments) {
            Assert.assertTrue(outerHtml.contains(expectedFragment),
                    "Expected login form to contain '" + expectedFragment + "'.");
        }
    }

    @Then("the login form outerHtml should not contain:")
    public void theLoginFormOuterHtmlShouldNotContain(DataTable dataTable) {
        String outerHtml = loginPage().getLoginFormOuterHtml();
        Assert.assertNotNull(outerHtml, "Login form outerHtml was null.");
        List<String> unexpectedFragments = dataTable.asList();
        for (String unexpectedFragment : unexpectedFragments) {
            Assert.assertFalse(outerHtml.contains(unexpectedFragment),
                    "Expected login form outerHtml not to contain '" + unexpectedFragment + "'.");
        }
    }

    @Then("the login error outerHtml should contain {string}")
    public void theLoginErrorOuterHtmlShouldContain(String expectedFragment) {
        Assert.assertTrue(loginPage().isLoginErrorMessageDisplayed(),
                "Expected login error message to be displayed before checking outerHtml.");
        String errorOuterHtml = loginPage().getLoginErrorOuterHtml();
        Assert.assertNotNull(errorOuterHtml, "Login error outerHtml was null.");
        Assert.assertTrue(errorOuterHtml.contains(expectedFragment),
                "Expected login error outerHtml to contain '" + expectedFragment + "'.");
    }

    @Then("the password field DOM value should be blank")
    public void thePasswordFieldDomValueShouldBeBlank() {
        Assert.assertEquals(loginPage().getPasswordInputFieldValue(), "",
                "Expected password DOM value to be blank after failed submission.");
        Assert.assertFalse(loginPage().isPasswordVisible(), "Password field should remain masked.");
    }

    @Then("I should see {string}")
    public void i_should_see(String confirmationMessage) {

        if (confirmationMessage.toLowerCase().contains("epic sadface") || confirmationMessage.toLowerCase().contains("error")) {
            assertLoginErrorMessage(confirmationMessage);
        } else {

            InventoryPage inventoryPage = PageManager.getInstance().getPage(InventoryPage.class);
            Assert.assertEquals(confirmationMessage, inventoryPage.getHeaderText(),
                    "Expected confirmation message: '" + confirmationMessage + "' but got: '" + inventoryPage.getHeaderText() + "'");
        }
    }

    @And("Should see {string} and {string} information:")
    public void iCan(String acceptedUsernames, String commonPassword, DataTable dataTable) {
        List<String> expectedItems = dataTable.asList();
        Assert.assertFalse(expectedItems.isEmpty(), "Expected at least one value in the data table.");

        String actualInfo = loginPage().getAcceptedUsernamesPasswordForAllUsersWrap();
        Assert.assertTrue(actualInfo != null && !actualInfo.isBlank(),
                "Accepted usernames/password section is missing or empty.");

        String normalizedActualInfo = normalizeWhitespace(actualInfo);
        String normalizedAcceptedLabel = canonicalizeLabel(acceptedUsernames);
        String normalizedPasswordLabel = canonicalizeLabel(commonPassword);
        String canonicalizedActualInfo = canonicalizeLabel(normalizedActualInfo);

        Assert.assertTrue(canonicalizedActualInfo.contains(normalizedAcceptedLabel),
                "Section label not found: '" + acceptedUsernames + "'. Actual: " + actualInfo);
        Assert.assertTrue(canonicalizedActualInfo.contains(normalizedPasswordLabel),
                "Section label not found: '" + commonPassword + "'. Actual: " + actualInfo);

        for (String expectedItem : expectedItems) {
            String normalizedExpectedItem = normalizeWhitespace(expectedItem);
            Assert.assertTrue(normalizedActualInfo.contains(normalizedExpectedItem),
                    "Expected value '" + expectedItem + "' not found. Actual: " + actualInfo);
            log.info("Verified value present in combined credentials section: " + expectedItem);
        }
    }

    @And("I can see {string}:")
    public void iCanSee(String label, DataTable dataTable) {
        String expectedPassword = dataTable.cell(0, 0);
        String actualPassword = loginPage().getCommonPassword();
        Assert.assertEquals(actualPassword, expectedPassword,
                "Expected password '" + expectedPassword + "' but found: '" + actualPassword + "'");
    }

    @And("I can see {string} button displayed")
    public void iCanSeeButtonDisplayed(String buttonName) {
        Assert.assertTrue(loginPage().isLoginButtonDisplayed(buttonName),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - " + buttonName + " button is not displayed.");
    }
}
