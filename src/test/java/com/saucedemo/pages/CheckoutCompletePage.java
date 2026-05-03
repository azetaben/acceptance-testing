package com.saucedemo.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutCompletePage extends Page {
    private static final Logger log = LogManager.getLogger(CheckoutCompletePage.class);

    @FindBy(css = ".complete-header")
    private WebElement completeHeader;

    @FindBy(css = ".complete-text")
    private WebElement completeText;

    @FindBy(css = "img[alt='Pony Express']")
    private WebElement ponyGoogGreenImage;

    @FindBy(className = "pony_express")
    private WebElement ponyImage;

    @FindBy(css = "#back-to-products")
    private WebElement backButton;

    public boolean isCheckoutCompleteTextDisplayed() {
        log.info("Checking checkout completion text visibility");
        return verificationHelper.isDisplayed(completeText);
    }

    public String getHeaderText() {
        log.info("Reading checkout finish header text");
        return verificationHelper.getText(completeHeader);
    }

    public String getCompleteText() {
        log.info("Reading checkout complete message text");
        return verificationHelper.getText(completeText);
    }


    public String getCompleteHeader() {
        return verificationHelper.getText(completeHeader);

    }

    public boolean isGoodGreenImageDisplayed() {
        return verificationHelper.isDisplayed(ponyGoogGreenImage);

    }

    public String getFinishButtonText() {
        return verificationHelper.getText(completeHeader);

    }

    public String getBackButtonText() {
        return verificationHelper.getText(backButton);

    }
}
