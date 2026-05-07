package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TopNavigationLinksPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class TopNavigationAssertionSteps {
    private final PageManager pm;

    public TopNavigationAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private TopNavigationLinksPage topNaviLinksPage() {
        return this.pm.getPage(TopNavigationLinksPage.class);
    }

    @And("I can see {string} items in the cart")
    public void iCanSeeItemsInTheCart(String itemsCount) {
        Assert.assertTrue(topNaviLinksPage().getShoppingCartCounterText().contains(itemsCount), "Shopping Cart is not displayed");
    }

    @And("I can see {int} item(s) in the cart")
    public void iCanSeeItemsInTheCart(int itemsCount) {
        Assert.assertTrue(topNaviLinksPage().getShoppingCartCounterText().contains(String.valueOf(itemsCount)), "Shopping Cart is not displayed");
    }


    @Then("Cart should be empty")
    public void cartIsEmpty() {
        Assert.assertTrue(topNaviLinksPage().isCartEmpty(), "Expected cart to be empty, but a cart badge was found.");
        Assert.assertFalse(topNaviLinksPage().isShoppingCartCounterBadgeDisplayed(), "Expected cart badge to be absent for an empty cart.");
    }

    @Then("cart should be empty {string}")
    public void cart_should_be_empty(String arg0) {
        Assert.assertTrue(topNaviLinksPage().isCartEmpty(), "Expected cart to be empty, but a cart badge was found.");
        Assert.assertEquals(arg0, SauceDemoConstants.EMPTY_CART_COUNT, "Expected cart to be represented as '0'.");
        Assert.assertFalse(topNaviLinksPage().isShoppingCartCounterBadgeDisplayed(), "Expected cart badge to be absent for an empty cart.");
    }
}
