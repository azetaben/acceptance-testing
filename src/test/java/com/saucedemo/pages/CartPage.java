package com.saucedemo.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CartPage extends Page {
    private static final Logger log = LogManager.getLogger(CartPage.class);
    private static final String DEFAULT_QUANTITY = "0";
    private static final String DEFAULT_PRICE = "0.00";
    private static final String DEFAULT_DESCRIPTION = "No description available";

    private static final By CART_ITEM = By.className("cart_item");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_QUANTITY = By.className("cart_quantity");
    private static final By ITEM_PRICE = By.className("inventory_item_price");
    private static final By ITEM_DESCRIPTION = By.className("inventory_item_desc");
    private static final By REMOVE_BUTTON = By.className("btn_secondary");


    @FindBy(id = "cart_items")
    private WebElement cartItemsList;

    @FindBy(css = ".cart_info")
    private WebElement cartInfo;

    @FindBy(id = "empty_cart")
    private WebElement emptyCartMessage;

    @FindBy(id = "cart_contents_container")
    private WebElement cartContentsContainer;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "cart_quantity_label")
    private WebElement quantityLabel;

    @FindBy(className = "cart_desc_label")
    private WebElement descriptionLabel;

    @FindBy(className = "cart_footer")
    private WebElement cartFooter;

    @FindBy(css = ".btn_action.checkout_button")
    private WebElement checkoutButton;

    @FindBy(css = "#continue-shopping, [data-test='continue-shopping'], a.btn_secondary")
    private WebElement continueShoppingButton;

    public boolean isCartEmpty() {
        return emptyCartMessage.isDisplayed();

    }

    public CheckoutStepOnePage clickCheckoutButton() {
        checkoutButton.click();
        log.info("Clicked on Checkout button");
        return new CheckoutStepOnePage();
    }

    public String getItemQuantityByTitle(String productTitle) {
        if (productTitle == null || productTitle.isBlank()) {
            throw new IllegalArgumentException("Product title must not be null or blank");
        }
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productTitle))
                .map(item -> item.findElement(ITEM_QUANTITY).getText())
                .findFirst()
                .orElse(DEFAULT_QUANTITY);
    }

    public void removeItemByTitle(String productTitle) {
        if (productTitle == null || productTitle.isBlank()) {
            throw new IllegalArgumentException("Product title must not be null or blank");
        }
        cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productTitle))
                .findFirst()
                .ifPresent(item -> item.findElement(REMOVE_BUTTON).click());
    }

    public int getTotalItemsByTitle(String productTitle) {
        if (productTitle == null || productTitle.isBlank()) {
            throw new IllegalArgumentException("Product title must not be null or blank");
        }
        return (int) cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productTitle))
                .count();
    }

    public String getItemPriceByName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productName))
                .map(item -> item.findElement(ITEM_PRICE).getText())
                .findFirst()
                .orElse(DEFAULT_PRICE);
    }

    public String getItemDescriptionByName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().equals(productName))
                .map(item -> item.findElement(ITEM_DESCRIPTION).getText())
                .findFirst()
                .orElse(DEFAULT_DESCRIPTION);
    }

    public List<String> getAllItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(ITEM_NAME).getText())
                .collect(Collectors.toList());
    }

    public String getPriceOfItemByName(String productName) {
        return getItemPriceByName(productName);

    }

    public InventoryPage clickContinueShoppingButton() {
        continueShoppingButton.click();
        log.info("Clicked on Continue Shopping button");
        return new InventoryPage();
    }

    public int getCartItemCount() {
        log.info("Getting count of cart items.");
        return cartItems.size();

    }

    public boolean hasProductInCart(String productName, int expectedQuantity) {
        return hasItemWithQuantityByName(productName, expectedQuantity);
    }


    public String getContinueShoppingButton() {
        return verificationHelper.getText(continueShoppingButton);

    }

    public String getCheckoutButton() {
        return verificationHelper.getText(checkoutButton);
    }

    public boolean hasItemWithQuantityByName(String expectedItemName, int expectedQuantity) {
        log.info("Verifying cart item name and quantity.");

        List<WebElement> currentCartItemElements = driver.findElements(CART_ITEM);

        if (currentCartItemElements.isEmpty() && expectedQuantity > 0) {
            log.error("Verification failed: cart is empty, but expected item '" + expectedItemName + "' with quantity " + expectedQuantity + ".");
            return false;
        }

        for (WebElement cartItemElement : currentCartItemElements) {
            String actualItemName;
            String actualQuantityStr = "";
            int actualQuantity;

            try {

                WebElement itemNameElement = cartItemElement.findElement(ITEM_NAME);
                actualItemName = itemNameElement.getText().trim();

                WebElement quantityElement = cartItemElement.findElement(ITEM_QUANTITY);
                actualQuantityStr = quantityElement.getText().trim();
                actualQuantity = Integer.parseInt(actualQuantityStr);

                log.debug("Checking cart item: Name='" + actualItemName + "', Quantity='" + actualQuantity + "'");

                // Try exact match first
                if (actualItemName.equals(expectedItemName)) {
                    if (actualQuantity == expectedQuantity) {
                        log.info("Verified item '" + expectedItemName + "' with quantity " + expectedQuantity + ".");
                        return true;
                    } else {
                        log.error("Verification failed for item '" + expectedItemName + "': expected quantity " + expectedQuantity + ", but found " + actualQuantity + ".");
                        return false;
                    }
                }

                // Try flexible match (case-insensitive, whitespace-tolerant)
                String normalizedActual = normalizeProductName(actualItemName);
                String normalizedExpected = normalizeProductName(expectedItemName);

                if (normalizedActual.equals(normalizedExpected)) {
                    if (actualQuantity == expectedQuantity) {
                        log.info("Verified item '" + expectedItemName + "' with quantity " + expectedQuantity + " (using flexible matching).");
                        return true;
                    } else {
                        log.error("Verification failed for item '" + expectedItemName + "': expected quantity " + expectedQuantity + ", but found " + actualQuantity + ".");
                        return false;
                    }
                }
            } catch (NoSuchElementException e) {
                log.warn("A cart item element did not contain expected sub-elements (inventory_item_name or cart_quantity).", e);
            } catch (NumberFormatException e) {
                log.warn("Could not parse quantity string '" + actualQuantityStr + "' for item '" + expectedItemName + "' to an integer.", e);
            }
        }

        log.error("Verification failed: item '" + expectedItemName + "' with quantity " + expectedQuantity + " was not found in the cart.");
        logDebugCurrentCartContents(currentCartItemElements);
        return false;
    }

    private void logDebugCurrentCartContents(List<WebElement> cartItemElements) {
        if (cartItemElements.isEmpty()) {
            log.debug("Cart was empty during debug logging.");
        } else {
            log.debug("Current cart contents (for debugging failure):");
            for (WebElement itemElement : cartItemElements) {
                try {
                    String desc = itemElement.findElement(ITEM_NAME).getText().trim();
                    String qty = itemElement.findElement(ITEM_QUANTITY).getText().trim();
                    log.debug("- Item: " + desc + ", Qty: " + qty);
                } catch (Exception e) {
                    log.debug("- Malformed cart item found during debug logging", e);
                }
            }
        }
    }

    private String normalizeProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            return "";
        }
        // Convert to lowercase and replace common variations
        return productName.trim()
                .toLowerCase()
                .replaceAll("\\s+", " ") // normalize whitespace
                .replaceAll("[\\(\\)\\-\\u2013\\u2014]", " ") // replace parentheses and dashes with space
                .replaceAll("\\s+", " ") // normalize whitespace again
                .trim();
    }

}
