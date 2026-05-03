package com.saucedemo.pages.general;

import com.saucedemo.pages.Page;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ApplicationFlowPageImpl extends Page implements ApplicationFlowPage {
    private static final Logger log = LogManager.getLogger(ApplicationFlowPageImpl.class);

    @FindBy(how = How.ID, using = "job_title")
    private WebElement jobTitle;

    @Override
    public boolean isElementDisplayed(String elementName) {
        if (elementName == null) {
            log.warn("isElementDisplayed called with null elementName");
            return false;
        }

        if (elementName.equalsIgnoreCase("job title")) {
            boolean displayed = jobTitle.isDisplayed();
            log.info("Job title element displayed: " + displayed);
            return displayed;
        }

        log.warn("Unsupported element name: " + elementName);
        return false;
    }
}
