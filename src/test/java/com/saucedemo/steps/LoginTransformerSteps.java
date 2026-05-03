package com.saucedemo.steps;

import com.saucedemo.models.LoginData;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.transformers.LoginDataTransformer;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Step definitions that drive login via {@link LoginDataTransformer}.
 *
 * Each DataTable row is converted to a {@link LoginData} record by the
 * transformer, providing typed access to username, password, and expectedError.
 * The outcome step branches on whether expectedError is blank (success path)
 * or non-blank (error path) using only the data returned by the transformer.
 */
public class LoginTransformerSteps {

    private static final Logger log = LogManager.getLogger(LoginTransformerSteps.class);
    private static final By LOGIN_ERROR_LOCATOR = By.cssSelector("h3[data-test='error']");

    private final PageManager pm;
    private final LoginDataTransformer transformer;

    // Holds the last LoginData produced by the transformer so the Then step
    // can read expectedError without accessing the DataTable a second time.
    private LoginData activeLoginData;

    public LoginTransformerSteps() {
        this.pm = PageManager.getInstance();
        this.transformer = new LoginDataTransformer();
    }

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    // =========================================================================
    // When
    // =========================================================================

    /**
     * Reads each DataTable row through {@link LoginDataTransformer}, stores the
     * resulting {@link LoginData}, and submits the credentials via the UI.
     *
     * DataTable columns: username | password | expectedError
     *
     * {@code expectedError} may be blank for scenarios that expect a successful
     * login; the Then step decides the assertion branch from that value.
     */
    @When("I login with transformer mapped credentials:")
    public void iLoginWithTransformerMappedCredentials(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            activeLoginData = transformer.loginDataTransformer(row);

            log.info("LoginDataTransformer produced → username='"
                    + activeLoginData.username()
                    + "' expectedError='"
                    + activeLoginData.expectedError() + "'");

            loginPage().enterUsername(
                    activeLoginData.username() == null ? "" : activeLoginData.username());
            loginPage().enterPassword(
                    activeLoginData.password() == null ? "" : activeLoginData.password());
            loginPage().clickLoginButton();
        }
    }

    // =========================================================================
    // Then
    // =========================================================================

    /**
     * Asserts the login outcome using the {@link LoginData} produced by the
     * transformer in the preceding When step.
     *
     * <ul>
     *   <li>Blank {@code expectedError} → successful login; Products header visible.</li>
     *   <li>Non-blank {@code expectedError} → failed login; error element text matches.</li>
     * </ul>
     */
    @Then("I verify the transformer login outcome")
    public void iVerifyTheTransformerLoginOutcome() {
        Assert.assertNotNull(activeLoginData,
                "No LoginData available — ensure the When step ran before this Then step.");

        String expectedError = activeLoginData.expectedError();

        if (expectedError == null || expectedError.isBlank()) {
            assertSuccessfulLogin();
        } else {
            assertLoginError(expectedError);
        }
    }

    // =========================================================================
    // Private assertion helpers
    // =========================================================================

    private void assertSuccessfulLogin() {
        InventoryPage inventoryPage = pm.getPage(InventoryPage.class);
        String actualHeader = inventoryPage.getHeaderText();
        log.info("Verifying successful login — header='" + actualHeader + "'");
        Assert.assertEquals(actualHeader, "Products",
                "Expected Products page header after successful login."
                        + " LoginDataTransformer username='" + activeLoginData.username() + "'");
    }

    private void assertLoginError(String expectedError) {
        WebDriver driver = WebDrv.getInstance().getWebDriver();

        // Poll until the error element appears AND its text matches the expected value.
        // driver.findElements() never throws — empty list signals element not yet in DOM.
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    List<WebElement> found = d.findElements(LOGIN_ERROR_LOCATOR);
                    return !found.isEmpty() && expectedError.equals(found.get(0).getText());
                });

        List<WebElement> errorElements = driver.findElements(LOGIN_ERROR_LOCATOR);
        Assert.assertFalse(errorElements.isEmpty(),
                "Login error element not found after 10 s."
                        + " LoginDataTransformer username='" + activeLoginData.username()
                        + "', expectedError='" + expectedError + "'");
        Assert.assertEquals(errorElements.get(0).getText(), expectedError,
                "Login error text mismatch."
                        + " LoginDataTransformer username='" + activeLoginData.username() + "'");
        log.info("Verified login error → '" + expectedError + "'");
    }
}
