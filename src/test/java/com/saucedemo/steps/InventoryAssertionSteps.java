package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @Then("add to cart button text should change to {string}")
    public void addToCartButtonTextShouldChangeTo(String expectedButtonText) {
        Assert.assertTrue(productsPage().hasAnyProductButtonWithLabel(expectedButtonText),
                "Expected at least one product action button to show label '" + expectedButtonText + "'.");
    }

    @Then("I can see products displayed in the page is greater than {string}")
    public void iCanSeeProductsDisplayedIsGreaterThan(String expectedMinimumCountStr) {
        int expectedMinimumCount = Integer.parseInt(expectedMinimumCountStr);
        int actualProductCount = productsPage().getDisplayedProductCount();
        Assert.assertTrue(actualProductCount > expectedMinimumCount,
                "Expected product count to be greater than " + expectedMinimumCount + ", but found " + actualProductCount + " products.");
    }

    @And("I can see inventory page management related controls:")
    public void iCanSeeInventoryPageManagementRelatedControls(DataTable controlsTable) {
        List<Map<String, String>> rows = controlsTable.asMaps(String.class, String.class);
        Assert.assertFalse(rows.isEmpty(), "Controls table must include at least one expected control.");

        ensureInventoryPageContext();

        InventoryPage inventoryPage = productsPage();
        List<String> displayedPriceTexts = inventoryPage.getDisplayedPriceTexts();

        for (Map<String, String> row : rows) {
            String control = row.get("control");
            Assert.assertNotNull(control, "Controls table must include a 'control' column.");
            String normalized = control.trim();

            switch (normalized) {
                case "Name (A to Z)" -> Assert.assertEquals(
                        inventoryPage.getSelectedSortOptionText(),
                        "Name (A to Z)",
                        "Expected default sort option to be 'Name (A to Z)'."
                );
                case "Add to cart" -> Assert.assertTrue(
                        inventoryPage.hasAnyProductButtonWithLabel("Add to cart"),
                        "Expected to find at least one 'Add to cart' button on inventory page."
                );
                case "$" -> Assert.assertTrue(
                        !displayedPriceTexts.isEmpty() && displayedPriceTexts.stream().allMatch(text -> text.contains("$")),
                        "Expected all displayed inventory prices to contain '$'."
                );
                default -> throw new IllegalArgumentException("Unsupported inventory control expectation: " + normalized);
            }
        }
    }

    @And("I verify the inventory details images in the inventory page:")
    public void iVerifyTheInventoryDetailsImagesInTheInventoryPage(DataTable imageDetailsTable) {
        List<Map<String, String>> rows = imageDetailsTable.asMaps(String.class, String.class);
        Assert.assertFalse(rows.isEmpty(), "Image details table must include at least one expected row.");

        ensureInventoryPageContext();
        InventoryPage inventoryPage = productsPage();
        List<String> availableProductNames = inventoryPage.getDisplayedProductNames();

        for (Map<String, String> row : rows) {
            String productName = getRequiredColumnValue(row, "productName");
            String expectedImageSrc = getRequiredColumnValue(row, "imageSrc");

            Assert.assertTrue(availableProductNames.stream().anyMatch(name -> name != null && name.trim().equals(productName.trim())),
                    "Product '" + productName + "' not found on inventory page. Available: " + availableProductNames);

            String actualImageSrc = inventoryPage.getProductImageSrcByName(productName);
            Assert.assertEquals(normalizeImageSrcForComparison(actualImageSrc), normalizeImageSrcForComparison(expectedImageSrc),
                    "Image src mismatch for product '" + productName + "'. expected [" + expectedImageSrc + "] but found [" + actualImageSrc + "]");
        }
    }

    private void ensureInventoryPageContext() {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        if (currentUrl != null && currentUrl.contains("inventory.html")) {
            return;
        }

        URI uri = URI.create(currentUrl == null || currentUrl.isBlank() ? "https://www.saucedemo.com/" : currentUrl);
        String scheme = uri.getScheme() == null ? "https" : uri.getScheme();
        String host = uri.getHost() == null ? "www.saucedemo.com" : uri.getHost();
        WebDrv.getInstance().getWebDriver().navigate().to(scheme + "://" + host + "/inventory.html");
    }

    private String getRequiredColumnValue(Map<String, String> row, String expectedColumnName) {
        for (Map.Entry<String, String> entry : row.entrySet()) {
            String actualKey = entry.getKey();
            if (actualKey == null) {
                continue;
            }

            if (actualKey.trim().toLowerCase(Locale.ROOT).equals(expectedColumnName.trim().toLowerCase(Locale.ROOT))) {
                String value = entry.getValue() == null ? "" : entry.getValue().trim();
                if (value.isBlank()) {
                    throw new IllegalArgumentException("Column '" + expectedColumnName + "' must not be blank.");
                }
                return value;
            }
        }

        throw new IllegalArgumentException("Missing required column: " + expectedColumnName);
    }

    private String normalizeImageSrcForComparison(String rawSrc) {
        if (rawSrc == null || rawSrc.isBlank()) {
            return "";
        }

        String trimmed = rawSrc.trim();
        String pathOnly = trimmed;
        try {
            URI uri = URI.create(trimmed);
            if (uri.getScheme() != null && uri.getPath() != null && !uri.getPath().isBlank()) {
                pathOnly = uri.getPath();
            }
        } catch (Exception ignored) {
            // Keep the original value when the src is not a valid URI.
        }

        String normalized = pathOnly.replace('\\', '/').trim().toLowerCase(Locale.ROOT);
        return normalized.replaceFirst("(?i)(\\.[a-f0-9]{6,})(\\.[a-z0-9]+)$", "$2");
    }
}

