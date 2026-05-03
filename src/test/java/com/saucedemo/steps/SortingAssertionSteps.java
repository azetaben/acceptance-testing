package com.saucedemo.steps;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class SortingAssertionSteps {
    private final PageManager pm;

    public SortingAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private InventoryPage productsPage() {
        return this.pm.getPage(InventoryPage.class);
    }

    @Then("I can see {string} is selected by default")
    public void verifyDefaultSortOption(String expectedSortOption) {
        WebElement sortDropdown = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, By.className("product_sort_container"));
        Select sortSelect = new Select(sortDropdown != null ? sortDropdown : WebDrv.getInstance().getWebDriver().findElement(By.className("product_sort_container")));
        Assert.assertEquals(sortSelect.getFirstSelectedOption().getText(), expectedSortOption, "Default sort option is not as expected.");
    }

    @Then("I should see sorted result for {string}")
    public void i_should_see_sorted_result_for(String sortOption) {
        switch (sortOption) {
            case "Name (A to Z)":
                Assert.assertTrue(productsPage().areProductNamesSortedAtoZ());
                break;
            case "Name (Z to A)":
                Assert.assertTrue(productsPage().areProductNamesSortedZtoA());
                break;
            case "Price (high to low)":
                Assert.assertTrue(productsPage().arePricesSortedHighToLow());
                break;
            case "Price (low to high)":
                Assert.assertTrue(productsPage().arePricesSortedLowToHigh());
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sortOption);
        }
        System.out.println("Sorted by: " + sortOption);
    }

    @Then("I should see sorted result for {string} not matching option")
    public void i_should_see_sorted_result_for_not_matching_option(String sortOption) {
        switch (sortOption) {
            case "Name (A to Z)":
                Assert.assertTrue(productsPage().areProductNamesSortedAtoZ());
                break;
            case "Name (Z to A)":
                Assert.assertFalse(productsPage().areProductNamesSortedZtoA());
                break;
            case "Price (high to low)":
                Assert.assertFalse(productsPage().arePricesSortedHighToLow());
                break;
            case "Price (low to high)":
                Assert.assertFalse(productsPage().arePricesSortedLowToHigh());
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sortOption);
        }
        System.out.println("Sorted by: " + sortOption);
    }
}

