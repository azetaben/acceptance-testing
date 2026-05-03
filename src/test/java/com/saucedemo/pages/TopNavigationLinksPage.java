package com.saucedemo.pages;

import com.saucedemo.helperUtilities.elements.ElementHelper;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;

public class TopNavigationLinksPage extends Page {
    private static final Logger log = LogManager.getLogger(TopNavigationLinksPage.class);
    ElementHelper elementHelper = new ElementHelper(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));

    @FindBy(css = "span.shopping_cart_badge")
    private WebElement shoppingCartCounterBadge;

    @FindBy(css = "span.shopping_cart_badge")
    private List<WebElement> shoppingCartCounterBadgesList;

    @FindBy(css = "a.shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".peek")
    private WebElement logoPeekIcon;

    @FindBy(css = "div[class='bm-burger-button'] button")
    private WebElement toggleButton;


    public WebElement getLogoPeekIcon() {
        return logoPeekIcon;
    }

    public boolean isMenuIconDisplayed() {
        return verificationHelper.isDisplayed(toggleButton);

    }

    public TogglePage clickMenuIcon() {
        toggleButton.click();
        elementHelper.waitForDuration(GlobalVarsHelper.ONE);
        log.info("Clicked on the toggle button");
        return PageManager.getInstance().getPage(TogglePage.class);
    }

    public String getShoppingCartCounterText() {
        elementHelper.waitForDuration(GlobalVarsHelper.ONE);
        try {
            // Use the @FindBy element
            return verificationHelper.getText(shoppingCartCounterBadge);
        } catch (NoSuchElementException e) {
            log.warn("Shopping cart counter badge element not found when trying to get text. Assuming cart is empty.");
            return "";
        }
    }

    public boolean isShoppingCartCounterBadgeDisplayed() {
        try {
            return verificationHelper.isDisplayed(shoppingCartCounterBadge);
        } catch (NoSuchElementException e) {
            log.info("Shopping cart counter badge element not found (isDisplayed check), thus not displayed.");
            return false;
        }
    }

    public boolean isCartIconDisplayed() {
        return verificationHelper.isDisplayed(cartIcon);
    }

    public CartPage clickCartIcon() {
        cartIcon.click();
        log.info("Clicked cart icon");
        return new CartPage();
    }

    public boolean isCartEmpty() {
        boolean cartEmpty = shoppingCartCounterBadgesList.isEmpty();
        if (cartEmpty) {
            log.info("Cart is empty (no counter badge found in DOM via list check).");
        } else {
            log.info("Cart is not empty. Found " + shoppingCartCounterBadgesList.size() + " counter badge element(s).");
        }
        return cartEmpty;
    }

    public boolean isCartEmptyByExpectingNoSuchElement() {
        return isCartEmpty();
    }

}


