package com.saucedemo.helperutilities.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelper {

    private String filePath;
    private String sheetName;
    private Workbook workbook;
    private Sheet sheet;
    private Row headerRow;

    public ExcelHelper(String filePath, String sheetName) throws IOException {
        this.filePath = filePath;
        this.sheetName = sheetName;
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in the Excel file.");
        }
        headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row not found in sheet '" + sheetName + "'.");
        }
    }

    public List<Map<String, String>> getData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        int totalRows = sheet.getLastRowNum();

        for (int i = 1; i <= totalRows; i++) {
            Row dataRow = sheet.getRow(i);
            if (dataRow == null || isRowEmpty(dataRow)) {
                continue;
            }
            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell headerCell = headerRow.getCell(j);
                Cell dataCell = dataRow.getCell(j);
                if (headerCell != null && dataCell != null) {
                    String headerValue = headerCell.getStringCellValue().trim();
                    String dataValue = getCellValueAsString(dataCell).trim();
                    rowData.put(headerValue, dataValue);
                }
            }
            dataList.add(rowData);
        }
        return dataList;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else {
            return "";
        }
    }

    public String getExpectedResult(int rowNum) {
        return getCellDataByColumnName(rowNum, "Expected Result");
    }

    public String getTestCaseDescription(int rowNum) {
        return getCellDataByColumnName(rowNum, "Test Case Description");
    }

    private String getCellDataByColumnName(int rowNum, String columnName) {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1) {
            return null;
        }

        Row dataRow = sheet.getRow(rowNum);
        if (dataRow == null) {
            return null;
        }

        Cell cell = dataRow.getCell(columnIndex);
        if (cell == null) {
            return null;
        }

        return getCellValueAsString(cell);
    }

    private int getColumnIndex(String columnName) {
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().equalsIgnoreCase(columnName.trim())) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
