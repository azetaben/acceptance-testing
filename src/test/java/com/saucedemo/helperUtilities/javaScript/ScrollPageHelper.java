package com.saucedemo.helperutilities.javascript;

import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollPageHelper {
    private final Logger log = LogManager.getLogger(ScrollPageHelper.class);


    public ScrollPageHelper() {


    }

    public void scrollToView(WebElement elementName) {
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", elementName);
    }

    public void scrollToViewContinue(WebDriver driver) {
        try {
            scrollToView(WebDrv.getInstance().getWebDriver().findElement(By.id("continue")));
        } catch (Exception e) {
            log.info(e);
        }
    }

    public void scrollToViewElement(WebDriver driver, String elementID) {
        try {
            scrollToView(WebDrv.getInstance().getWebDriver().findElement(By.id(elementID)));
        } catch (Exception e) {
            log.info(e);
        }
    }

    public void scrollToBottomOfThePage() {
        JavascriptExecutor js = (JavascriptExecutor) WebDrv.getInstance().getWebDriver();
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
    }
}
