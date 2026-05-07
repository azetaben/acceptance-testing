package com.saucedemo.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {

    private ExcelUtils() {
    }

    public static int getRowCount(String xlfile, String xlsheet) throws IOException {
        try (FileInputStream fi = new FileInputStream(xlfile);
             XSSFWorkbook wb = new XSSFWorkbook(fi)) {
            return wb.getSheet(xlsheet).getLastRowNum();
        }
    }

    public static int getCellCount(String xlfile, String xlsheet, int rownum) throws IOException {
        try (FileInputStream fi = new FileInputStream(xlfile);
             XSSFWorkbook wb = new XSSFWorkbook(fi)) {
            return wb.getSheet(xlsheet).getRow(rownum).getLastCellNum();
        }
    }

    public static String getCellData(String xlfile, String xlsheet, int rownum, int colnum) throws IOException {
        try (FileInputStream fi = new FileInputStream(xlfile);
             XSSFWorkbook wb = new XSSFWorkbook(fi)) {
            var row = wb.getSheet(xlsheet).getRow(rownum);
            if (row == null) return "";
            var cell = row.getCell(colnum);
            if (cell == null) return "";
            try {
                return new DataFormatter().formatCellValue(cell);
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static void setCellData(String xlfile, String xlsheet, int rownum, int colnum, String data) throws IOException {
        try (FileInputStream fi = new FileInputStream(xlfile);
             XSSFWorkbook wb = new XSSFWorkbook(fi)) {
            wb.getSheet(xlsheet).getRow(rownum).createCell(colnum).setCellValue(data);
            try (FileOutputStream fo = new FileOutputStream(xlfile)) {
                wb.write(fo);
            }
        }
    }

    public static void fillGreenColor(String xlfile, String xlsheet, int rownum, int colnum) throws IOException {
        fillColor(xlfile, xlsheet, rownum, colnum, IndexedColors.GREEN);
    }

    public static void fillRedColor(String xlfile, String xlsheet, int rownum, int colnum) throws IOException {
        fillColor(xlfile, xlsheet, rownum, colnum, IndexedColors.RED);
    }

    private static void fillColor(String xlfile, String xlsheet, int rownum, int colnum, IndexedColors color) throws IOException {
        try (FileInputStream fi = new FileInputStream(xlfile);
             XSSFWorkbook wb = new XSSFWorkbook(fi)) {
            CellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            wb.getSheet(xlsheet).getRow(rownum).getCell(colnum).setCellStyle(style);
            try (FileOutputStream fo = new FileOutputStream(xlfile)) {
                wb.write(fo);
            }
        }
    }
}
