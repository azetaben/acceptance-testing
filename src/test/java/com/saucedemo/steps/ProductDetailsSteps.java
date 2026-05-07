package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.ProductDetailsPage;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Locale;

public class ProductDetailsSteps {
    private static final Logger log = LogManager.getLogger(ProductDetailsSteps.class);
    private final PageManager pm;

    public ProductDetailsSteps() {
        this.pm = PageManager.getInstance();
    }

    private ProductDetailsPage productDetailsPage() {
        return this.pm.getPage(ProductDetailsPage.class);
    }

    @When("I tap on the {string} link")
    public void iTapOnTheLink(String linkName) {
        String normalizedLinkName = linkName == null ? "" : linkName.trim().toLowerCase(Locale.ROOT);
        if ("back to products".equals(normalizedLinkName) || "continue shopping".equals(normalizedLinkName)) {
            productDetailsPage().clickBackToProducts();
        } else {
            throw new IllegalArgumentException("Unsupported inventory item page link: " + linkName);
        }
    }

}
