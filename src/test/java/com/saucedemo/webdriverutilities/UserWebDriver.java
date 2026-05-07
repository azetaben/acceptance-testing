package com.saucedemo.webdriverutilities;

import com.saucedemo.utils.PathUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;

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
    private static volatile UserWebDriver instance;

    private UserWebDriver() {
    }

    public static UserWebDriver getInstance() {
        if (instance == null) {
            synchronized (UserWebDriver.class) {
                if (instance == null) {
                    instance = new UserWebDriver();
                }
            }
        }
        return instance;
    }

    public static void getScreenshot(WebElement element) throws IOException {
        Objects.requireNonNull(element, "WebElement must not be null");
        File src = element.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get(PathUtil.getScreenshotsDirFileName("screenShot.png"));
        ensureDirectoryExists(destination.getParent());
        FileHandler.copy(src, destination.toFile());
        log.info("Screenshot saved: " + destination.toAbsolutePath());
    }

    public static String takeElementScreenshot(WebElement element, String fileNamePrefix) throws IOException {
        Objects.requireNonNull(element, "WebElement must not be null");
        File src = element.getScreenshotAs(OutputType.FILE);
        Path screenshotDir = Paths.get(PathUtil.getScreenshotsDir());
        ensureDirectoryExists(screenshotDir);
        String prefix = (fileNamePrefix == null || fileNamePrefix.trim().isEmpty()) ? "screenshot" : fileNamePrefix.trim();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        File dest = screenshotDir.resolve(prefix + "_" + timestamp + ".png").toFile();
        FileHandler.copy(src, dest);
        log.info("Screenshot saved: " + dest.getAbsolutePath());
        return dest.getAbsolutePath();
    }

    private static void ensureDirectoryExists(Path dir) throws IOException {
        if (dir != null && !Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    public WebDriver openBrowser(String url) {
        return WebDrv.getInstance().openBrowser(url);
    }

    public WebDriver getWebDriver() {
        return WebDrv.getInstance().getWebDriver();
    }
}
