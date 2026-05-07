package com.saucedemo.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.context.TestContext;
import com.saucedemo.domainobjects.LoginDetails;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.models.ExternalLoginDataRow;
import com.saucedemo.models.LoginData;
import com.saucedemo.models.LoginModel;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.testdata.LoginDataSources;
import com.saucedemo.utils.ExcelUtils;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class LoginSteps {
    private static final String JSON_DATA_FILE = PathUtil.getTestDataJsonFilePath("login_external_data.json");
    private static final String EXCEL_DATA_FILE = PathUtil.getTestDataExcelFilePath("login_external_data.xlsx");
    private static final String EXCEL_SHEET = "login";

    private static final ThreadLocal<ExternalLoginDataRow> ACTIVE_EXTERNAL_ROW = new ThreadLocal<>();
    private static Map<String, ExternalLoginDataRow> jsonRowsById;
    private static Map<String, ExternalLoginDataRow> excelRowsById;

    private final PageManager pm;
    private final TestContext context;
    WebDriver driver;

    public LoginSteps(TestContext context) {
        this.driver = WebDrv.getInstance().getWebDriver();
        this.context = context;
        this.pm = PageManager.getInstance();

    }

    static ExternalLoginDataRow getActiveExternalRow() {
        return ACTIVE_EXTERNAL_ROW.get();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String getValue(Map<String, String> row, String expectedKey) {
        return row.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .filter(entry -> entry.getKey().trim().equalsIgnoreCase(expectedKey))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }

    private static ExternalLoginDataRow mapToExternalLoginDataRow(Map<String, String> row) {
        return new ExternalLoginDataRow(
                normalize(getValue(row, "testCaseId")),
                normalize(getValue(row, "username")),
                normalize(getValue(row, "password")),
                normalize(getValue(row, "expectedResult")),
                normalize(getValue(row, "expectedMessage"))
        );
    }

    private static synchronized Map<String, ExternalLoginDataRow> getJsonRowsById() {
        if (jsonRowsById != null) {
            return jsonRowsById;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> rows = objectMapper.readValue(
                    new File(JSON_DATA_FILE),
                    new TypeReference<>() {
                    }
            );

            jsonRowsById = rows.stream()
                    .map(LoginSteps::mapToExternalLoginDataRow)
                    .filter(mappedRow -> !mappedRow.testCaseId().isBlank())
                    .collect(Collectors.toMap(
                            mappedRow -> mappedRow.testCaseId().toUpperCase(Locale.ROOT),
                            mappedRow -> mappedRow,
                            (existing, replacement) -> replacement,
                            LinkedHashMap::new
                    ));
            return jsonRowsById;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load JSON login data file: " + JSON_DATA_FILE, e);
        }
    }

    private static synchronized Map<String, ExternalLoginDataRow> getExcelRowsById() {
        if (excelRowsById != null) {
            return excelRowsById;
        }

        try {
            int rowCount = ExcelUtils.getRowCount(EXCEL_DATA_FILE, EXCEL_SHEET);
            int columnCount = ExcelUtils.getCellCount(EXCEL_DATA_FILE, EXCEL_SHEET, 0);

            List<String> headers = new java.util.ArrayList<>();
            for (int col = 0; col < columnCount; col++) {
                headers.add(ExcelUtils.getCellData(EXCEL_DATA_FILE, EXCEL_SHEET, 0, col));
            }

            excelRowsById = new LinkedHashMap<>();
            for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int col = 0; col < columnCount; col++) {
                    rowMap.put(headers.get(col), ExcelUtils.getCellData(EXCEL_DATA_FILE, EXCEL_SHEET, rowIndex, col));
                }
                ExternalLoginDataRow mappedRow = mapToExternalLoginDataRow(rowMap);
                if (!mappedRow.testCaseId().isBlank()) {
                    excelRowsById.put(mappedRow.testCaseId().toUpperCase(Locale.ROOT), mappedRow);
                }
            }
            return excelRowsById;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Excel login data file: " + EXCEL_DATA_FILE, e);
        }
    }

    private static String decodeBase64(String encoded) {
        try {
            if (encoded == null) {
                return "";
            }
            byte[] decoded = Base64.getDecoder().decode(encoded.trim());
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {

            return encoded == null ? "" : encoded;
        }
    }

    private LoginPage loginPage() {
        if (WebDrv.getInstance().getWebDriver() == null) {
            WebDrv.getInstance().openBrowser(GlobalVarsHelper.getLoginPageUrl());
        }
        return this.pm.getPage(LoginPage.class);
    }

    private void enterCredentials(String username, String password) {
        loginPage().enterUsername(username);
        loginPage().enterPassword(password);
    }

    private void submitLogin() {
        loginPage().clickLoginButton();
    }

    private void loginWithCredentials(String username, String password) {
        enterCredentials(username, password);
        submitLogin();
    }

    private Keys resolveSupportedKey(String keyName) {
        String normalized = normalize(keyName).toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "tab" -> Keys.TAB;
            case "enter", "return" -> Keys.ENTER;
            default ->
                    throw new IllegalArgumentException("Unsupported key: " + keyName + ". Supported keys: Tab, Enter");
        };
    }

    private ExternalLoginDataRow resolveExternalRow(String source, String testCaseId) {

        return LoginDataSources.byName(source).findById(testCaseId);
    }

    @When("I execute login using {string} source for test case {string}")
    public void iExecuteLoginUsingSourceForTestCase(String source, String testCaseId) {
        ACTIVE_EXTERNAL_ROW.set(resolveExternalRow(source, testCaseId));
        ExternalLoginDataRow row = ACTIVE_EXTERNAL_ROW.get();
        loginWithCredentials(row.username(), row.password());
    }

    @When("I login with valid credentials as standard user")
    public void iLoginWithValidCredentialsAsStandardUser(DataTable dataTable) {
        loginWithCredentials(dataTable.cell(1, 0), dataTable.cell(1, 1));
    }

    @When("I attempt login with invalid credentials as lockout user")
    public void i_attempt_login_with_invalid_locked_out_user(DataTable dataTable) {
        loginWithCredentials(dataTable.cell(1, 0), dataTable.cell(1, 1));
    }

    @When("As an accepted user I login with valid credentials")
    public void as_an_accepted_user_i_login_with_valid_credentials(DataTable dataTable) {
        loginWithCredentials(dataTable.cell(1, 0), dataTable.cell(1, 1));
    }

    @When("I enter valid credentials with username {string} and password {string}")
    public void iEnterValidCredentials(String username, String password) {
        loginPage().enterUsername(username);
        loginPage().enterPassword(password);
    }

    @When("I leave the username and password fields empty")
    public void iLeaveTheFieldsEmpty() {
        loginPage().clearUsername();
        loginPage().clearPassword();
        loginPage().clickLoginButton();
    }

    @When("I enter invalid credentials with username {string} and password {string}")
    public void iEnterInvalidCredentials(String username, String password) {
        loginWithCredentials(username, password);
    }

    @When("I login with username {string} and {string}")
    public void iLoginWithUsernameAnd(String username, String password) {
        loginWithCredentials(username, password);
    }

    @When("I enter a password in the password field")
    public void iEnterAPasswordInThePasswordField() {
        loginPage().enterPassword(GlobalVarsHelper.getPasswordForAllUsers());
    }

    @When("I click the {string} button")
    public void iClickTheButton(String buttonName) {
        if (SauceDemoConstants.BUTTON_LABEL_LOGIN.equalsIgnoreCase(buttonName)) {
            submitLogin();
        }
    }

    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        loginPage().enterUsername(username);
    }

    @When("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage().enterPassword(password);
    }

    @When("I input password {string}")
    public void iInputPasswordAlias(String password) {
        loginPage().enterPassword(password);
    }

    @When("I press the {string}")
    public void iPressThe(String keyName) {
        WebDriver driver = WebDrv.getInstance().getWebDriver();
        if (driver == null) {
            throw new IllegalStateException("No active WebDriver session. Navigate to a page before pressing keys.");
        }
        Keys key = resolveSupportedKey(keyName);
        WebElement activeElement = driver.switchTo().activeElement();
        activeElement.sendKeys(key);


        if (key == Keys.ENTER && loginPage().isLoginFormDisplayed()) {
            submitLogin();
        }
    }

    @When("I enter username {string} and password {string}")
    public void iEnterUsernameAndPassword(String username, String password) {
        enterCredentials(username, password);
    }

    @And("I click on the login button")
    public void iClickOnTheLoginButton() {
        submitLogin();
    }

    @When("I login with Base64 encoded credentials")
    public void iLoginWithBase64EncodedCredentials(DataTable dataTable) {
        Map<String, String> values = dataTable.asMap(String.class, String.class);
        String encodedUsername = values.getOrDefault("username", "");
        String encodedPassword = values.getOrDefault("password", "");

        String username = decodeBase64(encodedUsername);
        String password = decodeBase64(encodedPassword);

        loginWithCredentials(username, password);
    }

    @Given("I navigate login page and login")
    public void iNavigateLoginPageAndLogin() {
        loginWithCredentials(GlobalVarsHelper.getStandardUser(), GlobalVarsHelper.getPasswordForAllUsers());
    }

    @DataTableType
    public LoginModel convertLoginModelEntry(List<String> entry) {
        return new LoginModel(entry.get(0), entry.get(1));
    }

    @DataTableType
    public LoginData loginDataTransformer(Map<String, String> entry) {
        return new LoginData(
                entry.get("username"),
                entry.get("password"),
                entry.get("expectedError")
        );
    }

    @When("I login in login page")
    public void i_login_in_login_page(List<LoginModel> loginModelList) {
        loginModelList.stream().findFirst().ifPresentOrElse(
                loginModel -> loginWithCredentials(loginModel.username(), loginModel.password()),
                () -> {
                    throw new IllegalArgumentException("Login model list is empty. Cannot perform login.");
                }
        );
    }

    public void login(String standardUser, String standardUserPassword) {
        loginPage().login(standardUser, standardUserPassword);
        loginPage().clickLoginButton();
    }

    @When("I login username and password with {string} and {string}")
    public void i_enter_username_and_password_with(String username, String password) {
        loginPage().login(username, password);
    }

    @When("I enter username and password with {string} and {string} respectively")
    public void i_enter_username_and_password_with_respectively(String username, String password) {
        loginPage().enterUsername(username);
        loginPage().enterPassword(password);
    }

    @When("I login in login page with all {string} and password for all users {string}")
    public void i_login_in_login_with_all_user_password_for_all_user(String username, String password) {
        loginWithCredentials(username, password);
    }

    @Given("my login credentials are:")
    public void myLoginCredentialsAre(LoginDetails loginDetails) {
        context.loginDetails = loginDetails;
    }


    @When("I provide login credentials")
    public void iProvideLoginCredentials() {
        enterCredentials(context.loginDetails.username(), context.loginDetails.password());
    }

    @When("I click on {string} button")
    public void iClickOnButton(String expectedButtonName) {
        if (SauceDemoConstants.BUTTON_LABEL_LOGIN.equalsIgnoreCase(expectedButtonName)) {
            submitLogin();
        } else {
            throw new IllegalArgumentException("Unsupported button: " + expectedButtonName + ". Supported buttons: Login");
        }
    }
}
