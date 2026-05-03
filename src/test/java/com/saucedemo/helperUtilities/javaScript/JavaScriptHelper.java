package com.saucedemo.helperUtilities.javaScript;


import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class JavaScriptHelper {

    private final Logger log = LoggerHelper.getLogger(JavaScriptHelper.class);

    public JavaScriptHelper() {

    }

    public Object executeScript(String script) {
        JavascriptExecutor exe = (JavascriptExecutor) WebDrv.getInstance().getWebDriver();
        log.info(script);
        return exe.executeScript(script);
    }

    public Object executeScript(String script, Object... args) {
        JavascriptExecutor exe = (JavascriptExecutor) WebDrv.getInstance().getWebDriver();
        log.info(script);
        return exe.executeScript(script, args);
    }

    public void scrollToElement(WebElement element) {
        executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x, element.getLocation().y);
        log.info("Scrolled to the element" + element.getText());
    }

    public void scrollToElementAndClick(WebElement element) {
        scrollToElementAndClick(element);
        log.info("clicked on the scrolled element");
    }

    public void scrollIntoView(WebElement element) {
        executeScript("arguments[0].scrollIntoView()", element);
        log.info("Scrolled into element view: " + element.getText());
    }

    public void scrollDownVertically() {
        executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollUpVertically() {
        executeScript("window.scrollTo(0, -document.body.scrollHeight)");
    }

    public void scrollDownByPixel() {
        executeScript("window.scrollBy(0,1500)");
    }

    public void scrollUpByPixel() {
        executeScript("window.scrollBy(0,-1500)");
    }

    public void ZoomInByPercentage() {
        executeScript("document.body.style.zoom='40%'");
    }

    public void ZoomBy100percentage() {
        executeScript("document.body.style.zoom='100%'");
    }

    public void zoomInBy60Percentage() {
        executeScript("document.body.style.zoom='60%'");
    }

    public void zoomInBy100Percentage() {
        executeScript("document.body.style.zoom='100%'");
    }

    public void clickElement(WebElement element) {
        executeScript("arguments[0].click();", element);
        log.info("clicked on the element: " + element.getText());
    }
}
