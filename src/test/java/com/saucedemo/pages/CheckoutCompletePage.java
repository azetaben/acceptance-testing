package com.saucedemo.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Locale;

public class CheckoutCompletePage extends Page {
    private static final Logger log = LogManager.getLogger(CheckoutCompletePage.class);
    private static final By SOCIAL_CONTROLS = By.cssSelector(".social li");

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

    public String getPonyExpressImageSrc() {
        String src = ponyGoogGreenImage.getDomAttribute("src");
        if (src == null || src.isBlank()) {
            src = ponyImage.getDomAttribute("src");
        }
        return src == null ? "" : src.trim();
    }

    public String getSuccessGoodbyeMessagesText() {
        return ((getCompleteHeader() == null ? "" : getCompleteHeader())
                + "\n"
                + (getCompleteText() == null ? "" : getCompleteText())).trim();
    }

    public void clickBackHomeButton() {
        click(backButton);
    }

    public boolean isBackHomeControlDisplayed() {
        return verificationHelper.isDisplayed(backButton);
    }

    public boolean hasSocialControl(String controlName) {
        if (controlName == null || controlName.isBlank()) {
            return false;
        }

        List<WebElement> controls = driver.findElements(SOCIAL_CONTROLS);
        String expected = controlName.trim().toLowerCase(Locale.ROOT);
        return controls.stream()
                .map(WebElement::getText)
                .filter(text -> text != null)
                .map(text -> text.trim().toLowerCase(Locale.ROOT))
                .anyMatch(actual -> actual.equals(expected));
    }

    public boolean containsCheckoutCompleteMessage(String expectedMessagePart) {
        if (expectedMessagePart == null || expectedMessagePart.isBlank()) {
            return false;
        }
        String actual = getCompleteText();
        return actual != null && actual.toLowerCase(Locale.ROOT).contains(expectedMessagePart.trim().toLowerCase(Locale.ROOT));
    }
}
