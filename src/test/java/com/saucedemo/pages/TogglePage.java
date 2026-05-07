package com.saucedemo.pages;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class TogglePage extends Page {
    private static final Logger log = LogManager.getLogger(TogglePage.class);

    @FindBy(css = ".bm-cross-button")
    private WebElement crossButton;

    @FindBy(css = "a.bm-item.menu-item")
    private List<WebElement> menuItems;

    public boolean isCloseMenuIconDisplayedAndEnabled() {
        return verificationHelper.isDisplayedAndEnabled(crossButton);
    }

    @Deprecated
    public boolean isReactCloseMenuIconDisplayedAndEnabled() {
        return isCloseMenuIconDisplayedAndEnabled();
    }

    public TopNavigationLinksPage closeMenu() {
        if (isCloseMenuIconDisplayedAndEnabled()) {
            crossButton.click();
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.ELEMENT_TO_BE_INVISIBLE, crossButton);
            log.info("Clicked on close button");
        } else {
            log.error("Close button is not displayed or enabled");
        }
        return PageManager.getInstance().getPage(TopNavigationLinksPage.class);
    }


    public TopNavigationLinksPage clickCrossButton() {
        return closeMenu();
    }

    public InventoryPage clickOnTopLeftMenuItem(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Menu item name must not be null or blank");
        }
        if (menuItems == null) {
            log.warn("Toggle menu items were not initialised (menuItems == null).");
            return PageManager.getInstance().getPage(InventoryPage.class);
        }

        menuItems.stream()
                .filter(webElement -> webElement.getText().contains(itemName))
                .findFirst()
                .ifPresentOrElse(WebElement::click, () -> log.warn("Toggle item not found: " + itemName));
        log.info("Clicked on the toggle item : " + itemName);
        return PageManager.getInstance().getPage(InventoryPage.class);
    }

    public String getTopLeftMenuItems() {
        log.info("Getting toggle menu items");
        if (menuItems == null) {
            return "";
        }
        return menuItems.stream().map(WebElement::getText).collect(Collectors.joining("\n"));

    }

}
