package com.saucedemo.steps;

import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoginDataTableSteps {

    private final PageManager pm;

    public LoginDataTableSteps() {
        this.pm = PageManager.getInstance();
    }

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    /**
     * 1. List<List<String>>
     *
     * No header row — every row is [username, password].
     * Uses the first data row to log in.
     *
     * Gherkin:
     *   When I login with raw credential rows:
     *     | standard_user | secret_sauce |
     */
    @When("I login with raw credential rows:")
    public void iLoginWithRawCredentialRows(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        // Each inner list: index 0 = username, index 1 = password
        for (List<String> row : rows) {
            String username = row.get(0);
            String password = row.get(1);
            loginPage().login(username, password);
        }
    }

    /**
     * 2. List<Map<String, String>>
     *
     * First row = column headers (username, password, expectedPage …).
     * Every subsequent row becomes a Map keyed by those headers.
     *
     * Gherkin:
     *   When I login with the following credentials list:
     *     | username      | password     | expectedPage |
     *     | standard_user | secret_sauce | Products     |
     */
    @When("I login with the following credentials list:")
    public void iLoginWithTheFollowingCredentialsList(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String username = row.get("username");
            String password = row.get("password");
            loginPage().login(username, password);
        }
    }

    /**
     * 3. Map<String, String>
     *
     * Vertical key-value table: first column = key, second column = value.
     * Keys must be unique — suited for a single credential pair.
     *
     * Gherkin:
     *   When I login with the following credentials map:
     *     | username | standard_user |
     *     | password | secret_sauce  |
     */
    @When("I login with the following credentials map:")
    public void iLoginWithTheFollowingCredentialsMap(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String username = credentials.get("username");
        String password = credentials.get("password");
        loginPage().login(username, password);
    }

    /**
     * 4. Map<String, List<String>>
     *
     * First column = key; columns 1..N = the list of values for that key.
     * Useful for testing multiple usernames that share the same password.
     * Uses the first value of "username" list and first value of "password" list.
     *
     * Gherkin:
     *   When I login with grouped credential values:
     *     | username | standard_user | problem_user | performance_glitch_user |
     *     | password | secret_sauce  |              |                         |
     */
    @When("I login with grouped credential values:")
    public void iLoginWithGroupedCredentialValues(DataTable dataTable) {
        Map<String, List<String>> grouped = buildMapOfLists(dataTable);
        List<String> usernames = grouped.getOrDefault("username", List.of());
        List<String> passwords = grouped.getOrDefault("password", List.of());
        String sharedPassword = passwords.isEmpty() ? "" : passwords.get(0);

        for (int i = 0; i < usernames.size(); i++) {
            String username = usernames.get(i);
            if (username == null || username.isBlank()) continue;
            loginPage().login(username, sharedPassword);
            if (i < usernames.size() - 1) {
                navigateToLoginPage();
            }
        }
    }

    /**
     * 5. Map<String, Map<String, String>>
     *
     * First column = test-case key; remaining columns form a nested Map of
     * field → value for that test case. Iterates every named test case.
     *
     * Gherkin:
     *   When I login with named test case credentials:
     *     | testCase | username                | password     |
     *     | TC_001   | standard_user           | secret_sauce |
     *     | TC_002   | performance_glitch_user | secret_sauce |
     */
    @When("I login with named test case credentials:")
    public void iLoginWithNamedTestCaseCredentials(DataTable dataTable) {
        Map<String, Map<String, String>> testCases = buildMapOfMaps(dataTable);
        List<Map<String, String>> entries = new ArrayList<>(testCases.values());
        for (int i = 0; i < entries.size(); i++) {
            Map<String, String> fields = entries.get(i);
            String username = fields.get("username");
            String password = fields.get("password");
            loginPage().login(username, password);
            if (i < entries.size() - 1) {
                navigateToLoginPage();
            }
        }
    }

    // --- negative step definitions ------------------------------------------

    /**
     * N1. List<List<String>> — raw rows: [username, password, expectedError]
     *
     * Each row is one failed attempt. After each submission the error
     * message is asserted, then the page is refreshed for the next row.
     *
     * Gherkin:
     *   Then I attempt login with invalid raw credential rows and verify errors:
     *     | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
     *     | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
     */
    @Then("I attempt login with invalid raw credential rows and verify errors:")
    public void iAttemptLoginWithInvalidRawCredentialRowsAndVerifyErrors(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            navigateToLoginPage();
            loginPage().enterUsername(row.get(0));
            loginPage().enterPassword(row.get(1));
            loginPage().clickLoginButton();
            assertLoginError(row.get(2));
        }
    }

    /**
     * N2. List<Map<String, String>> — header + data rows; each row has
     *     username, password, and expectedError columns.
     *
     * Gherkin:
     *   Then I attempt login with invalid credentials list and verify errors:
     *     | username     | password       | expectedError                        |
     *     | invalid_user | wrong_password | Epic sadface: Username and password … |
     */
    @Then("I attempt login with invalid credentials list and verify errors:")
    public void iAttemptLoginWithInvalidCredentialsListAndVerifyErrors(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            navigateToLoginPage();
            loginPage().enterUsername(row.getOrDefault("username", ""));
            loginPage().enterPassword(row.getOrDefault("password", ""));
            loginPage().clickLoginButton();
            assertLoginError(row.get("expectedError"));
        }
    }

    /**
     * N3. Map<String, String> — vertical key-value; a single invalid attempt.
     *     Error is asserted by a separate Then step in the feature.
     *
     * Gherkin:
     *   When I attempt login with invalid credentials map:
     *     | username | locked_out_user |
     *     | password | secret_sauce    |
     */
    @When("I attempt login with invalid credentials map:")
    public void iAttemptLoginWithInvalidCredentialsMap(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        loginPage().enterUsername(credentials.getOrDefault("username", ""));
        loginPage().enterPassword(credentials.getOrDefault("password", ""));
        loginPage().clickLoginButton();
    }

    /**
     * N4. Map<String, List<String>> — rows keyed by "username", "password",
     *     "expectedError"; each column (index 1..N) is one test case.
     *
     * Gherkin:
     *   Then I attempt login with invalid grouped credentials and verify errors:
     *     | username      | invalid_user_1 | invalid_user_2 | Standard_User |
     *     | password      | wrong_pass     | wrong_pass     | wrong_pass    |
     *     | expectedError | Epic sadface … | Epic sadface … | Epic sadface … |
     */
    @Then("I attempt login with invalid grouped credentials and verify errors:")
    public void iAttemptLoginWithInvalidGroupedCredentialsAndVerifyErrors(DataTable dataTable) {
        Map<String, List<String>> grouped = buildMapOfLists(dataTable);
        List<String> usernames     = grouped.getOrDefault("username",      List.of());
        List<String> passwords     = grouped.getOrDefault("password",      List.of());
        List<String> expectedErrors = grouped.getOrDefault("expectedError", List.of());

        for (int i = 0; i < usernames.size(); i++) {
            navigateToLoginPage();
            loginPage().enterUsername(i < usernames.size()      ? usernames.get(i)      : "");
            loginPage().enterPassword(i < passwords.size()      ? passwords.get(i)      : "");
            loginPage().clickLoginButton();
            assertLoginError(i < expectedErrors.size() ? expectedErrors.get(i) : "");
        }
    }

    /**
     * N5. Map<String, Map<String, String>> — named test cases; each inner map
     *     contains username, password, and expectedError.
     *
     * Gherkin:
     *   Then I attempt login with invalid named test cases and verify errors:
     *     | testCase | username        | password     | expectedError              |
     *     | TC_N_001 | locked_out_user | secret_sauce | Epic sadface: Sorry …      |
     */
    @Then("I attempt login with invalid named test cases and verify errors:")
    public void iAttemptLoginWithInvalidNamedTestCasesAndVerifyErrors(DataTable dataTable) {
        Map<String, Map<String, String>> testCases = buildMapOfMaps(dataTable);
        for (Map.Entry<String, Map<String, String>> entry : testCases.entrySet()) {
            Map<String, String> fields = entry.getValue();
            String username      = fields.getOrDefault("username",      "");
            String password      = fields.getOrDefault("password",      "");
            String expectedError = fields.getOrDefault("expectedError", "");
            navigateToLoginPage();
            loginPage().enterUsername(username);
            loginPage().enterPassword(password);
            loginPage().clickLoginButton();
            assertLoginError(expectedError);
        }
    }

    private void navigateToLoginPage() {
        WebDrv.getInstance().getWebDriver()
                .navigate().to(GlobalVarsHelper.getInstance().getURL());
    }

    private static final By LOGIN_ERROR = By.cssSelector("h3[data-test='error']");

    private void assertLoginError(String expectedError) {
        WebDriver driver = WebDrv.getInstance().getWebDriver();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    List<WebElement> found = d.findElements(LOGIN_ERROR);
                    return !found.isEmpty() && expectedError.equals(found.get(0).getText());
                });
        List<WebElement> errorElements = driver.findElements(LOGIN_ERROR);
        Assert.assertFalse(errorElements.isEmpty(),
                "Login error element not found. Expected: '" + expectedError + "'");
        Assert.assertEquals(errorElements.get(0).getText(), expectedError,
                "Login error message mismatch");
    }

    // --- helpers -----------------------------------------------------------

    /**
     * Converts a DataTable into Map<String, List<String>>.
     * First cell in each row is the key; remaining cells are the value list.
     */
    private Map<String, List<String>> buildMapOfLists(DataTable dataTable) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (List<String> row : dataTable.asLists(String.class)) {
            if (row.isEmpty()) continue;
            String key = row.get(0);
            List<String> values = new ArrayList<>(row.subList(1, row.size()));
            result.put(key, values);
        }
        return result;
    }

    /**
     * Converts a DataTable into Map<String, Map<String, String>>.
     * First column is the outer key; first row provides inner field names.
     */
    private Map<String, Map<String, String>> buildMapOfMaps(DataTable dataTable) {
        List<List<String>> raw = dataTable.asLists(String.class);
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        if (raw.size() < 2) return result;

        List<String> headers = raw.get(0);           // testCase | username | password …
        String outerKey = headers.get(0);            // "testCase" column name (unused as key)

        for (int r = 1; r < raw.size(); r++) {
            List<String> row = raw.get(r);
            String testCaseId = row.get(0);
            Map<String, String> fields = new LinkedHashMap<>();
            for (int c = 1; c < headers.size(); c++) {
                fields.put(headers.get(c), c < row.size() ? row.get(c) : "");
            }
            result.put(testCaseId, fields);
        }
        return result;
    }
}