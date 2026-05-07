package com.saucedemo.pages;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.general.*;
import com.saucedemo.utils.PerformanceUtil;
import com.saucedemo.webelementdata.FieldData;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.*;
import java.util.function.Supplier;

public class LoginPage extends Page implements TypedFieldInputPage, MessageBelowHeadingPage, UserNamePage, TextualContentPage, ButtonPage, BlankFieldPage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private static final int COMMON_PASSWORD_LINE_INDEX = 1;

    @FindBy(tagName = "body")
    private WebElement body;

    @FindBy(css = "#login_button_container")
    private WebElement loginForm;

    @FindBy(css = "#login_credentials")
    private WebElement acceptedUsernamesBlock;

    @FindBy(css = ".login_password")
    private WebElement loginPasswordBlock;

    @FindBy(className = "error-message-container")
    private WebElement errorMessageContainer;

    @FindBy(className = "password-toggle")
    private WebElement passwordVisibilityToggle;

    @FindBy(css = ".bot_column")
    private WebElement botColumnImage;

    @FindBy(id = "user-name")
    private WebElement usernameInputField;

    @FindBy(css = "[placeholder='Username']")
    private WebElement usernameInputFieldPlaceHolder;

    @FindBy(id = "password")
    private WebElement passwordInputField;

    @FindBy(css = "[placeholder='Password']")
    private WebElement passwordPlaceHolder;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "h4")
    private WebElement credentialHeaders;

    @FindBy(xpath = "//div[@id='login_credentials']/h4")
    private WebElement acceptedUsernamesHeader;

    @FindBy(xpath = "//div[@id='login_credentials']")
    private WebElement acceptedUsernamesContainer;

    @FindBy(css = ".login_credentials_wrap-inner")
    private WebElement acceptedUsernamesPasswordForAllUsersWrap;

    @FindBy(xpath = "//div[@class='login_password']/h4")
    private WebElement passwordForAllUsersHeader;

    @FindBy(xpath = "//div[@class='login_password']")
    private WebElement commonPassword;

    @FindBy(xpath = "//*[@id='login_credentials']/text()[1]")
    private WebElement standardUser;

    @FindBy(xpath = "//*[@id='login_credentials']/br[2]")
    private WebElement lockedOutUser;

    @FindBy(css = "h3[data-test='error']")
    private WebElement loginErrorWarningMessage;

    @FindBy(xpath = "//*[@id='login_credentials']/br[3]")
    private WebElement problemUser;

    @FindBy(xpath = "//*[@id='login_credentials']/br[4]")
    private WebElement performanceGlitchUser;


    private Map<String, Supplier<WebElement>> buildElementRegistry() {
        Map<String, Supplier<WebElement>> registry = new HashMap<>();
        registry.put("username", () -> usernameInputField);
        registry.put("user-name", () -> usernameInputField);
        registry.put("password", () -> passwordInputField);
        registry.put("login", () -> loginButton);
        registry.put("login button", () -> loginButton);
        registry.put("login form", () -> loginForm);
        registry.put("error", () -> loginErrorWarningMessage);
        registry.put("error message", () -> loginErrorWarningMessage);
        return registry;
    }

    private WebElement resolveElementByName(String elementName) {
        Objects.requireNonNull(elementName, "Element name must not be null");
        return Optional.of(elementName.trim().toLowerCase(Locale.ROOT))
                .map(buildElementRegistry()::get)
                .map(Supplier::get)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported login element: " + elementName));
    }


    public String getBodyText() {
        return body.getText();
    }

    public void enterUsername(String username) {
        if (username == null) {
            username = "";
        }
        usernameInputField.clear();
        usernameInputField.sendKeys(username);
        log.info("Entered username value: " + username);
    }

    public void enterPassword(String password) {
        if (password == null) {
            password = "";
        }
        passwordInputField.clear();
        passwordInputField.sendKeys(password);
        log.info("Entered password value (masked), length: " + password.length());
    }

    public String getPasswordInputFieldType() {
        return verificationHelper.getDomAttribute(passwordInputField, "type");
    }

    public InventoryPage clickLoginButton() {
        waitAndClick(loginButton);
        log.info("Clicked on login button");
        return new InventoryPage();
    }

    public void inputFieldValueData(FieldData fieldData) {
        Objects.requireNonNull(fieldData, "Field data must not be null");
        WebElement field = resolveElementByName(fieldData.getFieldName());
        field.clear();
        field.sendKeys(Optional.ofNullable(fieldData.getFieldValue()).orElse(""));
    }

    public void inputMultiFieldData(Map<String, String> fieldData) {
        Optional.ofNullable(fieldData)
                .filter(m -> !m.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Field data map must not be null or empty"));
        fieldData.forEach((fieldName, value) -> {
            WebElement field = resolveElementByName(fieldName);
            field.clear();
            field.sendKeys(Optional.ofNullable(value).orElse(""));
        });
    }


    public String getAcceptedUsernames() {
        return verificationHelper.getText(acceptedUsernamesContainer);
    }

    public String getAcceptedUsernamesPasswordForAllUsersWrap() {
        return verificationHelper.getText(acceptedUsernamesPasswordForAllUsersWrap);
    }

    public Boolean isAcceptedUsernamesPasswordForAllUsersWrap() {
        return verificationHelper.isDisplayed(acceptedUsernamesPasswordForAllUsersWrap);
    }


    public String getCommonPassword() {
        return commonPassword.getText().split("\n")[COMMON_PASSWORD_LINE_INDEX];
    }

    public String getStandardUserText() {
        return verificationHelper.getText(standardUser);
    }

    public String getLockedOutUser() {
        return verificationHelper.getText(lockedOutUser);
    }

    public String getProblemUser() {
        return verificationHelper.getText(problemUser);
    }

    public String getPerformanceGlitchUser() {
        return verificationHelper.getText(performanceGlitchUser);
    }

    public boolean isBotColumnImageDisplayed() {
        return verificationHelper.isDisplayed(botColumnImage);
    }

    public boolean isBotColumImageDisplayed() {
        return isBotColumnImageDisplayed();
    }

    public boolean isLoginFormDisplayed() {
        return Optional.of(loginForm)
                .map(verificationHelper::isDisplayed)
                .orElse(false);
    }

    public boolean isAcceptedUsernamesBlockDisplayed() {
        return verificationHelper.isDisplayed(acceptedUsernamesBlock);
    }

    public boolean isLoginPasswordBlockDisplayed() {
        return verificationHelper.isDisplayed(loginPasswordBlock);
    }

    public String getLoginErrorMessageText() {
        return Optional.of(loginErrorWarningMessage)
                .map(verificationHelper::getText)
                .orElse("");
    }

    public String getLoginFormOuterHtml() {
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].outerHTML;", loginForm);
    }

    public String getLoginErrorOuterHtml() {
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].outerHTML;", loginErrorWarningMessage);
    }

    public boolean isLoginErrorMessageDisplayed() {
        return verificationHelper.isDisplayed(loginErrorWarningMessage);
    }

    public void clearUsername() {
        usernameInputField.clear();
        log.info("Cleared username input field");
    }

    public void clearPassword() {
        passwordInputField.clear();
        log.info("Cleared password input field");
    }

    public boolean isPasswordVisible() {
        return Objects.equals(passwordInputField.getDomAttribute("type"), "text");
    }

    public boolean isUsernameFieldDisplayed() {
        return verificationHelper.isDisplayed(usernameInputField);
    }

    public boolean isPasswordInputFieldDisplayed() {
        return verificationHelper.isDisplayed(passwordInputField);
    }


    public boolean isLoginButtonDisplayed(String buttonName) {
        return Optional.ofNullable(buttonName)
                .filter(SauceDemoConstants.BUTTON_LABEL_LOGIN::equalsIgnoreCase)
                .map(ignored -> verificationHelper.isDisplayed(loginButton))
                .orElse(false);
    }

    public String getCssValueBgColor(String bgColor) {
        return loginButton.getCssValue(bgColor);
    }

    public String getCssValueColour(String colour) {
        return loginButton.getCssValue(colour);
    }

    public String getUsernameAttributeValue() {
        return verificationHelper.getDomAttribute(usernameInputFieldPlaceHolder, "placeholder");
    }

    public String getPasswordInputAttributeValue() {
        return verificationHelper.getDomAttribute(passwordPlaceHolder, "placeholder");
    }

    public void tapOnLoginButton() {
        waitAndClick(loginButton);
    }


    public String getLoginButtonText() {
        return Optional.ofNullable(verificationHelper.getText(loginButton))
                .filter(text -> !text.isBlank())
                .orElseGet(() -> verificationHelper.getDomAttribute(loginButton, "value"));
    }

    public String getUsernameInputFieldValue() {
        return verificationHelper.getDomAttribute(usernameInputField, "value");
    }

    public String getPasswordInputFieldValue() {
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].value;", passwordInputField);
    }


    public boolean isCorrectButtonTitle(String expectedButtonTitle) {
        return getLoginButtonText().equals(expectedButtonTitle);
    }

    public boolean areFieldsBlank() {
        return verificationHelper.getDomAttribute(usernameInputField, "value").isEmpty()
                && verificationHelper.getDomAttribute(passwordInputField, "value").isEmpty();
    }

    public boolean isElementDisplayed(String elementName) {
        return verificationHelper.isDisplayed(resolveElementByName(elementName));
    }

    public void click(String elementName) {
        resolveElementByName(elementName).click();
    }

    public boolean areFieldValuesCorrect(Map<String, String> fieldDataFromPage) {
        return fieldDataFromPage.entrySet().stream().allMatch(entry -> {
            String actual = verificationHelper.getDomAttribute(resolveElementByName(entry.getKey()), "value");
            return actual.equals(entry.getValue());
        });
    }

    @Override
    public void assertOnCorrectContentInFields(Map<String, String> fieldDataFromPage) {
        fieldDataFromPage.forEach((fieldName, expectedValue) -> {
            String actual = verificationHelper.getDomAttribute(resolveElementByName(fieldName), "value");
            if (!actual.equals(expectedValue)) {
                throw new RuntimeException(
                        "Field '" + fieldName + "' expected '" + expectedValue + "' but was '" + actual + "'");
            }
        });
    }

    public boolean isFieldDataPresent(String fieldValue) {
        return Arrays.asList(
                verificationHelper.getDomAttribute(usernameInputField, "value"),
                verificationHelper.getDomAttribute(passwordInputField, "value")
        ).contains(fieldValue);
    }

    public boolean isCorrectErrorMessage(String expectedMessage) {
        return verificationHelper.isDisplayed(loginErrorWarningMessage)
                && getLoginErrorMessageText().equals(expectedMessage);
    }

    public boolean isCorrectUsername(String name) {
        return verificationHelper.getDomAttribute(usernameInputField, "value").equals(name);
    }

    public boolean areTextualContentsCorrect(Map<String, String> fieldValues) {
        Set<String> errorFieldNames = Set.of("error", "error message");
        return fieldValues.entrySet().stream().allMatch(entry -> {
            WebElement element = resolveElementByName(entry.getKey());
            String actualText = errorFieldNames.contains(entry.getKey().toLowerCase(Locale.ROOT))
                    ? verificationHelper.getText(element)
                    : verificationHelper.getDomAttribute(element, "value");
            return actualText.equals(entry.getValue());
        });
    }


    @Override
    public void assertCorrectButtonTitle(String expectedButtonTitle) {
        if (!isCorrectButtonTitle(expectedButtonTitle)) {
            throw new RuntimeException("Login button title expected '" + expectedButtonTitle
                    + "' but was '" + getLoginButtonText() + "'");
        }
    }

    @Override
    public void assertThatAllFieldsAreBlank() {
        if (!areFieldsBlank()) {
            throw new RuntimeException("Expected username and password fields to be blank");
        }
    }

    @Override
    public void assertOnCorrectFieldData(String fieldValue) {
        if (!isFieldDataPresent(fieldValue)) {
            throw new RuntimeException("Expected '" + fieldValue + "' not found in login input fields");
        }
    }

    @Override
    public void assertFieldsAreEditable() {
        if (!usernameInputField.isEnabled()) {
            throw new RuntimeException("Username field is not editable");
        }
        if (!passwordInputField.isEnabled()) {
            throw new RuntimeException("Password field is not editable");
        }
    }

    @Override
    public void assertMessageBelowHeading() {
        if (!verificationHelper.isDisplayed(loginErrorWarningMessage)) {
            throw new RuntimeException("Expected error message below heading is not displayed");
        }
    }

    @Override
    public void assertCorrectMessageBelowHeading(String expectedMessage) {
        if (!isCorrectErrorMessage(expectedMessage)) {
            throw new RuntimeException("Login error message expected '" + expectedMessage
                    + "' but was '" + getLoginErrorMessageText() + "'");
        }
    }

    @Override
    public void assertCorrectUsername(String name) {
        if (!isCorrectUsername(name)) {
            throw new RuntimeException("Username field expected '" + name
                    + "' but was '" + verificationHelper.getDomAttribute(usernameInputField, "value") + "'");
        }
    }

    @Override
    public void assertCorrectTextualContentOnFields(Map<String, String> fieldValues) {
        if (!areTextualContentsCorrect(fieldValues)) {
            throw new RuntimeException("One or more field textual contents did not match expected values");
        }
    }


    public WebElement getLoginButtonElement() {
        return loginButton;
    }

    public WebElement getUsernameInputElement() {
        return usernameInputField;
    }

    public WebElement getPasswordInputElement() {
        return passwordInputField;
    }

    public WebElement getLoginFormElement() {
        return loginForm;
    }

    public WebElement getLoginErrorElement() {
        return loginErrorWarningMessage;
    }

    public InventoryPage login(String username, String password) {
        clearUsername();
        enterUsername(username);
        clearPassword();
        enterPassword(password);
        clickLoginButton();
        log.info("Logged in successfully");
        return new InventoryPage();
    }

}
