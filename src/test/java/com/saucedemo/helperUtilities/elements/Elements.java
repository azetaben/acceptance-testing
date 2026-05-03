package com.saucedemo.helperUtilities.elements;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

public class Elements {
    private static WebDriver driver;
    public Logger log = Logger.getLogger(Elements.class);

    public static void TypeText(WebElement element, String data) {
        element.sendKeys(data);
    }

    public static void TypeTextIfElementPresent(WebElement element, String data) {
        if (element.isDisplayed()) {
            element.sendKeys(data);
        }
    }

    public static void clearTxtBox(WebElement element) {
        element.clear();
    }

    public static void typeRandomNumber(WebElement element, int data) {
        Random random = new Random();
        int rn = random.nextInt(100);
        String randomNumber = Integer.toString(rn);
        element.sendKeys(randomNumber);
    }

    public static String getTextBoxValue(WebElement element) {
        return element.getDomAttribute("value");
    }

    public static String getText(WebElement element) {
        return element.getText();
    }

    public static boolean VerifyTextEquals(WebElement element, String expected) {
        boolean flag = false;
        if (element.getText().equals(expected)) return flag = true;
        return flag;
    }

    public static String getTitle() {
        return driver.getTitle();
    }

    public static String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public static boolean isSelected(WebElement element) {
        if (element.isSelected()) return true;
        return false;
    }

    public static void selectCheckBox(WebElement element) {
        if (!isSelected(element)) element.click();
    }

    public static void deSelectCheckbox(WebElement element) {
        if (isSelected(element)) element.click();
    }

    public static void selectRadioButton(WebElement element) {
        if (!isSelected(element)) element.click();
    }

    public static void deSelectRadioButton(WebElement element) {
        if (isSelected(element)) element.click();
    }

    public static boolean isEnabled(WebElement element) {
        if (element.isEnabled()) return true;
        return false;
    }

    public static boolean isDisplayed(WebElement element) {
        if (element.isDisplayed()) return true;
        return false;
    }

    public static void selectByText(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public static void selectByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public static void selectByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public static String getFirstSelectedOption(WebElement element) {
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    public static List<WebElement> getAllSelectedOptions(WebElement element) {
        Select select = new Select(element);
        return select.getAllSelectedOptions();
    }

    public static List<WebElement> getAllOptions(WebElement element) {
        Select select = new Select(element);
        return select.getOptions();
    }

    public static void deSelectByText(WebElement element, String text) {
        Select select = new Select(element);
        select.deselectByVisibleText(text);
    }

    public static void deSelectByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.deselectByIndex(index);
    }

    public static void deSelectByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.deselectByValue(value);
    }

    public static void click(WebElement element) {

        element.click();
    }

    public static void clickOnlyIfElementPresent(WebElement element) {
        if (isDisplayed(element)) element.click();
    }

    public static Alert getAlert() {
        return driver.switchTo().alert();
    }

    public static void AcceptAlert() {
        getAlert().accept();
    }

    public static void DismissAlert() {
        getAlert().dismiss();
    }

    public static String getAlertText() {
        return getAlert().getText();
    }
}
