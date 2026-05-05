package com.saucedemo.steps;

import com.saucedemo.domainobjects.Product;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TopNavigationLinksPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class CartAssertionSteps {
    private final PageManager pm;

    public CartAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private CartPage cartPage() {
        return this.pm.getPage(CartPage.class);
    }

    @Then("the quantity should be {string}")
    public void theQuantityShouldBe(String expectedQuantity) {
        String actualQuantity = cartPage().getItemQuantityByTitle(GlobalVarsHelper.getProductName1());
        Assert.assertEquals(actualQuantity, expectedQuantity, "Quantity does not match!");
    }

    @Then("the quantity of {string} should be {string}")
    public void theQuantityOfShouldBe(String productName, String expectedQuantity) {
        int actualQuantity = cartPage().getTotalItemsByTitle(productName);
        Assert.assertEquals(actualQuantity, Integer.parseInt(expectedQuantity), "Quantity does not match!");
    }

    @Then("the description should contain {string}")
    public void theDescriptionShouldContain(String expectedDescription) {
        String actualDescription = cartPage().getItemDescriptionByName(GlobalVarsHelper.getProductName2());
        Assert.assertTrue(actualDescription.contains(expectedDescription), "Description does not contain expected text!");
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String expectedProductName) {
        List<String> itemNames = cartPage().getAllItemNames();
        Assert.assertTrue(itemNames.contains(expectedProductName), "Cart does not contain the expected product!");
    }

    @Then("I should see {int} {product} in the cart")
    public void iShouldSeeInTheCart(int number, Product product) {
        Assert.assertTrue(
                cartPage().hasItemWithQuantityByName(product.getName(), number),
                "Expected " + number + " '" + product.getName() + "' in the cart, but it was not found."
        );
    }

    @And("I can see {string} button")
    public void iCanSeeButton(String contShoppingBtn) {
        String actualButtonText = cartPage().getContinueShoppingButton();
        Assert.assertTrue(actualButtonText.toLowerCase(java.util.Locale.ROOT).contains(contShoppingBtn.toLowerCase(java.util.Locale.ROOT)));
    }

    @And("I can see cart page management related controls:")
    public void iCanSeeCartPageManagementRelatedControls(DataTable controlsTable) {
        List<String> controls = controlsTable.asList(String.class);
        Assert.assertFalse(controls.isEmpty(), "Controls table must include at least one expected cart control.");

        for (String control : controls) {
            String normalizedControl = control == null ? "" : control.trim();
            switch (normalizedControl.toLowerCase(java.util.Locale.ROOT)) {
                case "checkout" -> Assert.assertEquals(
                        cartPage().getCheckoutButton().trim(),
                        "Checkout",
                        "Expected Checkout button to be visible on cart page.");
                case "continue shopping" -> Assert.assertEquals(
                        cartPage().getContinueShoppingButton().trim(),
                        "Continue Shopping",
                        "Expected Continue Shopping button to be visible on cart page.");
                case "remove" -> Assert.assertTrue(
                        cartPage().hasAnyRemoveButton(),
                        "Expected at least one Remove button to be visible on cart page.");
                default -> throw new IllegalArgumentException("Unsupported cart control expectation: " + normalizedControl);
            }
        }
    }

    @Then("I can see the following items in the cart")
    public void iCanSeeTheFollowingItemsInTheCart(DataTable dataTable) {
        if (!cartPage().isCartEmpty()) {
            System.out.println("Cart is not empty");
            return;
        }
        List<Map<String, String>> expectedEntries = dataTable.asMaps(String.class, String.class);

        if (expectedEntries.isEmpty()) {
            return;
        }
        for (Map<String, String> entry : expectedEntries) {
            String expectedDescription = entry.get("DESCRIPTION");
            String qtyString = entry.get("QTY");
            if (expectedDescription == null) {
                Assert.fail("DataTable is missing 'DESCRIPTION' column or its value is null for an entry. Found columns: " + entry.keySet());
                return;
            }
            if (qtyString == null) {
                Assert.fail("DataTable is missing 'QTY' column or its value is null for item: '" + expectedDescription + "'. Found columns: " + entry.keySet());
                return;
            }
            int expectedQuantity;
            try {
                expectedQuantity = Integer.parseInt(qtyString.trim());
            } catch (NumberFormatException e) {
                Assert.fail("Could not parse QTY value '" + qtyString + "' for item '" + expectedDescription + "' into an integer.");
                return;
            }

            Assert.assertTrue(cartPage().hasItemWithQuantityByName(expectedDescription, expectedQuantity),
                    "Item '" + expectedDescription + "' with quantity " + expectedQuantity + " was not found in the cart.");
        }
    }


    @And("I have a {product} in the cart")
    public void iHaveAProductInTheCart(Product product) {
        pm.getPage(InventoryPage.class).addProductToCart(product.getName());
        pm.getPage(TopNavigationLinksPage.class).clickCartIcon();
    }


    @And("there is already {product} in the cart")
    public void there_is_already_product_in_the_cart(Product product) {
        pm.getPage(InventoryPage.class).addProductToCart(product.getName());
        pm.getPage(TopNavigationLinksPage.class).clickCartIcon();
    }

}

