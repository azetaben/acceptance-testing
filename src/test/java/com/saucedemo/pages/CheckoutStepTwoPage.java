package com.saucedemo.pages;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.PageManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CheckoutStepTwoPage extends Page {
    private static final Logger log = LogManager.getLogger(CheckoutStepTwoPage.class);


    private static final By ITEM_NAMES = By.cssSelector(".inventory_item_name, [data-test='inventory-item-name']");
    private static final By ITEM_QUANTITIES = By.cssSelector(".cart_quantity, .summary_quantity, [data-test='item-quantity']");
    private static final By ITEM_PRICES = By.cssSelector(".inventory_item_price, [data-test='inventory-item-price']");
    private static final By ITEM_DESCRIPTIONS = By.cssSelector(".inventory_item_desc, [data-test='inventory-item-desc']");
    private static final By SAUCE_CARD_LABEL = By.cssSelector("div[class='summary_info'] div:nth-child(2)");
    private static final By FREE_PONY_EXPRESS_LABEL = By.cssSelector("div[class='summary_info'] div:nth-child(4)");
    private static final By CANCEL_BUTTON = By.className("cart_cancel_link");
    private static final By FINISH_BUTTON = By.className("btn_action");
    private static final By TOTAL_VALUE_SIBLING = By.xpath("following-sibling::div");

    private static final String DEFAULT_QUANTITY = "0";
    private static final String DEFAULT_PRICE = "$0.00";
    private static final String DEFAULT_DESCRIPTION = "";
    private static final String TOTAL_LABEL_TEXT = "Total:";
    private static final String LABEL_ITEM_QUANTITIES = "Item quantities";

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "summary_info_label")
    private List<WebElement> summaryInfoLabels;

    @FindBy(css = "div[class='summary_info'] div:nth-child(2)")
    private WebElement sauceCardLabel;

    @FindBy(css = "div[class='summary_info'] div:nth-child(4)")
    private WebElement freePonyExpressLabel;

    @FindBy(className = "cart_list")
    private WebElement cartList;

    @FindBy(css = ".cart_quantity, .summary_quantity, [data-test='item-quantity']")
    private List<WebElement> itemQuantities;

    @FindBy(css = ".inventory_item_name, [data-test='inventory-item-name']")
    private List<WebElement> itemNames;

    @FindBy(css = ".inventory_item_price, [data-test='inventory-item-price']")
    private List<WebElement> itemPrices;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "cart_cancel_link")
    private WebElement cancelButton;

    @FindBy(className = "btn_action")
    private WebElement finishButton;


    public boolean isCheckoutSummaryVisibleDisplayed() {
        return verificationHelper.isDisplayed(cartList);
    }

    public boolean isSauceCardDisplayed() {
        return verificationHelper.isDisplayed(sauceCardLabel);
    }

    public boolean isFreePonyExpressDisplayed() {
        return verificationHelper.isDisplayed(freePonyExpressLabel);
    }

    public boolean isSubtotalDisplayed() {
        return verificationHelper.isElementPresent(subtotalLabel);
    }

    public boolean isTaxDisplayed() {
        return verificationHelper.isElementPresent(taxLabel);
    }

    public boolean isTotalDisplayed() {
        return verificationHelper.isElementPresent(totalLabel);
    }

    public boolean isFinishButtonDisplayed() {
        return verificationHelper.isDisplayed(finishButton);
    }

    public boolean isCancelButtonDisplayed() {
        return verificationHelper.isDisplayed(cancelButton);
    }

    public boolean areItemNamesDisplayed() {
        return areElementsDisplayedWithFreshLocator(ITEM_NAMES, "Item names");
    }

    public boolean areItemQuantitiesDisplayed() {
        return areElementsDisplayedWithFreshLocator(ITEM_QUANTITIES, LABEL_ITEM_QUANTITIES);
    }

    public boolean areItemPricesDisplayed() {
        return areElementsDisplayedWithFreshLocator(ITEM_PRICES, "Item prices");
    }

    public String getSauceCardText() {
        return getFirstVisibleText(
                By.cssSelector("[data-test='payment-info-value']"),
                SAUCE_CARD_LABEL,
                By.xpath("//*[contains(normalize-space(.), 'SauceCard #')]")
        );
    }

    public String getFreePonyExpressText() {
        return getFirstVisibleText(
                By.cssSelector("[data-test='shipping-info-value']"),
                FREE_PONY_EXPRESS_LABEL,
                By.xpath("//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'pony express')]")
        );
    }

    public List<String> getItemNames() {
        return itemNames.stream().map(WebElement::getText).toList();
    }

    public List<String> getItemQuantities() {
        return itemQuantities.stream().map(WebElement::getText).toList();
    }

    public List<String> getItemPrices() {
        return itemPrices.stream().map(WebElement::getText).toList();
    }

    public String getSubtotalText() {
        return verificationHelper.getText(subtotalLabel);
    }

    public String getTaxText() {
        return verificationHelper.getText(taxLabel);
    }

    public String getTotalText() {
        return verificationHelper.getText(totalLabel);
    }

    public String getFinishButtonText() {
        return getElementLabel(finishButton);
    }

    public String getCancelButtonText() {
        return getElementLabel(cancelButton);
    }

    public String getItemQuantityByName(String productName) {
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAMES).getText().equals(productName))
                .map(item -> item.findElement(ITEM_QUANTITIES).getText())
                .findFirst()
                .orElse(DEFAULT_QUANTITY);
    }

    public String getItemPriceByName(String productName) {
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAMES).getText().equals(productName))
                .map(item -> item.findElement(ITEM_PRICES).getText())
                .findFirst()
                .orElse(DEFAULT_PRICE);
    }

    public String getItemDescriptionByName(String productName) {
        return cartItems.stream()
                .filter(item -> item.findElement(ITEM_NAMES).getText().equals(productName))
                .map(item -> item.findElement(ITEM_DESCRIPTIONS).getText())
                .findFirst()
                .orElse(DEFAULT_DESCRIPTION);
    }

    public String getTotalAmount() {
        return summaryInfoLabels.stream()
                .filter(label -> label.getText().equals(TOTAL_LABEL_TEXT))
                .map(label -> label.findElement(TOTAL_VALUE_SIBLING).getText())
                .findFirst()
                .orElse(DEFAULT_PRICE);
    }


    public CheckoutCompletePage clickFinishButton() {
        WebElement finish = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, finishButton);
        if (finish == null) {
            throw new org.openqa.selenium.NoSuchElementException("Finish button is not clickable after wait.");
        }
        finish.click();
        log.info("Clicked on Finish button");
        return new CheckoutCompletePage();
    }

    public CartPage clickCancelButton() {
        WebElement cancel = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, cancelButton);
        if (cancel == null) {
            throw new org.openqa.selenium.NoSuchElementException("Cancel button is not clickable after wait.");
        }
        cancel.click();
        log.info("Clicked on Cancel button");
        return PageManager.getInstance().getPage(CartPage.class);
    }


    private String getFirstVisibleText(By... locators) {
        return Stream.of(locators)
                .flatMap(locator -> getFreshElements(locator).stream())
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .map(String::trim)
                .filter(trim -> !trim.isEmpty())
                .findFirst()
                .orElseGet(() -> {
                    log.warn("No visible non-empty text found for provided locators.");
                    return "";
                });
    }

    private List<WebElement> getFreshElements(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            List<WebElement> visible = ExplicitWaitFactory.performExplicitWait(
                    WaitStrategy.VISIBILITY_OF_ALL_ELEMENTS, elements);
            return visible != null ? visible : elements;
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    private List<WebElement> getFreshElementsWithFallback(By... fallbacks) {
        List<WebElement> elements = getFreshElements(CheckoutStepTwoPage.ITEM_QUANTITIES);
        if (!elements.isEmpty()) {
            return elements;
        }
        return Stream.of(fallbacks)
                .map(this::getFreshElements)
                .filter(list -> !list.isEmpty())
                .findFirst()
                .orElse(java.util.Collections.emptyList());
    }

    private boolean areElementsDisplayedWithFreshLocator(By locator, String label) {
        if (LABEL_ITEM_QUANTITIES.equals(label)) {
            return areQuantitiesDisplayedWithFallbacks();
        }
        try {
            List<WebElement> elements = getFreshElements(locator);
            if (elements.isEmpty()) {
                log.warn(label + " list is empty even after wait.");
                return false;
            }
            Optional<WebElement> hidden = elements.stream()
                    .filter(e -> !e.isDisplayed())
                    .findFirst();
            if (hidden.isPresent()) {
                log.warn(label + " element is not displayed: " + hidden.get().getText());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Error checking if " + label + " are displayed: " + e.getMessage(), e);
            return false;
        }
    }

    private String getElementLabel(WebElement element) {
        if (element == null) {
            return "";
        }

        String text = verificationHelper.getText(element);
        if (text != null && !text.trim().isEmpty()) {
            return text;
        }

        String value = element.getDomAttribute("value");
        return value == null ? "" : value;
    }

    private boolean areQuantitiesDisplayedWithFallbacks() {
        try {
            List<WebElement> quantities = getFreshElementsWithFallback(
                    By.cssSelector(".cart_item .summary_quantity"),
                    By.cssSelector("[data-test='item-quantity']"),
                    By.xpath("//div[@class='cart_item']//div[contains(text(), 'QTY')]")
            );
            if (quantities.isEmpty()) {
                log.warn("Item quantities not found with any selector");
                return false;
            }
            boolean allDisplayed = quantities.stream().allMatch(WebElement::isDisplayed);
            if (!allDisplayed) {
                log.warn("One or more quantity elements are not displayed");
            }
            return allDisplayed;
        } catch (Exception e) {
            log.error("Error checking if quantities are displayed: " + e.getMessage());
            return false;
        }
    }
}
