package com.saucedemo.utils;

import com.saucedemo.constants.SauceDemoConstants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Standalone generator - creates src/test/resources/testData/excelFiles/login_external_data.xlsx
 * Run once via: mvn exec:java -Dexec.mainClass="com.saucedemo.utils.GenerateLoginExcelData"
 */
public class GenerateLoginExcelData {

    private static final String EXCEL_FILE = PathUtil.getTestDataExcelFilePath("login_external_data.xlsx");
    private static final String SHEET = "login";
    private static final int COLUMN_COUNT = 5;
    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAILURE = "FAILURE";

    public static void main(String[] args) throws IOException {
        Path filePath = Path.of(EXCEL_FILE);
        Files.createDirectories(filePath.getParent());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("testCaseId");
            header.createCell(1).setCellValue("username");
            header.createCell(2).setCellValue("password");
            header.createCell(3).setCellValue("expectedResult");
            header.createCell(4).setCellValue("expectedMessage");

            String pwd = SauceDemoConstants.USER_PASSWORD;
            addRow(sheet, 1,  "XL_TC_001", SauceDemoConstants.USER_STANDARD,          pwd,  RESULT_SUCCESS, "");
            addRow(sheet, 2,  "XL_TC_002", SauceDemoConstants.USER_LOCKED_OUT,         pwd,  RESULT_FAILURE, SauceDemoConstants.ERR_LOCKED_OUT);
            addRow(sheet, 3,  "XL_TC_003", SauceDemoConstants.USER_PROBLEM,            pwd,  RESULT_SUCCESS, "");
            addRow(sheet, 4,  "XL_TC_004", SauceDemoConstants.USER_PERFORMANCE_GLITCH, pwd,  RESULT_SUCCESS, "");
            addRow(sheet, 5,  "XL_TC_005", SauceDemoConstants.USER_ERROR,              pwd,  RESULT_SUCCESS, "");
            addRow(sheet, 6,  "XL_TC_006", SauceDemoConstants.USER_VISUAL,             pwd,  RESULT_SUCCESS, "");
            addRow(sheet, 7,  "XL_TC_007", SauceDemoConstants.USER_INVALID,            "wrong_password", RESULT_FAILURE, SauceDemoConstants.ERR_WRONG_CREDENTIALS);
            addRow(sheet, 8,  "XL_TC_008", SauceDemoConstants.USER_STANDARD,           "wrong_password", RESULT_FAILURE, SauceDemoConstants.ERR_WRONG_CREDENTIALS);
            addRow(sheet, 9,  "XL_TC_009", "",                                         pwd,  RESULT_FAILURE, SauceDemoConstants.ERR_USERNAME_REQUIRED);
            addRow(sheet, 10, "XL_TC_010", SauceDemoConstants.USER_STANDARD,           "",   RESULT_FAILURE, SauceDemoConstants.ERR_PASSWORD_REQUIRED);
            addRow(sheet, 11, "XL_TC_011", "",                                         "",   RESULT_FAILURE, SauceDemoConstants.ERR_USERNAME_REQUIRED);
            addRow(sheet, 12, "XL_TC_012", SauceDemoConstants.USER_LOCKED_OUT,         "wrong_password", RESULT_FAILURE, SauceDemoConstants.ERR_WRONG_CREDENTIALS);

            for (int i = 0; i < COLUMN_COUNT; i++) {
                sheet.autoSizeColumn(i);
            }

            try (OutputStream out = Files.newOutputStream(filePath)) {
                workbook.write(out);
            }
        }

        System.out.println("Generated: " + Path.of(EXCEL_FILE).toAbsolutePath());
    }

    private static void addRow(Sheet sheet, int rowIdx,
                               String tcId, String username, String password,
                               String expectedResult, String expectedMessage) {
        Row row = sheet.createRow(rowIdx);
        row.createCell(0).setCellValue(tcId);
        row.createCell(1).setCellValue(username);
        row.createCell(2).setCellValue(password);
        row.createCell(3).setCellValue(expectedResult);
        row.createCell(4).setCellValue(expectedMessage);
    }
}