package com.saucedemo.utils;

import com.saucedemo.configreader.FrameworkConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathUtil {
    private static final String SRC = "src";
    private static final String TEST = "test";
    private static final String JAVA = "java";
    private static final String RESOURCES = "resources";
    private static final String CONFIG = "config";
    private static final String TEST_DATA = "testData";
    private static final String TARGET = "target";
    private static final String SCREENSHOTS = "screenshots";
    private static final String REPORT = "report";

    private PathUtil() {
    }

    public static String getProjectRootDir() {
        return System.getProperty("user.dir");
    }

    public static String getAbsolutePath(String... relativePathSegments) {
        Path path = Paths.get(getProjectRootDir(), relativePathSegments);
        return path.normalize().toString();
    }

    public static String getCucumberOptionsFeatureFilePath() {
        return getAbsolutePath(SRC, TEST, RESOURCES, "features");
    }

    public static String getConfigPropertiesPath() {
        return getConfigFilePath("config.properties");
    }

    public static String getConfigFilePath(String fileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, CONFIG, fileName);
    }

    public static String getLog4jPropertiesPath() {
        return getConfigFilePath("log4j.properties");
    }


    public static String getTestDataDir() {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA);
    }

    public static String getTestDataFilePath(String fileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, fileName);
    }

    public static String getTestDataCsvFilePath(String csvFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "csvFiles", csvFileName);
    }

    public static String getTestDataXmlFilePath(String xmlFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "xmlFiles", xmlFileName);
    }

    public static String getTestDataYamlFilePath(String yamlFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "yamlFiles", yamlFileName);
    }

    public static String getPerformanceScorePath(String jsonFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "jsonFiles", jsonFileName);
    }

    public static String getTestDataExcelFilePath(String excelFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "excelFiles", excelFileName);
    }

    public static String getTestDataJsonFilePath(String jsonFileName) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, "jsonFiles", jsonFileName);
    }

    public static String getScreenshotsDir() {
        return getAbsolutePath(TARGET, SCREENSHOTS);
    }

    public static String getScreenshotsDirFileName(String screenShotName) {
        return getAbsolutePath(TARGET, SCREENSHOTS, screenShotName);
    }


    public static String getReportsDir() {
        return getAbsolutePath(TARGET, "reports");
    }

    public static String getExcelFilePath(String s) {
        return getAbsolutePath(SRC, TEST, RESOURCES, TEST_DATA, s);
    }

    public static String getDownloadsDir() {
        return getAbsolutePath(FrameworkConfig.getInstance().getString("download.dir", "file_downloads"));
    }

    public static String getUploadsDir() {
        return getAbsolutePath(FrameworkConfig.getInstance().getString("upload.dir", "file_uploads"));
    }

    public static String getReportingCsvDir() {
        return getAbsolutePath(FrameworkConfig.getInstance().getString("reporting.csv.dir", "reporting_csv_data"));
    }

    public static String getLighthouseReportPath() {
        String configuredPath = FrameworkConfig.getInstance().getString("lighthouse.report.path", REPORT + "/lighthouse-report.json");
        return getAbsolutePath(configuredPath.split("[/\\\\]"));
    }


}
