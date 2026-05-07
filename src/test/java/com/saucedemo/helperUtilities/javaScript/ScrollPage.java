package com.saucedemo.helperutilities.javascript;

import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ScrollPage {

    private static final Logger log = LogManager.getLogger(ScrollPage.class);

    public static void scrollToView(WebElement elementName) {
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", elementName);
    }

    public static void scrollToViewSaveAndContinue(WebDriver driver) {
        try {
            scrollToView(driver.findElement(By.id("save_continue")));
        } catch (Exception e) {
            log.warn("Could not scroll to save_continue element", e);
        }
    }

    public static void scrollToViewContinue(WebDriver driver) {
        try {
            scrollToView(driver.findElement(By.id("continue")));
        } catch (Exception e) {
            log.warn("Could not scroll to continue element", e);
        }
    }

    public static void scrollToViewCancel(WebDriver driver) {
        try {
            scrollToView(driver.findElement(By.id("cancel")));
        } catch (Exception e) {
            log.warn("Could not scroll to cancel element", e);
        }
    }

    public static void scrollToViewElement(WebDriver driver, String elementID) {
        try {
            scrollToView(driver.findElement(By.id(elementID)));
        } catch (Exception e) {
            log.warn("Could not scroll to element with id: " + elementID, e);
        }
    }

    public static void scrollToBottomOfThePage() {
        JavascriptExecutor js = (JavascriptExecutor) WebDrv.getInstance().getWebDriver();
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
    }
}
