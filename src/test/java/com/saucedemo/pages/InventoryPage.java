package com.saucedemo.pages;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InventoryPage extends Page {
    private static final Logger log = LogManager.getLogger(InventoryPage.class);
    private static final int DEFAULT_WAIT_SECONDS = 10;
    private static final String INVENTORY_ITEM_NAME_CLASS = "inventory_item_name";
    private static final String INVENTORY_ITEM_PRICE_CLASS = "inventory_item_price";
    private static final String INVENTORY_ITEM_CLASS = "inventory_item";
    private static final String CURRENCY_SYMBOL = "$";
    private static final String BUTTON_LABEL_ADD_TO_CART = "Add to cart";
    private static final String BUTTON_LABEL_REMOVE = "Remove";
    private final By productListContainerLocator = By.className("inventory_list");

    @FindBy(css = "div.inventory_item button")
    List<WebElement> addToCartOrRemoveButtons;
    @FindBy(className = "inventory_item")
    private List<WebElement> productElements;
    @FindBy(xpath = "//span[@class='title' and text()='Products']")
    private WebElement productsPageHeader;
    @FindBy(className = "inventory_list")
    private WebElement inventoryList;
    @FindBy(css = ".product_label")
    private WebElement header;
    @FindBy(css = ".product_sort_container")
    private WebElement productSortButton;
    @FindBy(css = "div.inventory_item")
    private List<WebElement> products;
    @FindBy(css = "div.inventory_item div a div")
    private List<WebElement> productTitles;
    @FindBy(css = ".pricebar div")
    private List<WebElement> productPrices;
    @FindBy(css = "div.inventory_item div a")
    private List<WebElement> productImages;
    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    public WebElement getHeader() {
        return header;
    }

    public boolean isHeaderDisplayed() {
        return verificationHelper.isDisplayed(header);
    }

    public int getDisplayedProductCount() {
        try {
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, productListContainerLocator);
        } catch (Exception e) {
            log.info("Product list container not found. Assuming 0 products.");
            return 0;
        }
        return productElements.size();
    }

    public String getHeaderText() {
        return SauceDemoConstants.HEADING_PRODUCTS;

    }

    @Deprecated(forRemoval = true)
    public String getHeaderTxt() {
        return getHeaderText();

    }

    public ProductSorterPage clickProductSortButton() {
        WebElement button = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, productSortButton);
        if (button != null) {
            button.click();
        }
        return new ProductSorterPage();
    }

    @Deprecated(forRemoval = true)
    public void addMultiplyItemsToCart(List<String> data) {
        addMultipleItemsToCart(data);

    }

    public void addMultipleItemsToCart(List<String> data) {
        if (data == null || data.isEmpty()) {
            log.warn("No product data supplied for addMultipleItemsToCart");
            return;
        }

        Set<String> neededItems = Arrays.stream(data.get(0).split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toSet());

        int maxIndex = Math.min(productTitles.size(), addToCartOrRemoveButtons.size());
        IntStream.range(0, maxIndex)
                .filter(i -> neededItems.contains(productTitles.get(i).getText().trim()))
                .forEach(i -> {
                    WebElement button = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, addToCartOrRemoveButtons.get(i));
                    if (button != null) {
                        button.click();
                    }
                });
    }

    public void addProductToCart(String productName) {
        if (productName == null || productName.isBlank()) {
            log.warn("Product name is null/blank for add operation");
            return;
        }
        clickProductButton(productName, "add", BUTTON_LABEL_REMOVE);
    }

    public void removeProductFromTheCart(String productName) {
        if (productName == null || productName.isBlank()) {
            log.warn("Product name is null/blank for remove operation");
            return;
        }
        clickProductButton(productName, "remove", BUTTON_LABEL_ADD_TO_CART);
    }

    private void clickProductButton(String productName, String actionLabel, String expectedButtonLabelAfterClick) {
        try {
            WebElement productButton = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, getProductButtonLocator(productName, actionLabel));
            if (productButton == null) {
                throw new TimeoutException("Product button is not clickable for action: " + actionLabel);
            }
            productButton.click();
            ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, getProductButtonLocator(productName, expectedButtonLabelAfterClick));
        } catch (TimeoutException e) {
            log.warn("No product found to " + actionLabel + ": " + productName, e);
        }
    }

    private By getProductButtonLocator(String productName, String buttonLabel) {
        String normalizedAction = buttonLabel == null ? "" : buttonLabel.trim().toLowerCase(Locale.ROOT);
        String normalizedProductName = normalizeProductNameForDataTest(productName);
        if (normalizedAction.equals("add") || normalizedAction.equals(BUTTON_LABEL_ADD_TO_CART.toLowerCase(Locale.ROOT))) {
            return By.cssSelector("button[data-test='add-to-cart-" + normalizedProductName + "']");
        }
        if (normalizedAction.equals("remove") || normalizedAction.equals(BUTTON_LABEL_REMOVE.toLowerCase(Locale.ROOT))) {
            return By.cssSelector("button[data-test='remove-" + normalizedProductName + "']");
        }
        return By.xpath("//div[contains(@class,'inventory_item')][.//div[@class='" + INVENTORY_ITEM_NAME_CLASS + "' and normalize-space()=\""
                + productName.trim()
                + "\"]]//button[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')=\""
                + normalizedAction
                + "\"]");
    }

    private String normalizeProductNameForDataTest(String productName) {
        return productName.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "-");
    }

    private WebElement findProductButton(String productName) {
        return driver.findElements(By.className(INVENTORY_ITEM_CLASS)).stream()
                .filter(productElement -> productElement.findElement(By.className(INVENTORY_ITEM_NAME_CLASS))
                        .getText()
                        .trim()
                        .equals(productName.trim()))
                .findFirst()
                .map(productElement -> productElement.findElement(By.tagName("button")))
                .orElse(null);
    }

    private String getProductButtonText(String productName) {
        WebElement productButton = findProductButton(productName);
        if (productButton == null) {
            throw new IllegalStateException("No button found for product: " + productName);
        }
        return productButton.getText().trim();
    }

    public boolean isProductListDisplayed() {
        return productElements.stream().allMatch(verificationHelper::isDisplayed);
    }

    public long getProductCount() {
        log.info("Displayed products count: " + productElements.size());
        return productElements.size();
    }

    public long isProductCountGreaterThanZero() {
        log.info("Checking if product count is greater than zero");
        return getProductCount();

    }

    public List<String> getDisplayedProductNames() {
        List<WebElement> elements = ExplicitWaitFactory.performExplicitWaitForList(
                WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS, By.className(INVENTORY_ITEM_NAME_CLASS));
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getDisplayedPrices() {
        List<WebElement> elements = ExplicitWaitFactory.performExplicitWaitForList(
                WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS, By.className(INVENTORY_ITEM_PRICE_CLASS));
        return elements.stream()
                .map(e -> Double.parseDouble(e.getText().replace(CURRENCY_SYMBOL, "")))
                .collect(Collectors.toList());
    }

    public List<String> getDisplayedPriceTexts() {
        List<WebElement> elements = ExplicitWaitFactory.performExplicitWaitForList(
                WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS, By.className(INVENTORY_ITEM_PRICE_CLASS));
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean areProductNamesSortedAtoZ() {
        List<String> productNames = getDisplayedProductNames();
        List<String> sortedNames = productNames.stream().sorted().toList();
        log.info("ProductTitles equals sortedProductTitles" + productNames.equals(sortedNames));
        return productNames.equals(sortedNames);
    }

    public boolean areProductNamesSortedZtoA() {
        List<String> productNames = getDisplayedProductNames();
        List<String> sortedNames = productNames.stream().sorted((a, b) -> b.compareTo(a)).toList();
        log.info("ProductTitles equals sortedProductTitles" + productNames.equals(sortedNames));
        return productNames.equals(sortedNames);
    }

    public boolean arePricesSortedHighToLow() {
        List<Double> prices = getDisplayedPrices();
        boolean sorted = IntStream.range(0, Math.max(0, prices.size() - 1))
                .allMatch(i -> prices.get(i) >= prices.get(i + 1));
        if (!sorted) {
            log.info("Found a price that is lower than the next one");
        }
        return sorted;
    }

    public boolean arePricesSortedLowToHigh() {
        List<Double> prices = getDisplayedPrices();
        boolean sorted = IntStream.range(0, Math.max(0, prices.size() - 1))
                .allMatch(i -> prices.get(i) <= prices.get(i + 1));
        if (!sorted) {
            log.info("Found a price that is higher than the next one");
        }
        return sorted;
    }

    public boolean isPageHeaderVisible() {
        try {
            log.debug("Waiting for Products Page Header to be visible...");
            WebElement header = wait.until(ExpectedConditions.visibilityOf(productsPageHeader));
            boolean visible = header.isDisplayed();
            if (visible) {
                log.debug("Products Page Header is visible.");
            } else {
                log.warn("Products Page Header was found but reported as not displayed.");
            }
            return visible;
        } catch (Exception e) {
            log.error("Products Page Header not visible or not found. Current URL: " + getCurrentUrl(), e);
            return false;
        }
    }

    public WebElement getInventoryListElement() {
        return inventoryList;
    }

    public String getInventoryListOuterHtml() {
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].outerHTML;", inventoryList);
    }

    public By getInventoryListLocator() {
        return By.className("inventory_list");
    }

    public String getPageHeaderText() {
        if (isPageHeaderVisible()) {
            return productsPageHeader.getText();
        }
        return null;
    }

    public void selectSortOption(String option) {
        if (option == null || option.isBlank()) {
            throw new IllegalArgumentException("Sort option must not be null or blank");
        }
        WebElement dropdownElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, sortDropdown);
        Select dropdown = new Select(dropdownElement == null ? sortDropdown : dropdownElement);
        dropdown.selectByVisibleText(option);
    }

    public String getSelectedSortOptionText() {
        WebElement dropdownElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, sortDropdown);
        Select dropdown = new Select(dropdownElement == null ? sortDropdown : dropdownElement);
        return dropdown.getFirstSelectedOption().getText().trim();
    }

    public boolean isButtonDisplayedForProduct(String productName, String buttonLabel) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        if (buttonLabel == null || buttonLabel.isBlank()) {
            throw new IllegalArgumentException("Button label must not be null or blank");
        }
        try {
            WebElement button = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, getProductButtonLocator(productName, buttonLabel));
            return button != null && button.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasAnyProductButtonWithLabel(String buttonLabel) {
        if (buttonLabel == null || buttonLabel.isBlank()) {
            throw new IllegalArgumentException("Button label must not be null or blank");
        }
        List<WebElement> buttons = ExplicitWaitFactory.performExplicitWaitForList(
                WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS, By.cssSelector("div.inventory_item button"));
        String expectedLabel = buttonLabel.trim();
        return !buttons.isEmpty()
                && buttons.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .anyMatch(text -> text.equalsIgnoreCase(expectedLabel));
    }

    public String getProductImageSrcByName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }

        String trimmedName = productName.trim();
        By productCard = By.xpath("//div[contains(@class,'inventory_item')][.//div[contains(@class,'inventory_item_name') and normalize-space()=\""
                + trimmedName
                + "\"]]");
        WebElement cardElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.PRESENCE, productCard);
        if (cardElement == null) {
            return "";
        }

        WebElement image = cardElement.findElement(By.cssSelector("img.inventory_item_img, [data-test='inventory-item-img']"));
        String src = image.getDomAttribute("src");
        return src == null ? "" : src.trim();
    }
}
