package com.saucedemo.testdata;

import com.saucedemo.models.ExternalLoginDataRow;
import com.saucedemo.utils.ExcelUtils;
import com.saucedemo.utils.PathUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ExcelLoginDataSource implements LoginDataSource {
    private final String excelFilePath;
    private final String sheetName;
    private volatile Map<String, ExternalLoginDataRow> rowsById;

    public ExcelLoginDataSource(String excelFilePath, String sheetName) {
        this.excelFilePath = excelFilePath;
        this.sheetName = sheetName;
    }

    public ExcelLoginDataSource() {
        this(PathUtil.getTestDataExcelFilePath("login_external_data.xlsx"), "login");
    }

    private static String get(Map<String, String> row, String key) {
        if (row == null || key == null) return "";
        return row.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .filter(e -> e.getKey().trim().equalsIgnoreCase(key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }

    private static String normalize(String v) {
        return v == null ? "" : v.trim();
    }

    @Override
    public ExternalLoginDataRow findById(String testCaseId) {
        if (testCaseId == null || testCaseId.isBlank()) {
            throw new IllegalArgumentException("testCaseId must not be blank");
        }
        String key = testCaseId.trim().toUpperCase(Locale.ROOT);
        ExternalLoginDataRow row = loadRows().get(key);
        if (row == null) {
            throw new IllegalArgumentException("No Excel login data row found for testCaseId: " + testCaseId
                    + " in file: " + excelFilePath + " sheet: " + sheetName);
        }
        return row;
    }

    private Map<String, ExternalLoginDataRow> loadRows() {
        if (rowsById != null) {
            return rowsById;
        }
        synchronized (this) {
            if (rowsById != null) {
                return rowsById;
            }
            try {
                int rowCount = ExcelUtils.getRowCount(excelFilePath, sheetName);
                int columnCount = ExcelUtils.getCellCount(excelFilePath, sheetName, 0);

                List<String> headers = new java.util.ArrayList<>();
                for (int col = 0; col < columnCount; col++) {
                    headers.add(ExcelUtils.getCellData(excelFilePath, sheetName, 0, col));
                }

                Map<String, ExternalLoginDataRow> loaded = new LinkedHashMap<>();
                for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    for (int col = 0; col < columnCount; col++) {
                        rowMap.put(headers.get(col), ExcelUtils.getCellData(excelFilePath, sheetName, rowIndex, col));
                    }
                    ExternalLoginDataRow mapped = new ExternalLoginDataRow(
                            normalize(get(rowMap, "testCaseId")),
                            normalize(get(rowMap, "username")),
                            normalize(get(rowMap, "password")),
                            normalize(get(rowMap, "expectedResult")),
                            normalize(get(rowMap, "expectedMessage"))
                    );
                    if (mapped.testCaseId() == null || mapped.testCaseId().isBlank()) {
                        continue;
                    }
                    loaded.put(mapped.testCaseId().trim().toUpperCase(Locale.ROOT), mapped);
                }

                rowsById = loaded;
                return rowsById;
            } catch (IOException e) {
                throw new RuntimeException("Unable to load Excel login data file: " + excelFilePath, e);
            }
        }
    }
}
