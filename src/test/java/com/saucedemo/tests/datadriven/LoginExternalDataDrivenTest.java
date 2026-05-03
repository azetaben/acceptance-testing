package com.saucedemo.tests.datadriven;

import com.fasterxml.jackson.core.type.TypeReference;
import com.saucedemo.constants.SauceDemoConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucedemo.annotations.FrameworkAnnotation;
import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.enums.CategoryType;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.utils.ExcelUtils;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LoginExternalDataDrivenTest {
    private static final String JSON_DATA_FILE = PathUtil.getTestDataJsonFilePath("login_external_data.json");
    private static final String EXCEL_FILE = PathUtil.getTestDataExcelFilePath("login_external_data.xlsx");
    private static final String EXCEL_SHEET = "login";
    private static final int COLUMN_COUNT = 5;
    private static final String BASE_URL = FrameworkConfig.getInstance().getString(
            "useruitesting.url",
            FrameworkConfig.getInstance().getString(
                    "uitesting.url",
                    FrameworkConfig.getInstance().getString("url", "https://saucedemo.com/")
            )
    );

    private static Object[][] toObjectMatrix(List<LoginDataRow> rows) {
        Object[][] matrix = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i][0] = rows.get(i);
        }
        return matrix;
    }

    private static List<LoginDataRow> readJsonData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(JSON_DATA_FILE);
        List<Map<String, String>> rows = objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {
        });

        List<LoginDataRow> dataRows = new ArrayList<>();
        for (Map<String, String> row : rows) {
            dataRows.add(mapToLoginDataRow(row));
        }
        return dataRows;
    }

    private static List<LoginDataRow> readExcelData() throws IOException {
        int rowCount = ExcelUtils.getRowCount(EXCEL_FILE, EXCEL_SHEET);
        int columnCount = ExcelUtils.getCellCount(EXCEL_FILE, EXCEL_SHEET, 0);

        List<String> headers = new ArrayList<>();
        for (int col = 0; col < columnCount; col++) {
            headers.add(ExcelUtils.getCellData(EXCEL_FILE, EXCEL_SHEET, 0, col).trim());
        }

        List<LoginDataRow> dataRows = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int col = 0; col < columnCount; col++) {
                rowMap.put(headers.get(col), ExcelUtils.getCellData(EXCEL_FILE, EXCEL_SHEET, rowIndex, col));
            }
            dataRows.add(mapToLoginDataRow(rowMap));
        }

        return dataRows;
    }

    private static LoginDataRow mapToLoginDataRow(Map<String, String> row) {
        String username = normalize(getValue(row, "username"));
        if (username.isBlank()) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey() == null ? "" : entry.getKey().trim().toLowerCase(Locale.ROOT);
                if (!"password".equals(key)
                        && !"expectedresult".equals(key)
                        && !"expectedmessage".equals(key)
                        && !"testcaseid".equals(key)) {
                    username = normalize(entry.getValue());
                    break;
                }
            }
        }

        String password = normalize(getValue(row, "password"));
        String expectedResult = normalize(getValue(row, "expectedResult"));
        String expectedMessage = normalize(getValue(row, "expectedMessage"));
        String testCaseId = normalize(getValue(row, "testCaseId"));

        if (expectedResult.isBlank()) {
            expectedResult = inferExpectedResult(username);
        }
        if (testCaseId.isBlank()) {
            testCaseId = "AUTO_" + username;
        }

        return new LoginDataRow(testCaseId, username, password, expectedResult, expectedMessage);
    }

    private static String getValue(Map<String, String> row, String expectedKey) {
        for (Map.Entry<String, String> entry : row.entrySet()) {
            if (entry.getKey() != null && entry.getKey().trim().equalsIgnoreCase(expectedKey)) {
                return entry.getValue();
            }
        }
        return "";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String inferExpectedResult(String username) {
        if ("standard_user".equalsIgnoreCase(username)
                || "problem_user".equalsIgnoreCase(username)
                || "performance_glitch_user".equalsIgnoreCase(username)
                || "error_user".equalsIgnoreCase(username)
                || "visual_user".equalsIgnoreCase(username)) {
            return "SUCCESS";
        }
        return "FAILURE";
    }

    private static synchronized void ensureExcelTestDataFile() throws IOException {
        Path filePath = Path.of(EXCEL_FILE);
        if (Files.exists(filePath)) {
            return;
        }

        Files.createDirectories(filePath.getParent());
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(EXCEL_SHEET);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("testCaseId");
            header.createCell(1).setCellValue("username");
            header.createCell(2).setCellValue("password");
            header.createCell(3).setCellValue("expectedResult");
            header.createCell(4).setCellValue("expectedMessage");

            // TC 001 - standard_user valid login
            addRow(sheet, 1, "XL_TC_001", SauceDemoConstants.USER_STANDARD, SauceDemoConstants.USER_PASSWORD, "SUCCESS", "");
            // TC 002 - locked_out_user
            addRow(sheet, 2, "XL_TC_002", SauceDemoConstants.USER_LOCKED_OUT, SauceDemoConstants.USER_PASSWORD, "FAILURE", SauceDemoConstants.ERR_LOCKED_OUT);
            // TC 003 - problem_user (logs in but UI has issues)
            addRow(sheet, 3, "XL_TC_003", SauceDemoConstants.USER_PROBLEM, SauceDemoConstants.USER_PASSWORD, "SUCCESS", "");
            // TC 004 - performance_glitch_user
            addRow(sheet, 4, "XL_TC_004", SauceDemoConstants.USER_PERFORMANCE_GLITCH, SauceDemoConstants.USER_PASSWORD, "SUCCESS", "");
            // TC 005 - error_user
            addRow(sheet, 5, "XL_TC_005", SauceDemoConstants.USER_ERROR, SauceDemoConstants.USER_PASSWORD, "SUCCESS", "");
            // TC 006 - visual_user
            addRow(sheet, 6, "XL_TC_006", SauceDemoConstants.USER_VISUAL, SauceDemoConstants.USER_PASSWORD, "SUCCESS", "");
            // TC 007 - completely invalid credentials
            addRow(sheet, 7, "XL_TC_007", SauceDemoConstants.USER_INVALID, "wrong_password", "FAILURE", SauceDemoConstants.ERR_WRONG_CREDENTIALS);
            // TC 008 - valid user with wrong password
            addRow(sheet, 8, "XL_TC_008", SauceDemoConstants.USER_STANDARD, "wrong_password", "FAILURE", SauceDemoConstants.ERR_WRONG_CREDENTIALS);
            // TC 009 - empty username
            addRow(sheet, 9, "XL_TC_009", "", SauceDemoConstants.USER_PASSWORD, "FAILURE", SauceDemoConstants.ERR_USERNAME_REQUIRED);
            // TC 010 - empty password
            addRow(sheet, 10, "XL_TC_010", SauceDemoConstants.USER_STANDARD, "", "FAILURE", SauceDemoConstants.ERR_PASSWORD_REQUIRED);
            // TC 011 - both fields empty
            addRow(sheet, 11, "XL_TC_011", "", "", "FAILURE", SauceDemoConstants.ERR_USERNAME_REQUIRED);
            // TC 012 - locked_out_user with wrong password
            addRow(sheet, 12, "XL_TC_012", SauceDemoConstants.USER_LOCKED_OUT, "wrong_password", "FAILURE", SauceDemoConstants.ERR_WRONG_CREDENTIALS);

            for (int i = 0; i < COLUMN_COUNT; i++) {
                sheet.autoSizeColumn(i);
            }

            try (OutputStream outputStream = Files.newOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        }
    }

    private static void addRow(Sheet sheet, int rowIndex,
                               String tcId, String username, String password,
                               String expectedResult, String expectedMessage) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(tcId);
        row.createCell(1).setCellValue(username);
        row.createCell(2).setCellValue(password);
        row.createCell(3).setCellValue(expectedResult);
        row.createCell(4).setCellValue(expectedMessage);
    }

    @BeforeMethod
    public void setUp() {
        WebDrv.getInstance().openBrowser(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        PageManager.getInstance().clear();
        WebDrv.getInstance().closeCurrentDriver();
    }

    @FrameworkAnnotation(
            author = {"QA Team"},
            tags = {"login", "smoke", "json"},
            category = {CategoryType.SMOKE}
    )
    @Test(dataProvider = "loginDataFromJson")
    public void loginWithJsonData(LoginDataRow dataRow) {
        executeLogin(dataRow, "JSON");
    }

    @FrameworkAnnotation(
            author = {"QA Team"},
            tags = {"login", "regression", "excel"},
            category = {CategoryType.REGRESSION}
    )
    @Test(dataProvider = "loginDataFromExcel")
    public void loginWithExcelData(LoginDataRow dataRow) {
        executeLogin(dataRow, "EXCEL");
    }

    @DataProvider(name = "loginDataFromJson")
    public Object[][] loginDataFromJson() throws IOException {
        return toObjectMatrix(readJsonData());
    }

    @DataProvider(name = "loginDataFromExcel")
    public Object[][] loginDataFromExcel() throws IOException {
        ensureExcelTestDataFile();
        return toObjectMatrix(readExcelData());
    }

    private void executeLogin(LoginDataRow dataRow, String source) {
        LoginPage loginPage = PageManager.getInstance().getPage(LoginPage.class);
        loginPage.enterUsername(dataRow.username());
        loginPage.enterPassword(dataRow.password());
        loginPage.clickLoginButton();

        if ("SUCCESS".equalsIgnoreCase(dataRow.expectedResult())) {
            InventoryPage inventoryPage = PageManager.getInstance().getPage(InventoryPage.class);
            Assert.assertTrue(inventoryPage.isPageHeaderVisible(),
                    source + " " + dataRow.testCaseId() + " expected a successful login, but Products page is not visible.");
            Assert.assertTrue(inventoryPage.getProductCount() > 0,
                    source + " " + dataRow.testCaseId() + " expected products to be visible after login.");
            return;
        }

        Assert.assertTrue(loginPage.isLoginErrorMessageDisplayed(),
                source + " " + dataRow.testCaseId() + " expected a login error, but no error message was shown.");

        if (!dataRow.expectedMessage().isBlank()) {
            String actualMessage = loginPage.getLoginErrorMessageText();
            Assert.assertTrue(actualMessage.contains(dataRow.expectedMessage()),
                    source + " " + dataRow.testCaseId() + " error text mismatch. Expected to contain: '"
                            + dataRow.expectedMessage() + "' but was: '" + actualMessage + "'.");
        }
    }

    private record LoginDataRow(
            String testCaseId,
            String username,
            String password,
            String expectedResult,
            String expectedMessage
    ) {
        @Override
        public String toString() {
            return testCaseId + "|" + username + "|" + expectedResult;
        }
    }
}


