package com.saucedemo.pages;

import com.saucedemo.helperUtilities.elements.ElementHelper;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class ProductDetailsPage extends Page {
    private static final Logger log = LogManager.getLogger(ProductDetailsPage.class);
    private final ElementHelper elementHelper = new ElementHelper(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));

    // Locators
    private static final By BACK_TO_PRODUCTS_BUTTON = By.xpath("//button[@data-test='back-to-products']");
    private static final By PRODUCT_DETAILS_CONTAINER = By.xpath("//div[@data-test='inventory-item']");
    private static final By PRODUCT_IMAGE = By.xpath("//img[@data-test='item-sauce-labs-backpack-img']");
    private static final By PRODUCT_NAME = By.xpath("//div[@data-test='inventory-item-name']");
    private static final By PRODUCT_DESCRIPTION = By.xpath("//div[@data-test='inventory-item-desc']");
    private static final By PRODUCT_PRICE = By.xpath("//div[@data-test='inventory-item-price']");
    private static final By REMOVE_BUTTON = By.cssSelector("button[data-test*='remove'], button[id*='remove']");
    private static final By DETAIL_ACTION_BUTTONS = By.cssSelector(".inventory_details_desc_container button, .inventory_details_desc_container a");
    private static final By PRODUCT_IMAGE_CONTAINER = By.className("inventory_details_img_container");

    // WebElement Locators using @FindBy
    @FindBy(xpath = "//button[@data-test='back-to-products']")
    private WebElement backToProductsButton;

    @FindBy(xpath = "//div[@data-test='inventory-item']")
    private WebElement productDetailsContainer;

    @FindBy(xpath = "//img[@data-test='item-sauce-labs-backpack-img']")
    private WebElement productImage;

    @FindBy(xpath = "//div[@data-test='inventory-item-name']")
    private WebElement productName;

    @FindBy(xpath = "//div[@data-test='inventory-item-desc']")
    private WebElement productDescription;

    @FindBy(xpath = "//div[@data-test='inventory-item-price']")
    private WebElement productPrice;

    @FindBy(css = "button[data-test^='remove-'], button[id^='remove-']")
    private WebElement removeButton;

    // Assertion methods - Text verification
    public boolean isProductNameDisplayed() {
        log.info("Checking if product name is displayed");
        return isDisplayed(productName);
    }

    public String getProductName() {
        log.info("Getting product name text");
        return getText(productName);
    }

    public boolean assertProductName(String expectedName) {
        log.info("Asserting product name: " + expectedName);
        return verificationHelper.verifyTextEquals(productName, expectedName);
    }

    public boolean isProductDescriptionDisplayed() {
        log.info("Checking if product description is displayed");
        return isDisplayed(productDescription);
    }

    public String getProductDescription() {
        log.info("Getting product description text");
        return getText(productDescription);
    }

    public boolean assertProductDescription(String expectedDescription) {
        log.info("Asserting product description");
        return verificationHelper.verifyTextEquals(productDescription, expectedDescription);
    }

    public boolean isProductPriceDisplayed() {
        log.info("Checking if product price is displayed");
        return isDisplayed(productPrice);
    }

    public String getProductPrice() {
        log.info("Getting product price text");
        return getText(productPrice);
    }

    public boolean assertProductPrice(String expectedPrice) {
        log.info("Asserting product price: " + expectedPrice);
        return verificationHelper.verifyTextEquals(productPrice, expectedPrice);
    }

    public boolean isProductImageDisplayed() {
        log.info("Checking if product image is displayed");
        return isDisplayed(productImage);
    }

    public String getProductImageAlt() {
        log.info("Getting product image alt text");
        return getAttribute(productImage, "alt");
    }

    public boolean assertProductImageAlt(String expectedAlt) {
        log.info("Asserting product image alt text: " + expectedAlt);
        String actualAlt = getProductImageAlt();
        log.info("Expected alt: " + expectedAlt + ", Actual alt: " + actualAlt);
        return actualAlt != null && actualAlt.equals(expectedAlt);
    }

    public String getProductImageSource() {
        log.info("Getting product image source");
        return getAttribute(productImage, "src");
    }

    public boolean isProductDetailsContainerDisplayed() {
        log.info("Checking if product details container is displayed");
        return isDisplayed(productDetailsContainer);
    }

    public boolean isBackToProductsButtonDisplayed() {
        log.info("Checking if back to products button is displayed");
        return isDisplayed(backToProductsButton);
    }

    public boolean isRemoveButtonDisplayed() {
        log.info("Checking if remove button is displayed");
        List<WebElement> buttons = findRemoveButtons();
        return !buttons.isEmpty() && buttons.stream().anyMatch(WebElement::isDisplayed);
    }

    // Clickable methods
    public InventoryPage clickBackToProducts() {
        log.info("Clicking on Back to Products button");
        waitAndClick(backToProductsButton);
        return PageManager.getInstance().getPage(InventoryPage.class);
    }

    public void clickRemoveButton() {
        log.info("Clicking on Remove button");
        List<WebElement> buttons = findRemoveButtons();
        if (buttons.isEmpty()) {
            throw new org.openqa.selenium.NoSuchElementException("Remove button was not found on inventory item page.");
        }
        waitAndClick(buttons.get(0));
    }

    // Text presence checks
    public boolean isProductNamePresent(String productName) {
        log.info("Checking if product name is present: " + productName);
        return getProductName().contains(productName);
    }

    public boolean isProductDescriptionPresent(String description) {
        log.info("Checking if product description is present: " + description);
        return getProductDescription().contains(description);
    }

    public boolean isProductPricePresent(String price) {
        log.info("Checking if product price is present: " + price);
        return getProductPrice().contains(price);
    }

    // Verify all elements are displayed
    public boolean areAllProductDetailsDisplayed() {
        log.info("Verifying all product details are displayed");
        return isProductDetailsContainerDisplayed()
                && isProductImageDisplayed()
                && isProductNameDisplayed()
                && isProductDescriptionDisplayed()
                && isProductPriceDisplayed()
                && isRemoveButtonDisplayed()
                && isBackToProductsButtonDisplayed();
    }

    // Verify all buttons are enabled
    public boolean isRemoveButtonEnabled() {
        log.info("Checking if remove button is enabled");
        List<WebElement> buttons = findRemoveButtons();
        return !buttons.isEmpty() && buttons.get(0).isEnabled();
    }

    public boolean isBackToProductsButtonEnabled() {
        log.info("Checking if back to products button is enabled");
        return isEnabled(backToProductsButton);
    }

    // Get element states for detailed assertions
    public boolean isRemoveButtonClickable() {
        log.info("Checking if remove button is clickable");
        List<WebElement> buttons = findRemoveButtons();
        return !buttons.isEmpty() && isElementClickable(buttons.get(0));
    }

    private List<WebElement> findRemoveButtons() {
        List<WebElement> buttons = driver.findElements(REMOVE_BUTTON);
        if (!buttons.isEmpty()) {
            return buttons;
        }

        List<WebElement> candidateButtons = driver.findElements(DETAIL_ACTION_BUTTONS);
        return candidateButtons.stream()
                .filter(WebElement::isDisplayed)
                .filter(button -> {
                    String text = button.getText() == null ? "" : button.getText().trim().toLowerCase(Locale.ROOT);
                    String dataTest = button.getDomAttribute("data-test");
                    String id = button.getDomAttribute("id");
                    String normalizedDataTest = dataTest == null ? "" : dataTest.toLowerCase(Locale.ROOT);
                    String normalizedId = id == null ? "" : id.toLowerCase(Locale.ROOT);
                    return "remove".equals(text)
                            || normalizedDataTest.contains("remove")
                            || normalizedId.contains("remove");
                })
                .toList();
    }

    public boolean isBackToProductsButtonClickable() {
        log.info("Checking if back to products button is clickable");
        return isElementClickable(backToProductsButton);
    }

}
