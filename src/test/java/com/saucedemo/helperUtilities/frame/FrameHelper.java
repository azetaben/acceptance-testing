package com.saucedemo.helperUtilities.frame;

import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FrameHelper {
    private final Logger log = LogManager.getLogger(this.getClass());
    private WebDriver driver;


    public void switchToFrame(int frameIndex) {
        driver.switchTo().frame(frameIndex);
        log.info("switched to :" + frameIndex + " frame");
    }

    public void switchToFrame(String frameName) {
        driver.switchTo().frame(frameName);
        log.info("switched to :" + frameName + " frame");
    }

    public void switchToFrame(WebElement element) {
        driver.switchTo().frame(element);
        log.info("switched to frame " + element.toString());
    }

    public void waitForIframeAndSwitch(String idOrName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getExplicitTimeout()));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(idOrName));
            log.info("Switched to iframe");
        } catch (Exception e) {
            log.error("Error occurred while switching to Iframe");
        }
    }

    public void waitForIframeAndSwitch(int index) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalVarsHelper.getExplicitTimeout()));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
            log.info("Switched to iframe");
        } catch (Exception e) {
            log.error("Error occurred while switching to Iframe");
        }
    }

    public void switchBackToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            log.info("Switched to the default content");
        } catch (Exception e) {
            log.error("Error occurred while switching to the default content");
        }
    }
}
