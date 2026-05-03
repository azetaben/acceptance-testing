package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.ProductSorterPage;
import io.cucumber.java.en.When;

public class SortingSteps {
    private final PageManager pm;

    public SortingSteps() {
        this.pm = PageManager.getInstance();
    }

    private ProductSorterPage productSorterPage() {
        return this.pm.getPage(ProductSorterPage.class);
    }

    @When("I select price option {string} from the sort drop down list")
    public void selectPriceOption(String sortOption) {
        productSorterPage().sortProducts(sortOption);
    }

    @When("I select {string} from the sort drop down list")
    public void selectSortOption(String sortOption) {
        productSorterPage().sortProducts(sortOption);
    }
}
