package com.saucedemo.pages;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutStepOnePage extends Page {
    private static final Logger log = LogManager.getLogger(CheckoutStepOnePage.class);
    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By POSTAL_CODE_INPUT = By.id("postal-code");
    private static final By CANCEL_BUTTON = By.cssSelector(".cart_cancel_link, #cancel, [data-test='cancel']");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By ERROR_WARNING_RED_MESSAGE = By.cssSelector("h3[data-test='error']");
    private static final By PAGE_HEADING = By.cssSelector(".title");

    private String enteredFirstName;
    private String enteredLastName;
    private String enteredPostalCode;

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalOrZipCodeInput;

    @FindBy(css = ".cart_cancel_link")
    private WebElement cancelButton;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "h3[data-test='error']")
    private WebElement errorWarningRedMessage;

    public void enterFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("First name must not be null");
        }
        this.enteredFirstName = firstName;
        setInputValue(FIRST_NAME_INPUT, firstName, "First name");
        log.info("Entered first name");
    }

    public void enterLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("Last name must not be null");
        }
        this.enteredLastName = lastName;
        setInputValue(LAST_NAME_INPUT, lastName, "Last name");
        log.info("Entered last name");
    }

    public void enterPostalCode(String postalCode) {
        if (postalCode == null) {
            throw new IllegalArgumentException("Postal code must not be null");
        }
        this.enteredPostalCode = postalCode;
        setInputValue(POSTAL_CODE_INPUT, postalCode, "Postal code");
        log.info("Entered postal code");
    }

    private void setInputValue(By inputLocator, String value, String fieldLabel) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));
        WebElement activeInput = wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(inputLocator)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", activeInput);
        activeInput.click();
        activeInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        activeInput.sendKeys(Keys.DELETE);
        activeInput.clear();
        activeInput.sendKeys(value);

        String currentValue = readInputValue(inputLocator);
        if (currentValue == null || !currentValue.trim().equals(value.trim())) {
            forceInputValue(inputLocator, value);
        }

        wait.until(ExpectedConditions.attributeToBe(inputLocator, "value", value));

        String finalValue = readInputValue(inputLocator);
        if (finalValue == null || !finalValue.trim().equals(value.trim())) {
            throw new RuntimeException(fieldLabel + " was not populated correctly before continuing.");
        }
    }

    private void forceInputValue(By inputLocator, String value) {
        WebElement input = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, inputLocator);
        if (input == null) {
            throw new RuntimeException("Unable to locate input for forced value set: " + inputLocator);
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", input, value);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', {bubbles: true}));", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('blur', {bubbles: true}));", input);
    }

    private String readInputValue(By inputLocator) {
        WebElement input = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, inputLocator);
        return input == null ? null : input.getDomProperty("value");
    }

    private void ensureRequiredFieldValuesPresent() {
        if (enteredFirstName != null && !enteredFirstName.isBlank() && !enteredFirstName.trim().equals(readInputValue(FIRST_NAME_INPUT))) {
            setInputValue(FIRST_NAME_INPUT, enteredFirstName, "First name");
        }
        if (enteredLastName != null && !enteredLastName.isBlank() && !enteredLastName.trim().equals(readInputValue(LAST_NAME_INPUT))) {
            setInputValue(LAST_NAME_INPUT, enteredLastName, "Last name");
        }
        if (enteredPostalCode != null && !enteredPostalCode.isBlank() && !enteredPostalCode.trim().equals(readInputValue(POSTAL_CODE_INPUT))) {
            setInputValue(POSTAL_CODE_INPUT, enteredPostalCode, "Postal code");
        }
    }

    public CartPage clickCancelButton() {
        WebElement cancel = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, CANCEL_BUTTON);
        if (cancel == null) {
            throw new org.openqa.selenium.NoSuchElementException("Cancel button is not clickable after wait: " + CANCEL_BUTTON);
        }
        cancel.click();
        log.info("Clicked on the Cancel button");
        return PageManager.getInstance().getPage(CartPage.class);
    }

    public CheckoutStepTwoPage clickContinueButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));

        ensureRequiredFieldValuesPresent();
        WebElement continueBtn = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, CONTINUE_BUTTON);
        if (continueBtn != null) continueBtn.click();
        log.info("Clicked on the Continue button");

        boolean navigatedToOverview = waitForCheckoutOverviewTransition(wait);
        if (!navigatedToOverview) {
            log.warn("Checkout continue displayed a validation error instead of navigating to the overview page.");
        }
        return PageManager.getInstance().getPage(CheckoutStepTwoPage.class);
    }

    private boolean waitForCheckoutOverviewTransition(WebDriverWait wait) {
        try {
            String outcome = wait.until(driver -> {
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl != null && currentUrl.contains(SauceDemoConstants.CHECKOUT_OVERVIEW_PAGE_PATH)) {
                    return "OVERVIEW";
                }

                if (isValidationErrorVisible(driver)) {
                    return "ERROR";
                }

                String heading = "";
                try {
                    heading = driver.findElement(PAGE_HEADING).getText();
                } catch (Exception ignored) {
                    // Ignore transient heading lookup failures while transition is in-flight.
                }
                if (heading.contains(SauceDemoConstants.HEADING_CHECKOUT_OVERVIEW)) {
                    return "OVERVIEW";
                }

                return isValidationErrorVisible(driver) ? "ERROR" : null;
            });
            return "OVERVIEW".equals(outcome);
        } catch (TimeoutException timeoutException) {
            String currentUrl = driver.getCurrentUrl();
            String errorText = "";
            String firstNameValue = safeReadInputValue(FIRST_NAME_INPUT);
            String lastNameValue = safeReadInputValue(LAST_NAME_INPUT);
            String postalCodeValue = safeReadInputValue(POSTAL_CODE_INPUT);
            try {
                WebElement errElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, ERROR_WARNING_RED_MESSAGE);
                errorText = errElement != null ? errElement.getText() : "";
            } catch (Exception ignored) {
                // Keep diagnostics best-effort.
            }

            throw new RuntimeException(
                    "Continue click did not navigate to checkout overview. Current URL: '" + currentUrl
                            + "'. Validation error: '" + errorText + "'. Field values before submit were firstName='"
                            + firstNameValue + "', lastName='" + lastNameValue + "', postalCode='" + postalCodeValue + "'.", timeoutException);
        }
    }

    private boolean isValidationErrorVisible(WebDriver driver) {
        try {
            WebElement errorElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, ERROR_WARNING_RED_MESSAGE);
            return errorElement != null && errorElement.isDisplayed() && !errorElement.getText().isBlank();
        } catch (Exception ignored) {
            return false;
        }
    }

    private String safeReadInputValue(By inputLocator) {
        try {
            String value = readInputValue(inputLocator);
            return value == null ? "" : value;
        } catch (Exception ignored) {
            return "<unavailable>";
        }
    }

    public CheckoutStepTwoPage clickOnContinueButton() {
        clickContinueButton();
        return null;
    }

    public String getCancelButtonText() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, CANCEL_BUTTON);
        return element == null ? "" : verificationHelper.getText(element);
    }

    public String getContinueButtonText() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, CONTINUE_BUTTON);
        return element == null ? "" : verificationHelper.getText(element);
    }

    @Deprecated
    public String getErrorWarningRedMessageTest() {
        return getErrorWarningRedMessageText();
    }

    public String getErrorWarningRedMessageText() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, ERROR_WARNING_RED_MESSAGE);
        return element == null ? "" : verificationHelper.getText(element);
    }

    public boolean isErrorWarningRedMessageDisplayed() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, ERROR_WARNING_RED_MESSAGE);
        return element != null && verificationHelper.isDisplayed(element);
    }

    public boolean isFirstNameInputDisplayedEnabledAndPresent() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, FIRST_NAME_INPUT);
        return element != null && isDisplayedEnabledAndPresent(element);
    }

    public boolean isLastNameInputDisplayedEnabledAndPresent() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, LAST_NAME_INPUT);
        return element != null && isDisplayedEnabledAndPresent(element);
    }

    public boolean isPostalOrZipCodeInputDisplayedEnabledAndPresent() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, POSTAL_CODE_INPUT);
        return element != null && isDisplayedEnabledAndPresent(element);
    }

    public boolean isCancelButtonDisplayedEnabledAndPresent() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, CANCEL_BUTTON);
        return element != null && isDisplayedEnabledAndPresent(element);
    }

    public boolean isContinueButtonDisplayedEnabledAndPresent() {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, CONTINUE_BUTTON);
        return element != null && isDisplayedEnabledAndPresent(element);
    }

    private boolean isDisplayedEnabledAndPresent(WebElement element) {
        return verificationHelper.isDisplayedAndEnabled(element) && verificationHelper.isElementPresent(element);
    }


}
