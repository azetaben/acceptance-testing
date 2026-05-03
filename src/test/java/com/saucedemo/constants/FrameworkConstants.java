package com.saucedemo.constants;

import com.saucedemo.utils.PathUtil;

public final class FrameworkConstants {

    private static final int IMPLICIT_WAIT = 10;
    private static final int EXPLICIT_WAIT = 15;
    private static final String RESOURCES_PATH = PathUtil.getAbsolutePath("src", "test", "resources");
    private static final String CONFIG_PROPERTIES_DIRECTORY = PathUtil.getConfigPropertiesPath();
    private static final String JSONPATH = PathUtil.getTestDataJsonFilePath("Register.json");
    private static final String EXCEL_FILE_PATH = PathUtil.getTestDataExcelFilePath("testData.xlsx");
    private static final String EXCELPATH = PathUtil.getTestDataExcelFilePath("testData.xlsx");
    private static final String CONFIG_FILE_PATH = PathUtil.getConfigPropertiesPath();
    private static final String JSON_CONFIG_FILE_PATH = PathUtil.getTestDataJsonFilePath("Register.json");
    private static final String RUN_MANGER_SHEET = "MANAGER";
    private static final String ITERATIONDATASHEET = "DATA";
    private static final String EXTENT_REPORT_FOLDER_PATH = PathUtil.getReportsDir();
    private FrameworkConstants() {
    }

    public static String getConfigPropertiesDirectory() {
        return CONFIG_PROPERTIES_DIRECTORY;
    }

    public static String getJsonpath() {
        return JSONPATH;
    }

    public static int getImplicitWait() {
        return IMPLICIT_WAIT;
    }

    public static int getExplicitWait() {
        return EXPLICIT_WAIT;
    }

    public static String getExcelFilePath() {
        return EXCEL_FILE_PATH;
    }

    public static String getJsonConfigFilePath() {
        return JSON_CONFIG_FILE_PATH;
    }

    public static String getRunManagerDatasheet() {
        return RUN_MANGER_SHEET;
    }

    public static String getIterationDatasheet() {
        return ITERATIONDATASHEET;
    }

    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }
}
