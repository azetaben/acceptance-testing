package com.saucedemo.steps;

import com.saucedemo.configReader.PropertyFileReader;
import com.saucedemo.constants.AppError;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.general.*;
import com.saucedemo.webdriverutilities.WebDrv;
import com.saucedemo.webelementdata.FieldData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Map;

public class LoginGeneralInterfaceSteps {
    private final PageManager pm;
    private final PropertyFileReader propertyFileReader;

    public LoginGeneralInterfaceSteps() {
        this.pm = PageManager.getInstance();
        this.propertyFileReader = new PropertyFileReader();
    }

    private LoginPage loginPage() {
        return this.pm.getPage(LoginPage.class);
    }

    private TypedFieldInputPage typedFieldInputPage() {
        return loginPage();
    }

    private ClickableElementPage clickableElementPage() {
        return loginPage();
    }

    private ApplicationFlowPage applicationFlowPage() {
        return loginPage();
    }

    private MessageBelowHeadingPage messageBelowHeadingPage() {
        return loginPage();
    }

    private UserNamePage userNamePage() {
        return loginPage();
    }

    private TextualContentPage textualContentPage() {
        return loginPage();
    }

    private ButtonPage buttonPage() {
        return loginPage();
    }

    private BlankFieldPage blankFieldPage() {
        return loginPage();
    }

    private String resolveLoginValue(String rawValue) {
        return propertyFileReader.resolveValue(rawValue);
    }

    private String normalizeUrl(String url) {
        if (url == null) {
            return "";
        }

        String trimmed = url.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    @When("I fill login form using general page contract with:")
    public void fillLoginFormUsingGeneralPageContractWith(DataTable dataTable) {
        Map<String, String> loginData = dataTable.asMap(String.class, String.class);
        Map<String, String> resolvedLoginData = loginData.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> resolveLoginValue(entry.getValue()),
                        (left, right) -> right,
                        java.util.LinkedHashMap::new
                ));
        typedFieldInputPage().inputMultiFieldData(resolvedLoginData);
        typedFieldInputPage().assertOnCorrectContentInFields(resolvedLoginData);
    }

    @And("I type {string} in {string} using field data contract")
    public void iTypeInUsingFieldDataContract(String value, String fieldName) {
        String resolvedValue = resolveLoginValue(value);
        typedFieldInputPage().inputFieldValueData(new FieldData(resolvedValue, fieldName));
        typedFieldInputPage().assertOnCorrectFieldData(resolvedValue);
    }

    @When("I perform general login with {string} and {string}")
    public void iPerformGeneralLoginWithAnd(String username, String password) {
        loginPage().login(resolveLoginValue(username), resolveLoginValue(password));
    }

    @Then("I should reach inventory page from general login flow")
    public void iShouldReachInventoryPageFromGeneralLoginFlow() {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Current URL should not be null");
        Assert.assertTrue(currentUrl.contains(SauceDemoConstants.INVENTORY_PAGE_PATH),
                AppError.URL_NOT_FOUND_ERROR + " - Expected inventory page path: " + SauceDemoConstants.INVENTORY_PAGE_PATH +
                        " but URL was: " + currentUrl);

        String pageTitle = WebDrv.getInstance().getWebDriver().getTitle();
        String safePageTitle = pageTitle == null ? "" : pageTitle;
        Assert.assertFalse(safePageTitle.isBlank(), "Page title should not be null or blank");
        Assert.assertTrue(safePageTitle.contains("Swag Labs"),
                AppError.TITLE_NOT_FOUND_ERROR + " - Expected page title to contain 'Swag Labs' but got: " + safePageTitle);
    }

    @Then("I should see general login error message {string}")
    public void iShouldSeeGeneralLoginErrorMessage(String expectedMessage) {
        messageBelowHeadingPage().assertCorrectMessageBelowHeading(resolveLoginValue(expectedMessage));
    }

    @Then("username field should contain {string} in general login page")
    public void usernameFieldShouldContainInGeneralLoginPage(String username) {
        userNamePage().assertCorrectUsername(resolveLoginValue(username));
    }

    @Then("the following textual values should match on login fields")
    public void theFollowingTextualValuesShouldMatchOnLoginFields(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);
        Map<String, String> resolvedExpected = expected.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> resolveLoginValue(entry.getValue()),
                        (left, right) -> right,
                        java.util.LinkedHashMap::new
                ));
        textualContentPage().assertCorrectTextualContentOnFields(resolvedExpected);
    }

    @Then("the general login element {string} should be visible")
    public void theGeneralLoginElementShouldBeVisible(String elementName) {
        Assert.assertTrue(applicationFlowPage().isElementDisplayed(elementName),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Expected login element to be displayed: " + elementName);
    }

    @Then("all login fields should be blank on load")
    public void allLoginFieldsShouldBeBlankOnLoad() {
        blankFieldPage().assertThatAllFieldsAreBlank();
    }

    @Then("the login button should read {string} using button contract")
    public void theLoginButtonShouldReadUsingButtonContract(String expectedButtonTitle) {
        buttonPage().assertCorrectButtonTitle(resolveLoginValue(expectedButtonTitle));
    }

    @Then("the login form should be displayed with all required elements")
    public void theLoginFormShouldBeDisplayedWithAllRequiredElements() {
        Assert.assertTrue(loginPage().isLoginFormDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Login form is not displayed");
        Assert.assertTrue(loginPage().isUsernameFieldDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Username field is not displayed");
        Assert.assertTrue(loginPage().isPasswordInputFieldDisplayed(),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Password field is not displayed");
        Assert.assertTrue(loginPage().isLoginButtonDisplayed(SauceDemoConstants.BUTTON_LABEL_LOGIN),
                AppError.ELEMENT_NOT_FOUND_ERROR + " - Login button is not displayed");
    }

    @Then("I should verify the login page URL and title match expected values")
    public void iShouldVerifyTheLoginPageURLAndTitleMatchExpectedValues() {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        String pageTitle = WebDrv.getInstance().getWebDriver().getTitle();
        String expectedLoginUrl = normalizeUrl(resolveLoginValue("${config:login_url}"));
        String expectedBaseUrl = normalizeUrl(propertyFileReader.getWebsite());
        String normalizedCurrentUrl = normalizeUrl(currentUrl);

        Assert.assertTrue(normalizedCurrentUrl.equals(expectedLoginUrl) || normalizedCurrentUrl.equals(expectedBaseUrl),
                AppError.URL_NOT_FOUND_ERROR + " - Expected login page URL to match configured SauceDemo URL. Expected one of: "
                        + expectedLoginUrl + ", " + expectedBaseUrl + " but got: " + currentUrl);
        Assert.assertEquals(pageTitle, "Swag Labs",
                AppError.TITLE_NOT_FOUND_ERROR + " - Expected page title 'Swag Labs' but got: " + pageTitle);
    }
}




