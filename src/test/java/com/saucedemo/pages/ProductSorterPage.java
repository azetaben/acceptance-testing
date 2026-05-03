package com.saucedemo.pages;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Set;

public class ProductSorterPage extends Page {
    private static final Logger log = LogManager.getLogger(ProductSorterPage.class);
    private static final Set<String> VALID_SORT_OPTIONS = Set.of(
            "name (a to z)",
            "name (z to a)",
            "price (low to high)",
            "price (high to low)"
    );

    public void sortProducts(String sortOption) {
        if (sortOption == null || sortOption.isBlank()) {
            throw new IllegalArgumentException("Sort option must not be null or blank");
        }
        WebElement sortDropdown = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, By.className("product_sort_container"));
        Select sortSelect = new Select(sortDropdown != null ? sortDropdown : WebDrv.getInstance().getWebDriver().findElement(By.className("product_sort_container")));
        if (isValidSortOption(sortOption)) {
            log.info("Selecting sort option: " + sortOption);
            sortSelect.selectByVisibleText(sortOption);
        } else {
            log.error("Invalid sort option: " + sortOption);
            throw new IllegalArgumentException("Invalid sort option: " + sortOption);
        }
    }

    private boolean isValidSortOption(String sortOption) {
        boolean valid = VALID_SORT_OPTIONS.contains(sortOption.toLowerCase());
        if (valid) {
            log.info("Valid sort option: " + sortOption);
        }
        return valid;
    }


}
