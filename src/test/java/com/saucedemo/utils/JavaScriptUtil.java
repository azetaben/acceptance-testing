package com.saucedemo.utils;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.webdriverutilities.WebDrv;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class JavaScriptUtil {

    private final JavascriptExecutor js;
    private WebDriver driver = WebDrv.getInstance().getWebDriver();

    public JavaScriptUtil(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) this.driver;
    }

    public String getTitleByJS() {
        return Objects.requireNonNull(js.executeScript("return document.title;")).toString();
    }

    public void goBackWithJS() {
        js.executeScript("history.go(-1);");
    }

    public void goForwardWithJS() {
        js.executeScript("history.go(1);");
    }

    public void pageRefreshWithJS() {
        js.executeScript("history.go(0);");
    }

    public void generateJSAlert(String msg) {
        js.executeScript("alert('" + msg + "')");
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(ExpectedConditions.alertIsPresent())
                .accept();
    }

    public void generateJSConfirm(String mesg) {
        js.executeScript("confirm('" + mesg + "')");
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(ExpectedConditions.alertIsPresent())
                .accept();
    }

    public void generateJSPrompt(String mesg, String value) {
        js.executeScript("prompt('" + mesg + "')");
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()))
                .until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(value);
        alert.accept();
    }

    public String getPageInnerText() {
        return js.executeScript("return document.documentElement.innerText;").toString();
    }

    public void scrollPageDown() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollPageUp() {
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    public void scrollPageDown(String height) {
        js.executeScript("window.scrollTo(0, '" + height + "')");
    }

    public void scrollPageDownMiddlepage() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight/2)");
    }

    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void zoomChromeEdgeSafari(String zoomPercentage) {
        js.executeScript("document.body.style.zoom = '" + zoomPercentage + "%'");
    }

    public void zoomFirefox(String zoomPercentage) {
        js.executeScript("document.body.style.MozTransform = 'scale(" + zoomPercentage + ")'");
    }

    public void drawBorder(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    public void flash(WebElement element) {
        String bgcolor = element.getCssValue("backgroundColor");
        for (int i = 0; i < 10; i++) {
            changeColor("rgb(0,200,0)", element);
            changeColor(bgcolor, element);
        }
    }

    private void changeColor(String color, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
