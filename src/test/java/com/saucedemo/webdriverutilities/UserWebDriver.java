package com.saucedemo.webdriverutilities;

import com.saucedemo.utils.PathUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class UserWebDriver {
    private static final Logger log = LogManager.getLogger(UserWebDriver.class);
    private static UserWebDriver instance = null;
    private WebDriver webDriver;

    private UserWebDriver() {
    }

    public static UserWebDriver getInstance() {
        if (instance == null) {
            instance = new UserWebDriver();
        }
        return instance;
    }

    public static void getScreenshot(WebElement element) throws IOException {
        Objects.requireNonNull(element, "WebElement must not be null");
        File srcScreenShot = element.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get(PathUtil.getScreenshotsDirFileName("screenShot.png"));
        Path parent = destination.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        FileHandler.copy(srcScreenShot, destination.toFile());
        log.info("Screenshot saved: " + destination.toAbsolutePath());
    }

    public static String takeElementScreenshot(WebElement element, String fileNamePrefix) throws IOException {
        Objects.requireNonNull(element, "WebElement must not be null");
        File srcScreenShot = element.getScreenshotAs(OutputType.FILE);
        String screenshotsDirectory = PathUtil.getScreenshotsDir();
        Path screenshotDirPath = Paths.get(screenshotsDirectory);
        if (!Files.exists(screenshotDirPath)) {
            Files.createDirectories(screenshotDirPath);
        }

        String safePrefix = (fileNamePrefix == null || fileNamePrefix.trim().isEmpty())
                ? "screenshot"
                : fileNamePrefix.trim();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String screenshotFileName = safePrefix + "_" + timestamp + ".png";
        File destFile = new File(screenshotDirPath.resolve(screenshotFileName).toString());

        FileHandler.copy(srcScreenShot, destFile);
        log.info("Screenshot saved: " + destFile.getAbsolutePath());
        return destFile.getAbsolutePath();
    }

    public WebDriver openBrowser(String url, DesiredCapabilities caps) {
        if (caps != null) {
            log.debug("DesiredCapabilities provided but ignored; browser options are configured in WebDriverConfig");
        }
        webDriver = WebDriverConfig.configureAndOpenBrowser(url);
        return webDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;

    }

}
