package com.saucedemo.helperUtilities.inputFields;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.assertion.VerificationHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginInputHelper {
    private final VerificationHelper verificationHelper;

    public LoginInputHelper(WebDriver driver) {
        ExplicitWaitFactory.setDriver(driver);
        this.verificationHelper = new VerificationHelper();
    }

    public void clearAndType(By locator, String value) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) {
            element.clear();
            element.sendKeys(value);
        }
    }

    public void click(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
        if (element != null) {
            element.click();
        }
    }

    public boolean isDisplayed(By locator) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
            return element != null && verificationHelper.isDisplayed(element);
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public String getInputValue(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) return null;
        return verificationHelper.getDomAttribute(element, "value");
    }

    public String getText(By locator) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element == null) return null;
        return verificationHelper.getText(element);
    }
}
