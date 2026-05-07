package com.saucedemo.helperutilities.checkbox;


import com.saucedemo.helperutilities.logger.LoggerHelper;
import com.saucedemo.interfaces.IWebComponent;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckBoxOrRadioButtonHelper implements IWebComponent {
    private final Logger log = LoggerHelper.getLogger(CheckBoxOrRadioButtonHelper.class);
    private WebDriver driver;

    public CheckBoxOrRadioButtonHelper() {
        log.debug("CheckBoxOrRadioButtonHelper : " + driver.hashCode());
    }

    public void selectCheckBox(By locator) {
        log.info(locator);
        selectCheckBox(driver.findElement(locator));
    }

    public void unSelectCheckBox(By locator) {
        log.info(locator);
        unSelectCheckBox(driver.findElement(locator));
    }

    public boolean isIselected(By locator) {
        log.info(locator);
        return isIselected(driver.findElement(locator));
    }

    public boolean isIselected(WebElement element) {
        boolean flag = element.isSelected();
        log.info(flag);
        return flag;
    }

    public void selectCheckBox(WebElement element) {
        if (!isIselected(element)) element.click();
        log.info(element);
    }

    public void unSelectCheckBox(WebElement element) {
        if (isIselected(element)) element.click();
        log.info(element);
    }
}
