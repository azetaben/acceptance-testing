package com.saucedemo.steps;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.Page;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TopNavigationLinksPage;
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

    private CheckoutStepTwoPage checkoutOverviewPage() {
        return this.pm.getPage(CheckoutStepTwoPage.class);
    }

    private void ensureOnCartPage() {
        String currentUrl = this.pm.getPage(Page.class).getCurrentUrl();
        if (currentUrl != null && currentUrl.contains("cart.html")) {
            return;
        }
        this.pm.getPage(TopNavigationLinksPage.class).clickCartIcon();
    }

    @Given("I have the following items in the cart:")
    public void iHaveTheFollowingItemsInMyCart(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);
        Assert.assertFalse(items.isEmpty(), "Items table must include at least one product row.");

        if (isOnCheckoutOverviewPage()) {
            List<String> actualProductNames = checkoutOverviewPage().getItemNames();
            System.out.println("DEBUG: Actual products in checkout overview: " + actualProductNames);

            for (Map<String, String> item : items) {
                String productName = getRequiredValue(item, "DESCRIPTION");
                String quantity = getRequiredValue(item, "QTY");
                String expectedPrice = getOptionalValue(item, "Price", "PRICE", "price", "Price ($)", "$");

                Assert.assertTrue(actualProductNames.stream().anyMatch(actual -> actual != null && actual.trim().equals(productName.trim())),
                        "Product '" + productName + "' was not found in checkout overview. Available: " + actualProductNames);
                Assert.assertEquals(checkoutOverviewPage().getItemQuantityByName(productName).trim(), quantity.trim(),
                        "Quantity mismatch for product '" + productName + "' in checkout overview.");

                if (expectedPrice != null && !expectedPrice.isBlank()) {
                    Assert.assertEquals(normalizePriceText(checkoutOverviewPage().getItemPriceByName(productName)),
                            normalizePriceText(expectedPrice),
                            "Price mismatch for product '" + productName + "' in checkout overview.");
                }
            }
            return;
        }

        List<String> actualProductNames = cartPage().getAllItemNames();
        System.out.println("DEBUG: Actual products in cart: " + actualProductNames);

        for (Map<String, String> item : items) {
            String productName = getRequiredValue(item, "DESCRIPTION");
            String quantity = getRequiredValue(item, "QTY");
            String expectedPrice = getOptionalValue(item, "Price", "PRICE", "price", "Price ($)", "$");
            boolean found = cartPage().hasItemWithQuantityByName(productName, Integer.parseInt(quantity.trim()));

            if (!found) {
                System.out.println("DEBUG: Expected product '" + productName + "' not found.");
                System.out.println("DEBUG: Available products: " + actualProductNames);
            }

            Assert.assertTrue(found,
                    "Product '" + productName + "' with quantity " + quantity + " was not found in the cart. Available: " + actualProductNames);

            if (expectedPrice != null && !expectedPrice.isBlank()) {
                Assert.assertEquals(normalizePriceText(cartPage().getItemPriceByName(productName)),
                        normalizePriceText(expectedPrice),
                        "Price mismatch for product '" + productName + "' in the cart.");
            }
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

    @And("I click the first product item in the cart")
    public void iClickTheFirstProductItemInTheCart() {
        cartPage().clickFirstProductItemInCart();
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
        ensureOnCartPage();
        cartPage().clickCheckoutButton();
        return new CheckoutStepOnePage();
    }

    @And("click on {string} button")
    public CheckoutStepOnePage clickCheckoutButton(String checkoutBtn) {
        ensureOnCartPage();
        String actualButtonText = cartPage().getCheckoutButton();
        Assert.assertTrue(actualButtonText.toLowerCase(Locale.ROOT).contains(checkoutBtn.toLowerCase(Locale.ROOT)));
        cartPage().clickCheckoutButton();
        return new CheckoutStepOnePage();
    }

    @When("I check the description of {string}")
    public void iCheckTheDescriptionOf(String productDescription) {
        cartPage().getItemDescriptionByName(productDescription);
    }

    private boolean isOnCheckoutOverviewPage() {
        String currentUrl = this.pm.getPage(Page.class).getCurrentUrl();
        return currentUrl != null && currentUrl.contains("checkout-step-two");
    }

    private String getRequiredValue(Map<String, String> row, String... expectedKeys) {
        String value = getOptionalValue(row, expectedKeys);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required table column. Expected one of: " + String.join(", ", expectedKeys));
        }
        return value;
    }

    private String getOptionalValue(Map<String, String> row, String... expectedKeys) {
        for (Map.Entry<String, String> entry : row.entrySet()) {
            String actualKey = entry.getKey();
            if (actualKey == null) {
                continue;
            }

            String normalizedActualKey = actualKey.trim().toLowerCase(Locale.ROOT);
            for (String expectedKey : expectedKeys) {
                if (normalizedActualKey.equals(expectedKey.trim().toLowerCase(Locale.ROOT))) {
                    return entry.getValue() == null ? "" : entry.getValue().trim();
                }
            }
        }
        return null;
    }

    private String normalizePriceText(String rawPrice) {
        return rawPrice == null ? "" : rawPrice.replace("$", "").trim();
    }
}
