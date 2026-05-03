package com.saucedemo.steps;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartSteps {
    private final PageManager pm;

    public CartSteps() {
        this.pm = PageManager.getInstance();
    }

    private CartPage cartPage() {
        return this.pm.getPage(CartPage.class);
    }

    @Given("I have the following items in the cart:")
    public void iHaveTheFollowingItemsInMyCart(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);
        List<String> actualProductNames = cartPage().getAllItemNames();
        System.out.println("DEBUG: Actual products in cart: " + actualProductNames);

        for (Map<String, String> item : items) {
            String productName = item.get("DESCRIPTION");
            String quantity = item.get("QTY");
            boolean found = cartPage().hasItemWithQuantityByName(productName, Integer.parseInt(quantity));

            if (!found) {
                System.out.println("DEBUG: Expected product '" + productName + "' not found.");
                System.out.println("DEBUG: Available products: " + actualProductNames);
            }

            Assert.assertTrue(found,
                    "Product '" + productName + "' with quantity " + quantity + " was not found in the cart. Available: " + actualProductNames);
        }
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromTheCart(String productName) {
        cartPage().removeItemByTitle(productName);
    }

    @When("Check the quantity of {string}")
    public void iCheckTheQuantityOfProducts(String productName) {
        cartPage().getItemQuantityByTitle(productName);
    }

    @When("I get all item names in the cart")
    public void iGetAllItemNamesInTheCart() {
        cartPage().getAllItemNames();
    }

    @And("I tap on continue shopping button")
    public void iTapOnContinueShoppingButton() {
        cartPage().clickContinueShoppingButton();
    }

    @And("I tap on {string} button")
    public void iTapOnContinueShoppingButton(String continueShoppingBtn) {
        String actualButtonText = cartPage().getContinueShoppingButton();
        Assert.assertTrue(actualButtonText.toLowerCase(Locale.ROOT).contains(continueShoppingBtn.toLowerCase(Locale.ROOT)));
        cartPage().clickContinueShoppingButton();
    }

    @And("click on checkout button")
    public CheckoutStepOnePage clickOnCheckoutButton() {
        cartPage().clickCheckoutButton();
        return new CheckoutStepOnePage();
    }

    @And("click on {string} button")
    public CheckoutStepOnePage clickCheckoutButton(String checkoutBtn) {
        String actualButtonText = cartPage().getCheckoutButton();
        Assert.assertTrue(actualButtonText.toLowerCase(Locale.ROOT).contains(checkoutBtn.toLowerCase(Locale.ROOT)));
        cartPage().clickCheckoutButton();
        return new CheckoutStepOnePage();
    }

    @When("I check the description of {string}")
    public void iCheckTheDescriptionOf(String productDescription) {
        cartPage().getItemDescriptionByName(productDescription);
    }
}
