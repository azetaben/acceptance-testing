/**
 * @author rahul.rathore
 * <p>07-Aug-2016
 */
package com.saucedemo.helperUtilities.textBox;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.generic.GenericHelper;
import com.saucedemo.helperUtilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TextBoxHelper extends GenericHelper {

    private final Logger log = LoggerHelper.getLogger(TextBoxHelper.class);
    private final WebDriver localDriver;

    public TextBoxHelper(WebDriver driver) {
        this.localDriver = driver;
        ExplicitWaitFactory.setDriver(driver);
    }

    public void sendKeys(By locator, String value) {
        log.info("Locator : " + locator + " Value : " + value);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) element.sendKeys(value);
    }

    public void clear(By locator) {
        log.info("Locator : " + locator);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) element.clear();
    }

    public String getText(By locator) {
        log.info("Locator : " + locator);
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        return element != null ? element.getText() : null;
    }

    public void clearAndSendKeys(By locator, String value) {
        WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, locator);
        if (element != null) {
            element.clear();
            element.sendKeys(value);
        }
        log.info("Locator : " + locator + " Value : " + value);
    }
}
