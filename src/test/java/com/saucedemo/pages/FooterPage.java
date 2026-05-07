package com.saucedemo.pages;

import com.saucedemo.helperutilities.javascript.ScrollPageHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class FooterPage extends Page {
    private static final Logger log = LogManager.getLogger(FooterPage.class);
    private static final By SOCIAL_LINKS = By.cssSelector(".social li");
    private static final By ROBOT_IMAGE = By.cssSelector(".footer_robot");

    @FindBy(css = ".social li")
    private List<WebElement> socialMediaLinks;

    @FindBy(css = ".footer_copy")
    private WebElement copyrightInfo;

    @FindBy(css = "img[alt='Pony Express']")
    private WebElement expressGoodGreenIcon;


    public List<String> getSocialMediaLinks() throws Exception {
        log.info("Getting social media links");
        new ScrollPageHelper().scrollToBottomOfThePage();
        List<WebElement> socialMediaElements = driver.findElements(SOCIAL_LINKS);
        return socialMediaElements.stream().map(WebElement::getText).toList();
    }

    public String getFooterRobotImageSrc() {
        WebElement robotImage = driver.findElement(ROBOT_IMAGE);
        return robotImage.getDomAttribute("src");
    }

    public boolean isExpressGoodGreenIcon() {
        return verificationHelper.isDisplayed(expressGoodGreenIcon);

    }

    public String getCopyrightInfo() {
        return verificationHelper.getText(copyrightInfo);

    }


}
