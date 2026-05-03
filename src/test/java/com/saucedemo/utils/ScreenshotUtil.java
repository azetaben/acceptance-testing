package com.saucedemo.utils;

import com.saucedemo.webdriverutilities.WebDrv;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private final WebDriver driver = WebDrv.getInstance().getWebDriver();

    private static File captureScreenshotAsFile(WebDriver driver) {
        if (WebDrv.getInstance().getWebDriver() == null) {
            log.error("WebDriver instance is null. Cannot take screenshot.");
            return null;
        }
        if (!(driver instanceof TakesScreenshot)) {
            log.error("Driver does not support taking screenshots. Driver class: {}", driver.getClass().getName());
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } catch (WebDriverException e) {
            log.error("Failed to capture screenshot from WebDriver: {}", e.getMessage(), e);
            return null;
        }
    }


    public static void takeScreenshotAsFile(WebDriver driver, String filePath) {
        TakesScreenshot screenshotTaker = (TakesScreenshot) WebDrv.getInstance().getWebDriver();

        File screenshotFile = screenshotTaker.getScreenshotAs(OutputType.FILE);
        try {
            // Copy the screenshot file to the specified location
            FileHandler.copy(screenshotFile, new File(filePath));
            System.out.println("Screenshot saved at: " + filePath);
            log.info("Screenshot saved at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving screenshot: " + e.getMessage());
            log.error("Error saving screenshot: " + e.getMessage(), e);
        }
    }

    private static String saveScreenshotFile(File screenshotFile, File destinationFile) {
        if (screenshotFile == null) {
            return null;
        }
        try {
            File parentDir = destinationFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    log.error("Could not create directory: {}", parentDir.getAbsolutePath());
                    return null;
                }
            }
            FileHandler.copy(screenshotFile, destinationFile);
            log.info("Screenshot saved at: {}", destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Error saving screenshot to {}: {}", destinationFile.getAbsolutePath(), e.getMessage(), e);
            return null;
        }
    }

    public static String takeScreenshot(WebDriver driver, String fullFilePath) {
        if (fullFilePath == null || fullFilePath.trim().isEmpty()) {
            log.error("File path cannot be null or empty.");
            return null;
        }
        File screenshotFile = captureScreenshotAsFile(driver);
        if (screenshotFile == null) return null;
        return saveScreenshotFile(screenshotFile, new File(fullFilePath));
    }

    public static String takeScreenshotWithTimestamp(WebDriver driver, String directory) {
        if (directory == null || directory.trim().isEmpty()) {
            log.error("Directory path cannot be null or empty.");
            return null;
        }
        File screenshotFile = captureScreenshotAsFile(driver);
        if (screenshotFile == null) return null;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String fileName = "screenshot_" + timestamp + ".png";
        File destinationFile = new File(directory, fileName);
        return saveScreenshotFile(screenshotFile, destinationFile);
    }

    public static String takeScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            log.error("WebDriver instance is null. Cannot take screenshot.");
            return null;
        }
        if (!(driver instanceof TakesScreenshot)) {
            log.error("Driver does not support taking screenshots. Driver class: {}", driver.getClass().getName());
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (WebDriverException e) {
            log.error("Failed to capture screenshot as Base64: {}", e.getMessage(), e);
            return null;
        }
    }

    public static String takeScreenshotWithCustomName(WebDriver driver, String directory, String baseFileName) {
        if (directory == null || directory.trim().isEmpty()) {
            log.error("Directory path cannot be null or empty.");
            return null;
        }
        if (baseFileName == null || baseFileName.trim().isEmpty()) {
            log.warn("Base file name is null or empty. Falling back to timestamped name.");
            return takeScreenshotWithTimestamp(driver, directory); // Fallback behavior
        }

        File screenshotFile = captureScreenshotAsFile(driver);
        if (screenshotFile == null) return null;
        String finalFileName = baseFileName.toLowerCase().endsWith(".png") ? baseFileName : baseFileName + ".png";
        File destinationFile = new File(directory, finalFileName);
        return saveScreenshotFile(screenshotFile, destinationFile);
    }

}