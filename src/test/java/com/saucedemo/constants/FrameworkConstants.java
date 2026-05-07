package com.saucedemo.constants;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.utils.PathUtil;

public final class FrameworkConstants {

    private static final int IMPLICIT_WAIT = 10;
    private static final int EXPLICIT_WAIT = 15;
    private static final int PAGE_STABILISE_DELAY_MS = 500;
    private static final int DOWNLOAD_TIMEOUT_SECONDS =
            FrameworkConfig.getInstance().getInt("download.timeout.seconds", 30);
    private static final int FLUENT_POLL_INTERVAL_MS =
            FrameworkConfig.getInstance().getInt("fluent.poll.interval.ms", 500);
    private static final String RESOURCES_PATH = PathUtil.getAbsolutePath("src", "test", "resources");
    private static final String CONFIG_PROPERTIES_DIRECTORY = PathUtil.getConfigPropertiesPath();
    private static final String JSONPATH = PathUtil.getTestDataJsonFilePath("Register.json");
    private static final String EXCEL_FILE_PATH = PathUtil.getTestDataExcelFilePath("testData.xlsx");
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
        return JSONPATH;
    }

    public static String getRunManagerDatasheet() {
        return RUN_MANGER_SHEET;
    }

    public static String getIterationDatasheet() {
        return ITERATIONDATASHEET;
    }

    public static int getPageStabiliseDelayMs() {
        return PAGE_STABILISE_DELAY_MS;
    }

    public static int getDownloadTimeoutSeconds() {
        return DOWNLOAD_TIMEOUT_SECONDS;
    }

    public static int getFluentPollIntervalMs() {
        return FLUENT_POLL_INTERVAL_MS;
    }

    public static String getConfigFilePath() {
        return CONFIG_PROPERTIES_DIRECTORY;
    }
}
