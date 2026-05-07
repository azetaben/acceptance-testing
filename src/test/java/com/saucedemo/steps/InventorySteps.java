package com.saucedemo.steps;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.util.List;

public class InventorySteps {
    private final PageManager pm;


    public InventorySteps() {
        this.pm = PageManager.getInstance();
    }

    private InventoryPage productsPage() {
        return this.pm.getPage(InventoryPage.class);
    }


    @Given("I add following product(s) to cart:")
    public void iAddFollowingProductsToCart(DataTable dataTable) {
        List<String> products = dataTable.asList(String.class);
        for (String product : products) {
            productsPage().addProductToCart(product);
        }
    }

    @And("I add a product {string} to the cart")
    public void iAddAProductToTheCart(String productTitle) {
        productsPage().addProductToCart(productTitle);
    }

    @And("I remove following products from the cart:")
    public void iRemoveFollowingProductsFromTheCart(DataTable dataTable) {
        List<String> products = dataTable.asList(String.class);
        for (String product : products) {
            productsPage().removeProductFromTheCart(product);
        }
    }
}
