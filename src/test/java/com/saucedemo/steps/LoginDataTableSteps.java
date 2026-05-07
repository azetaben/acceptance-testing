package com.saucedemo.steps;

import com.saucedemo.constants.AppConstants;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
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

    private static final By LOGIN_ERROR = By.cssSelector("h3[data-test='error']");
    private final PageManager pm;

    public LoginDataTableSteps() {
        this.pm = PageManager.getInstance();
    }

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    @When("I login with raw credential rows:")
    public void iLoginWithRawCredentialRows(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);

        for (List<String> row : rows) {
            String username = row.get(0);
            String password = row.get(1);
            loginPage().login(username, password);
        }
    }

    @When("I login with the following credentials list:")
    public void iLoginWithTheFollowingCredentialsList(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String username = row.get("username");
            String password = row.get("password");
            loginPage().login(username, password);
        }
    }

    @When("I login with the following credentials map:")
    public void iLoginWithTheFollowingCredentialsMap(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String username = credentials.get("username");
        String password = credentials.get("password");
        loginPage().login(username, password);
    }

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

    @When("I attempt login with invalid credentials map:")
    public void iAttemptLoginWithInvalidCredentialsMap(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        loginPage().enterUsername(credentials.getOrDefault("username", ""));
        loginPage().enterPassword(credentials.getOrDefault("password", ""));
        loginPage().clickLoginButton();
    }

    @Then("I attempt login with invalid grouped credentials and verify errors:")
    public void iAttemptLoginWithInvalidGroupedCredentialsAndVerifyErrors(DataTable dataTable) {
        Map<String, List<String>> grouped = buildMapOfLists(dataTable);
        List<String> usernames = grouped.getOrDefault("username", List.of());
        List<String> passwords = grouped.getOrDefault("password", List.of());
        List<String> expectedErrors = grouped.getOrDefault("expectedError", List.of());

        for (int i = 0; i < usernames.size(); i++) {
            navigateToLoginPage();
            loginPage().enterUsername(i < usernames.size() ? usernames.get(i) : "");
            loginPage().enterPassword(i < passwords.size() ? passwords.get(i) : "");
            loginPage().clickLoginButton();
            assertLoginError(i < expectedErrors.size() ? expectedErrors.get(i) : "");
        }
    }

    @Then("I attempt login with invalid named test cases and verify errors:")
    public void iAttemptLoginWithInvalidNamedTestCasesAndVerifyErrors(DataTable dataTable) {
        Map<String, Map<String, String>> testCases = buildMapOfMaps(dataTable);
        for (Map.Entry<String, Map<String, String>> entry : testCases.entrySet()) {
            Map<String, String> fields = entry.getValue();
            String username = fields.getOrDefault("username", "");
            String password = fields.getOrDefault("password", "");
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
                .navigate().to(GlobalVarsHelper.getURL());
    }

    private void assertLoginError(String expectedError) {
        WebDriver driver = WebDrv.getInstance().getWebDriver();
        new WebDriverWait(driver, Duration.ofSeconds(AppConstants.SHORT_TIME_OUT))
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


    private Map<String, Map<String, String>> buildMapOfMaps(DataTable dataTable) {
        List<List<String>> raw = dataTable.asLists(String.class);
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        if (raw.size() < 2) return result;

        List<String> headers = raw.get(0);
        String outerKey = headers.get(0);

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
