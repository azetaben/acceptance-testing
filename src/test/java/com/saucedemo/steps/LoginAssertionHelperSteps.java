package com.saucedemo.steps;

import com.saucedemo.helperUtilities.assertion.AssertionHelper;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;

/**
 * Step definitions that demonstrate AssertionHelper as the sole assertion
 * mechanism — no direct TestNG Assert calls in this class.
 * AssertionHelper methods covered:
 *   verifyTrue(boolean)            — assert a condition holds
 *   verifyFalse(boolean)           — assert a condition does not hold
 *   verifyText(String, String)     — assert two strings are equal
 *   verifyNotNull(Object)          — assert a value was resolved
 *   updateTestStatus(boolean)      — pass/fail based on a runtime boolean
 *   verifyElementPresent(element)  — assert an element is displayed
 *   verifyElementNotPresent(elem)  — assert an element is absent from the DOM
 *   verifyTextEquals(elem, String) — assert element text equals expected
 */
public class LoginAssertionHelperSteps {

    private final PageManager pm = PageManager.getInstance();

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    // ── Precondition assertions ───────────────────────────────────────────────

    @Then("the login page elements are visible")
    public void theLoginPageElementsAreVisible() {
        LoginPage page = loginPage();
        page.waitForLoad();
        AssertionHelper.verifyTrue(page.isLoginFormDisplayed());
        AssertionHelper.verifyTrue(page.isUsernameFieldDisplayed());
        AssertionHelper.verifyTrue(page.isPasswordInputFieldDisplayed());
        AssertionHelper.verifyTrue(page.isLogoIsDisplayed());
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I log in with username {string} and password {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        loginPage().enterUsername(username);
        loginPage().enterPassword(password);
        loginPage().tapOnLoginButton();
    }

    @When("I enter the password {string}")
    public void iEnterThePassword(String password) {
        loginPage().enterPassword(password);
    }

    // ── Post-login assertions ─────────────────────────────────────────────────

    @Then("Page heading should be {string}")
    public void pageHeadingShouldBe(String expectedHeading) {
        InventoryPage inventoryPage = pm.getPage(InventoryPage.class);
        String actualHeading = inventoryPage.getThisPageSubHeaderText();
        AssertionHelper.verifyNotNull(actualHeading);
        AssertionHelper.verifyText(actualHeading, expectedHeading);
    }

    @And("the current page url should contain {string}")
    public void theCurrentUrlShouldContain(String urlFragment) {
        String currentUrl = loginPage().getCurrentUrl();
        AssertionHelper.verifyNotNull(currentUrl);
        AssertionHelper.updateTestStatus(currentUrl.contains(urlFragment));
    }

    // ── Error message assertions ──────────────────────────────────────────────

    @Then("Login error message should be {string}")
    public void theLoginErrorMessageShouldBe(String expectedMessage) {
        AssertionHelper.verifyTrue(loginPage().isLoginErrorMessageDisplayed());
        AssertionHelper.verifyText(loginPage().getLoginErrorMessageText(), expectedMessage);
    }

    @And("I should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        AssertionHelper.verifyTrue(loginPage().isLoginFormDisplayed());
        AssertionHelper.verifyFalse(loginPage().getCurrentUrl().contains("inventory.html"));
    }

    // ── UI element assertions ─────────────────────────────────────────────────

    @Then("Login button text should be {string}")
    public void theLoginButtonTextShouldBe(String expectedLabel) {
        String actualLabel = loginPage().getLoginButtonText();
        AssertionHelper.verifyNotNull(actualLabel);
        AssertionHelper.verifyText(actualLabel, expectedLabel);
    }

    @Then("Password field type should be {string}")
    public void thePasswordFieldTypeShouldBe(String expectedType) {
        String actualType = loginPage().getPasswordInputFieldType();
        AssertionHelper.verifyNotNull(actualType);
        AssertionHelper.verifyText(actualType, expectedType);
    }

    // ── verifyElementPresent (TC_LAH_010) ─────────────────────────────────────

    @Then("the login button element should be present")
    public void theLoginButtonElementShouldBePresent() {
        WebElement button = loginPage().getLoginButtonElement();
        AssertionHelper.verifyTrue(AssertionHelper.verifyElementPresent(button));
    }

    @And("the username field element should be present")
    public void theUsernameFieldElementShouldBePresent() {
        WebElement usernameField = loginPage().getUsernameInputElement();
        AssertionHelper.verifyTrue(AssertionHelper.verifyElementPresent(usernameField));
    }

    @And("the password field element should be present")
    public void thePasswordFieldElementShouldBePresent() {
        WebElement passwordField = loginPage().getPasswordInputElement();
        AssertionHelper.verifyTrue(AssertionHelper.verifyElementPresent(passwordField));
    }

    // ── verifyElementNotPresent (TC_LAH_011) ──────────────────────────────────

    @Then("the login form element should not be present")
    public void theLoginFormElementShouldNotBePresent() {
        // After navigation the PageFactory proxy throws NoSuchElementException on
        // isDisplayed(), which verifyElementNotPresent catches and returns true.
        WebElement loginForm = loginPage().getLoginFormElement();
        AssertionHelper.updateTestStatus(AssertionHelper.verifyElementNotPresent(loginForm));
    }

    // ── verifyTextEquals (TC_LAH_012 / TC_LAH_013) ───────────────────────────

    @Then("the login button element text should equal {string}")
    public void theLoginButtonElementTextShouldEqual(String expectedText) {
        // Login button is <input type="submit"> — getText() returns ""; use
        // getLoginButtonText() which falls back to the value attribute.
        String actualText = loginPage().getLoginButtonText();
        AssertionHelper.verifyNotNull(actualText);
        AssertionHelper.verifyText(actualText, expectedText);
    }

    @Then("the error message element text should equal {string}")
    public void theErrorMessageElementTextShouldEqual(String expectedText) {
        WebElement errorMessage = loginPage().getLoginErrorElement();
        AssertionHelper.updateTestStatus(AssertionHelper.verifyTextEquals(errorMessage, expectedText));
    }
}
