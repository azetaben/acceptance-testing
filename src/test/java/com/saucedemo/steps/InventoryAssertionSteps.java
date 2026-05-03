package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.InventoryPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class InventoryAssertionSteps {
    private final PageManager pm;

    public InventoryAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private InventoryPage productsPage() {
        return this.pm.getPage(InventoryPage.class);
    }

    @Then("I can see {string} displayed")
    public void iCanSeeDisplayed(String header) {
        Assert.assertEquals(header, productsPage().getHeaderText(), "Header is not matching");
    }

    @Then("I can see product page header {string} displayed")
    public void iCanSeeProductPageHeaderDisplayed(String header) {
        Assert.assertEquals(header, productsPage().getHeaderText(), "Header is not matching");
    }

    @Then("I should see {int} product(s) displayed")
    public void iShouldSeeProductsAreDisplayed(int expectedCount) {
        Assert.assertEquals(productsPage().getProductCount(), expectedCount, "The number of products displayed does not match the expected count.");
    }

    @Then("Product count is greater than {int}")
    public void iShouldSeeThatTheProductCountIsGreaterThanZero(int counter) {
        Assert.assertTrue(productsPage().isPageHeaderVisible(),
                "The Products page is not visible. Ensure login/navigation reaches inventory before checking product count.");
        long actualProductCount = productsPage().isProductCountGreaterThanZero();
        Assert.assertTrue(actualProductCount > counter,
                "Expected product count to be greater than " + counter + ", but found " + actualProductCount + ".");
    }

    @Then("I should see a list of products")
    public void iShouldSeeAListOfProducts() {
        Assert.assertTrue(productsPage().isProductListDisplayed(), "Product list is not displayed.");
    }

    @And("I can see {string} button for product {string}")
    public void iCanSeeButtonForProduct(String removeButton, String productName) {
        Assert.assertTrue(productsPage().getDisplayedProductNames().contains(productName), "Product with title '" + productName + "' was not found in the list.");
        Assert.assertTrue(productsPage().isButtonDisplayedForProduct(productName, removeButton), "Button '" + removeButton + "' was not found for product '" + productName + "'.");
    }

    @Then("I can see products displayed in the page is greater than {string}")
    public void iCanSeeProductsDisplayedIsGreaterThan(String expectedMinimumCountStr) {
        int expectedMinimumCount = Integer.parseInt(expectedMinimumCountStr);
        int actualProductCount = productsPage().getDisplayedProductCount();
        Assert.assertTrue(actualProductCount > expectedMinimumCount,
                "Expected product count to be greater than " + expectedMinimumCount + ", but found " + actualProductCount + " products.");
    }
}

