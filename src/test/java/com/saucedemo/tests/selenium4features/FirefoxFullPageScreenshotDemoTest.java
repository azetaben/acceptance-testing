package com.saucedemo.tests.selenium4features;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.utils.PathUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FirefoxFullPageScreenshotDemoTest extends Selenium4FeatureBaseTest {
    private FirefoxDriver firefox;

    @BeforeMethod
    public void setUp() {
        firefox = createFirefoxDriver();
        driver = firefox;
    }

    @Test(description = "Selenium 4 Firefox full page screenshot with getFullPageScreenshotAs")
    public void shouldCaptureFullPageScreenshotInFirefox() throws IOException {
        firefox.get(baseUrl);

        List<WebElement> loginButtons = firefox.findElements(By.id(SauceDemoConstants.LOGIN_BUTTON_ID));
        Assert.assertFalse(loginButtons.isEmpty(), "Expected login page to be ready before screenshot capture.");

        File src = firefox.getFullPageScreenshotAs(OutputType.FILE);
        Path screenshotsDir = Path.of(PathUtil.getScreenshotsDir());
        Files.createDirectories(screenshotsDir);

        Path destination = screenshotsDir.resolve("selenium4-firefox-fullpage.png");
        Files.copy(src.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        Assert.assertTrue(Files.exists(destination), "Expected full-page screenshot file to be created.");
        Assert.assertTrue(Files.size(destination) > 0, "Expected non-empty full-page screenshot file.");
    }
}
