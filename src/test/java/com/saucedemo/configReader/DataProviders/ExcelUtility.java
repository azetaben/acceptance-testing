package com.saucedemo.configReader.DataProviders;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtility {

    private final String path;

    public ExcelUtility(String path) {
        this.path = path;
    }

    public int getRowCount(String sheetName) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            return workbook.getSheet(sheetName).getLastRowNum();
        }
    }

    public int getCellCount(String sheetName, int rownum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            return workbook.getSheet(sheetName).getRow(rownum).getLastCellNum();
        }
    }

    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            try {
                return new DataFormatter().formatCellValue(
                        workbook.getSheet(sheetName).getRow(rownum).getCell(colnum));
            } catch (Exception e) {
                return "";
            }
        }
    }

    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException {
        if (!new File(path).exists()) {
            try (XSSFWorkbook wb = new XSSFWorkbook();
                 FileOutputStream fo = new FileOutputStream(path)) {
                wb.write(fo);
            }
        }
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            if (workbook.getSheetIndex(sheetName) == -1) {
                workbook.createSheet(sheetName);
            }
            var sheet = workbook.getSheet(sheetName);
            if (sheet.getRow(rownum) == null) {
                sheet.createRow(rownum);
            }
            sheet.getRow(rownum).createCell(colnum).setCellValue(data);
            try (FileOutputStream fo = new FileOutputStream(path)) {
                workbook.write(fo);
            }
        }
    }

    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {
        fillColor(sheetName, rownum, colnum, IndexedColors.GREEN);
    }

    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {
        fillColor(sheetName, rownum, colnum, IndexedColors.RED);
    }

    private void fillColor(String sheetName, int rownum, int colnum, IndexedColors color) throws IOException {
        try (FileInputStream fi = new FileInputStream(path);
             XSSFWorkbook workbook = new XSSFWorkbook(fi)) {
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            workbook.getSheet(sheetName).getRow(rownum).getCell(colnum).setCellStyle(style);
            try (FileOutputStream fo = new FileOutputStream(path)) {
                workbook.write(fo);
            }
        }
    }
}
