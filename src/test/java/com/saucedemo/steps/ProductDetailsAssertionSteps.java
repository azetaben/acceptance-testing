package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.ProductDetailsPage;
import com.saucedemo.utils.ScreenshotUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;
import java.util.Locale;

public class ProductDetailsAssertionSteps {
    private final PageManager pm;
    protected ProductDetailsPage productDetailsPage;
    protected WebDriver driver = WebDrv.getInstance().getWebDriver();

    public ProductDetailsAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private ProductDetailsPage productDetailsPage() {
        return this.pm.getPage(ProductDetailsPage.class);
    }

    @And("I can see inventory items page management related controls:")
    public void iCanSeeInventoryItemsPageManagementRelatedControls(DataTable controlsTable) {
        List<String> controls = controlsTable.asList(String.class);
        Assert.assertFalse(controls.isEmpty(), "Controls table must include at least one expected inventory-item control.");

        for (String control : controls) {
            String normalizedControl = control == null ? "" : control.trim();
            switch (normalizedControl.toLowerCase(Locale.ROOT)) {
                case "remove" -> Assert.assertTrue(
                        productDetailsPage().isRemoveButtonDisplayed(),
                        "Expected Remove button to be visible on inventory item page.");
                case "continue shopping" -> Assert.assertTrue(
                        productDetailsPage().isBackToProductsButtonDisplayed(),
                        "Expected Continue Shopping control (Back to products) to be visible on inventory item page.");
                default -> throw new IllegalArgumentException("Unsupported inventory item control expectation: " + normalizedControl);
            }
        }
    }

    @And("I can see the product details:")
    public void iCanSeeTheProductDetails(DataTable productDetailsTable) {
        List<List<String>> rows = productDetailsTable.asLists(String.class);
        Assert.assertFalse(rows.isEmpty(), "Product details table must include at least one row.");

        for (List<String> row : rows) {
            Assert.assertTrue(row.size() >= 2, "Each product details row must contain exactly two columns: field and expected value.");

            String field = row.get(0) == null ? "" : row.get(0).trim();
            String expectedValue = row.get(1) == null ? "" : row.get(1).trim();
            String normalizedField = normalizeProductDetailsField(field);

            switch (normalizedField) {
                case "name" -> Assert.assertEquals(
                        productDetailsPage().getProductName().trim(),
                        expectedValue,
                        "Product name did not match on inventory item page.");
                case "description" -> Assert.assertTrue(
                        productDetailsPage().getProductDescription().trim().contains(expectedValue),
                        "Product description did not match on inventory item page.");
                case "price" -> Assert.assertEquals(
                        normalizePriceText(productDetailsPage().getProductPrice()),
                        normalizePriceText(expectedValue),
                        "Product price did not match on inventory item page.");
                default -> throw new IllegalArgumentException("Unsupported product details field: " + field);
            }

            ScreenshotUtil.takeScreenshotAsBase64(driver);
        }
    }

    private String normalizeProductDetailsField(String rawField) {
        String normalized = rawField == null ? "" : rawField.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("price") || normalized.equals("price ($)") || normalized.equals("$")) {
            return "price";
        }
        return normalized;
    }

    private String normalizePriceText(String rawPrice) {
        return rawPrice == null ? "" : rawPrice.replace("$", "").trim();
    }


}

