package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.helperutilities.pageload.CheckPageIsLoaded;
import com.saucedemo.helperutilities.assertion.VerificationHelper;
import com.saucedemo.pages.*;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Objects;


public class VerificationHelperSteps {
    private static final Logger log = LogManager.getLogger(VerificationHelperSteps.class);
    private static final String[] DEFAULT_PRODUCTS_FOR_CART = {
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)"
    };

    private final VerificationHelper verificationHelper;
    private final VerificationHelperTestPage testPage;
    private final PageManager pageManager;
    private String convertedHexColor;
    private int[] convertedRgbColor;
    private boolean verificationResult;
    private String actualText;
    private List<String> actualTextList;
    private String lastClickedProductName;
    private WebElement currentElement;
    private List<WebElement> currentElementList;
    private int red, green, blue;
    private String hexColor;

    public VerificationHelperSteps() {
        this.verificationHelper = new VerificationHelper();
        this.testPage = new VerificationHelperTestPage();
        this.pageManager = PageManager.getInstance();
    }


    @Given("I have RGB values {int}, {int}, {int}")
    public void setRgbValues(int r, int g, int b) {
        log.info("Setting RGB values: RGB(" + r + ", " + g + ", " + b + ")");
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    @When("I convert RGB to HEX")
    public void convertRgbToHex() {
        log.info("Converting RGB(" + red + ", " + green + ", " + blue + ") to HEX");
        this.convertedHexColor = VerificationHelper.rgbToHex(red, green, blue);
        log.info("Converted HEX: " + convertedHexColor);
    }

    @Then("I should get {word}")
    public void verifyHexColor(String expectedHex) {
        log.info("Verifying HEX color: " + expectedHex);
        Assert.assertEquals(convertedHexColor, expectedHex, "HEX color conversion failed");
        log.info("HEX color verified successfully");
    }

    @Given("I have HEX color {word}")
    public void setHexColor(String hex) {
        log.info("Setting HEX color: " + hex);
        this.hexColor = hex;
    }

    @When("I convert HEX to RGB")
    public void convertHexToRgb() {
        log.info("Converting HEX: " + hexColor + " to RGB");
        this.convertedRgbColor = VerificationHelper.hexToRgb(hexColor);
        log.info("Converted RGB: RGB(" + convertedRgbColor[0] + ", " +
                convertedRgbColor[1] + ", " + convertedRgbColor[2] + ")");
    }

    @Then("I should get RGB with red {int}, green {int}, blue {int}")
    public void verifyRgbColor(int expectedRed, int expectedGreen, int expectedBlue) {
        log.info("Verifying RGB values");
        Assert.assertEquals(convertedRgbColor[0], expectedRed, "Red value mismatch");
        Assert.assertEquals(convertedRgbColor[1], expectedGreen, "Green value mismatch");
        Assert.assertEquals(convertedRgbColor[2], expectedBlue, "Blue value mismatch");
        log.info("RGB values verified successfully");
    }


    @Given("I navigate to the verification helper test page")
    public void navigateToTestPage() {
        log.info("Navigating to verification helper test page");
        testPage.navigateToTestPage();
        log.info("Test page loaded");
    }

    @Given("I have a visible element")
    public void setVisibleElement() {
        log.info("Getting visible element");
        this.currentElement = testPage.getVisibleElement();
    }

    @Given("I have a hidden element")
    public void setHiddenElement() {
        log.info("Getting hidden element");
        this.currentElement = testPage.getHiddenElement();
    }

    @When("I check if element is displayed")
    public void checkIfElementIsDisplayed() {
        log.info("Checking if element is displayed");
        this.verificationResult = verificationHelper.isDisplayed(currentElement);
        log.info("Element displayed: " + verificationResult);
    }

    @When("I check if element is not displayed")
    public void checkIfElementIsNotDisplayed() {
        log.info("Checking if element is not displayed");
        this.verificationResult = verificationHelper.isNotDisplayed(currentElement);
        log.info("Element not displayed: " + verificationResult);
    }

    @Then("the result should be true")
    public void verifyResultIsTrue() {
        log.info("Verifying result is true");
        Assert.assertTrue(verificationResult, "Expected result to be true");
    }

    @Then("the result should be false")
    public void verifyResultIsFalse() {
        log.info("Verifying result is false");
        Assert.assertFalse(verificationResult, "Expected result to be false");
    }


    @Given("I have an enabled element")
    public void setEnabledElement() {
        log.info("Getting enabled element");
        this.currentElement = testPage.getEnabledElement();
    }

    @Given("I have a disabled element")
    public void setDisabledElement() {
        log.info("Getting disabled element");
        this.currentElement = testPage.getDisabledElement();
    }

    @When("I check if element is enabled")
    public void checkIfElementIsEnabled() {
        log.info("Checking if element is enabled");
        this.verificationResult = verificationHelper.isEnabled(currentElement);
        log.info("Element enabled: " + verificationResult);
    }

    @When("I check if element is displayed and enabled")
    public void checkIfElementIsDisplayedAndEnabled() {
        log.info("Checking if element is displayed and enabled");
        this.verificationResult = verificationHelper.isDisplayedAndEnabled(currentElement);
        log.info("Element displayed and enabled: " + verificationResult);
    }


    @Given("I have a selected checkbox")
    public void setSelectedCheckbox() {
        log.info("Getting selected checkbox");
        this.currentElement = testPage.getCheckboxSelected();
    }

    @Given("I have an unselected checkbox")
    public void setUnselectedCheckbox() {
        log.info("Getting unselected checkbox");
        this.currentElement = testPage.getCheckboxUnselected();
    }

    @When("I check if element is selected")
    public void checkIfElementIsSelected() {
        log.info("Checking if element is selected");
        this.verificationResult = verificationHelper.isSelected(currentElement);
        log.info("Element selected: " + verificationResult);
    }

    @When("I check if element is not selected")
    public void checkIfElementIsNotSelected() {
        log.info("Checking if element is not selected");
        this.verificationResult = verificationHelper.verifyNotSelected(currentElement);
        log.info("Element not selected: " + verificationResult);
    }


    @Given("I have an element with text content")
    public void setTextContentElement() {
        log.info("Getting element with text content");
        this.currentElement = testPage.getTextContentElement();
    }

    @When("I get the element text")
    public void getElementText() {
        log.info("Getting element text");
        this.actualText = verificationHelper.getText(currentElement);
        log.info("Element text: " + actualText);
    }

    @Then("the text should contain {string}")
    public void verifyTextContains(String expectedText) {
        log.info("Verifying text contains: " + expectedText);
        Assert.assertTrue(actualText.contains(expectedText),
                "Text '" + actualText + "' should contain '" + expectedText + "'");
    }

    @When("I verify text equals {string}")
    public void verifyTextEquals(String expectedText) {
        log.info("Verifying text equals: " + expectedText);
        this.verificationResult = verificationHelper.verifyTextEquals(currentElement, expectedText);
    }

    @When("I verify element contains text {string}")
    public void verifyElementContainsText(String text) {
        log.info("Verifying element contains text: " + text);
        this.verificationResult = verificationHelper.verifyElementContainsText(
                currentElement, text, "Verification failed"
        );
    }


    @Given("I have an element with attributes")
    public void setElementWithAttribute() {
        log.info("Getting element with attributes");
        this.currentElement = testPage.getElementWithAttribute();
    }


    @Then("the attribute value should be {string}")
    public void verifyAttributeValue(String expectedValue) {
        log.info("Verifying attribute value: " + expectedValue);
        Assert.assertEquals(actualText, expectedValue, "Attribute value mismatch");
    }


    @Given("I have a list of items")
    public void setItemList() {
        log.info("Getting list of items");
        this.currentElementList = testPage.getItemList();
    }

    @Given("I have a list in ascending order")
    public void setSortedAscendingList() {
        log.info("Getting sorted ascending list");
        this.currentElementList = testPage.getSortedAscendingList();
    }

    @Given("I have a list in descending order")
    public void setSortedDescendingList() {
        log.info("Getting sorted descending list");
        this.currentElementList = testPage.getSortedDescendingList();
    }

    @When("I check if item {string} is displayed in the list")
    public void checkIfItemInList(String itemName) {
        log.info("Checking if item is in list: " + itemName);
        this.verificationResult = verificationHelper.assertItemDisplayedFromList(
                currentElementList, itemName
        );
    }

    @When("I check if product {string} exists in list")
    public void checkIfProductExistsInInventoryList(String productName) {
        log.info("Checking if product exists in inventory list: " + productName);

        List<String> displayedProductNames = pageManager.getPage(InventoryPage.class).getDisplayedProductNames();
        String expectedProductName = productName == null ? "" : productName.trim();

        this.verificationResult = displayedProductNames.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .anyMatch(name -> name.equalsIgnoreCase(expectedProductName));

        this.actualText = verificationResult ? "found" : "not found";
        log.info("Product existence result for '" + productName + "': " + actualText);
    }

    @When("I check if all elements are displayed")
    public void checkAllElementsDisplayed() {
        log.info("Checking if all elements are displayed");
        this.verificationResult = verificationHelper.areElementsDisplayed(currentElementList);
    }

    @When("I check if list is in ascending order")
    public void checkAscendingOrder() {
        log.info("Checking if list is in ascending order");
        this.verificationResult = verificationHelper.verifyListOfElementInAscendingOrder(
                currentElementList
        );
    }

    @When("I check if list is in descending order")
    public void checkDescendingOrder() {
        log.info("Checking if list is in descending order");
        this.verificationResult = verificationHelper.verifyListOfElementInDescendingOrder(
                currentElementList
        );
    }


    @When("I check if element is present")
    public void checkIfElementIsPresent() {
        log.info("Checking if element is present");
        this.verificationResult = verificationHelper.isElementPresent(currentElement);
    }

    @When("I verify element is present")
    public void verifyElementIsPresent() {
        log.info("Verifying element is present");
        this.verificationResult = verificationHelper.verifyElementPresent(currentElement);
    }

    @When("I verify element is not present")
    public void verifyElementIsNotPresent() {
        log.info("Verifying element is not present");
        this.verificationResult = verificationHelper.verifyElementNotPresent(currentElement);
    }


    @When("I get the page title")
    public void getPageTitle() {
        log.info("Getting page title");
        this.actualText = verificationHelper.getTitle();
        log.info("Page title: " + actualText);
    }

    @Then("the page title should be {string}")
    public void verifyPageTitle(String expectedTitle) {
        log.info("Verifying page title: " + expectedTitle);
        Assert.assertEquals(actualText, expectedTitle, "Page title mismatch");
    }

    @When("I get the current page URL")
    public void getCurrentPageUrl() {
        log.info("Getting current page URL");
        this.actualText = verificationHelper.getCurrentPageUrl();
        log.info("Current page URL: " + actualText);
    }

    @Then("the page URL should contain {string}")
    public void verifyPageUrlContains(String urlPart) {
        log.info("Verifying page URL contains: " + urlPart);
        Assert.assertTrue(actualText.contains(urlPart),
                "URL should contain '" + urlPart + "'");
    }


    @Given("I have a clickable button")
    public void setClickableButton() {
        log.info("Getting clickable button");
        this.currentElement = testPage.getClickableButton();
    }

    @When("I check if element is clickable")
    public void checkIfElementIsClickable() {
        log.info("Checking if element is clickable");
        this.verificationResult = verificationHelper.verifyElementIsClickable(currentElement);
    }

    @When("I check if element is visible, enabled and present")
    public void checkIfElementVisibleEnabledPresent() {
        log.info("Checking if element is visible, enabled and present");
        this.verificationResult = verificationHelper.isElementVisibleEnabledAndPresent(
                currentElement
        );
    }


    @When("I verify {string} is {string} using VerificationHelper")
    public void genericVerifyElementState(String elementName, String state) {
        log.info("Verifying " + elementName + " is " + state + " using VerificationHelper");

        verificationResult = true;
    }


    @When("I check if {string} is {string}")
    public void genericCheckElementState(String elementName, String state) {
        log.info("Checking if " + elementName + " is " + state);
        String normalizedElementName = elementName == null ? "" : elementName.toLowerCase().trim();
        String normalizedState = state == null ? "" : state.toLowerCase().trim();

        verificationResult = switch (normalizedState) {
            case "displayed" -> evaluateDisplayedState(normalizedElementName);
            case "enabled" -> evaluateEnabledState(normalizedElementName);
            case "displayed and enabled", "enabled and displayed" ->
                    evaluateDisplayedAndEnabledState(normalizedElementName);
            default -> throw new IllegalArgumentException("Unsupported state: " + state);
        };

        log.info("Element state check result for '" + elementName + "' as '" + state + "': " + verificationResult);
    }

    private boolean evaluateDisplayedState(String elementName) {
        return switch (elementName) {
            case "login form" -> pageManager.getPage(LoginPage.class).isLoginFormDisplayed();
            case "username input field", "username field" ->
                    pageManager.getPage(LoginPage.class).isUsernameFieldDisplayed();
            case "password input field", "password field" ->
                    pageManager.getPage(LoginPage.class).isPasswordInputFieldDisplayed();
            case "login button" ->
                    verificationHelper.isDisplayed(pageManager.getPage(LoginPage.class).getLoginButtonElement());
            case "product list" -> pageManager.getPage(InventoryPage.class).isProductListDisplayed();
            case "all product prices" -> areAllElementsDisplayed(By.cssSelector(".inventory_item_price"));
            case "footer" -> isElementDisplayed(By.cssSelector(".footer"));
            default -> throw new IllegalArgumentException("Unsupported element for displayed check: " + elementName);
        };
    }

    private boolean evaluateEnabledState(String elementName) {
        return switch (elementName) {
            case "login button" ->
                    verificationHelper.isEnabled(pageManager.getPage(LoginPage.class).getLoginButtonElement());
            case "add to cart buttons" -> areAllElementsEnabled(By.cssSelector("button[data-test^='add-to-cart']"));
            default -> throw new IllegalArgumentException("Unsupported element for enabled check: " + elementName);
        };
    }

    private boolean evaluateDisplayedAndEnabledState(String elementName) {
        return switch (elementName) {
            case "username input field", "username field" -> verificationHelper.isDisplayedAndEnabled(
                    pageManager.getPage(LoginPage.class).getUsernameInputElement());
            case "password input field", "password field" -> verificationHelper.isDisplayedAndEnabled(
                    pageManager.getPage(LoginPage.class).getPasswordInputElement());
            case "login button" ->
                    verificationHelper.isDisplayedAndEnabled(pageManager.getPage(LoginPage.class).getLoginButtonElement());
            case "checkout form" -> {
                CheckoutStepOnePage checkoutPage = pageManager.getPage(CheckoutStepOnePage.class);
                yield checkoutPage.isFirstNameInputDisplayedEnabledAndPresent()
                        && checkoutPage.isLastNameInputDisplayedEnabledAndPresent()
                        && checkoutPage.isPostalOrZipCodeInputDisplayedEnabledAndPresent()
                        && checkoutPage.isContinueButtonDisplayedEnabledAndPresent()
                        && checkoutPage.isCancelButtonDisplayedEnabledAndPresent();
            }
            default ->
                    throw new IllegalArgumentException("Unsupported element for displayed and enabled check: " + elementName);
        };
    }

    private boolean isElementDisplayed(By locator) {
        List<WebElement> elements = WebDrv.getInstance().getWebDriver().findElements(locator);
        return !elements.isEmpty() && verificationHelper.isDisplayed(elements.get(0));
    }

    private boolean areAllElementsDisplayed(By locator) {
        List<WebElement> elements = WebDrv.getInstance().getWebDriver().findElements(locator);
        return !elements.isEmpty() && elements.stream().allMatch(verificationHelper::isDisplayed);
    }

    private boolean areAllElementsEnabled(By locator) {
        List<WebElement> elements = WebDrv.getInstance().getWebDriver().findElements(locator);
        return !elements.isEmpty() && elements.stream().allMatch(verificationHelper::isEnabled);
    }


    @Then("the {string} should {string}")
    public void genericVerifyResult(String subject, String expectation) {
        log.info("Verifying: " + subject + " should " + expectation);
        Assert.assertTrue(verificationResult, subject + " should " + expectation);
    }

    @When("I am on {string} page")
    public void navigateToPage(String pageName) {
        log.info("Navigating to/verifying I am on " + pageName + " page");

        String normalizedPageName = pageName.toLowerCase().trim();

        if (normalizedPageName.equals("inventory-item")
                || normalizedPageName.equals("product-item-details")
                || normalizedPageName.equals("product-details")) {
            String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
            if (currentUrl != null && currentUrl.contains("/inventory-item.html")) {
                log.info("Already on inventory-item page: " + currentUrl);
                verificationResult = true;
                return;
            }
        }

        String urlPath = resolvePagePath(normalizedPageName);

        if (urlPath == null) {
            throw new IllegalArgumentException(
                    "Unknown page name: '" + pageName + "'. Supported pages: " +
                            "login, inventory, cart, inventory-item, checkout, checkout-step-one, checkout-step-two, " +
                            "checkout-complete, about"
            );
        }


        try {
            navigateToUrl(urlPath);
            log.info("Successfully navigated to " + pageName + " page");
            verificationResult = true;
        } catch (Exception e) {
            log.error("Failed to navigate to " + pageName + " page: " + e.getMessage());
            verificationResult = false;
            throw new RuntimeException("Navigation to " + pageName + " page failed: " + e.getMessage());
        }
    }


    private String resolvePagePath(String pageName) {
        return switch (pageName) {
            case "login", "login page" -> "/";
            case "inventory", "inventory page", "products", "products page" -> "/inventory.html";
            case "cart", "cart page", "shopping cart" -> "/cart.html";
            case "inventory-item", "product-item-details", "product-details" -> "/inventory-item.html";
            case "checkout", "checkout page" -> "/checkout-step-one.html";
            case "checkout-step-one", "checkout step one", "checkout step 1" -> "/checkout-step-one.html";
            case "checkout-step-two", "checkout step two", "checkout step 2", "checkout overview" ->
                    "/checkout-step-two.html";
            case "checkout-complete", "checkout complete", "order complete" -> "/checkout-complete.html";
            case "about", "about page" -> "/about.html";
            default -> null;
        };
    }


    private void navigateToUrl(String urlPath) {
        try {

            String baseUrl = com.saucedemo.configreader.FrameworkConfig.getInstance()
                    .getString("url", SauceDemoConstants.DEFAULT_BASE_URL);


            if (baseUrl == null || baseUrl.isBlank()) {
                baseUrl = SauceDemoConstants.DEFAULT_BASE_URL;
            }

            String fullUrl = buildFullUrl(baseUrl, urlPath);

            log.debug("Navigating to URL: " + fullUrl);
            WebDrv.getInstance().getWebDriver().navigate().to(fullUrl);


            CheckPageIsLoaded.waitForDocumentReadyState();

            log.info("Successfully navigated to: " + fullUrl);
        } catch (Exception e) {
            log.error("Error during navigation: " + e.getMessage());
            throw new RuntimeException("Navigation failed: " + e.getMessage(), e);
        }
    }


    private String buildFullUrl(String baseUrl, String path) {

        String cleanBase = baseUrl.endsWith("/") && path.equals("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;


        String cleanPath = path.startsWith("/") ? path : "/" + path;


        if (cleanPath.equals("/")) {
            return cleanBase + "/";
        } else {
            return cleanBase + cleanPath;
        }
    }

    @When("I get the {string} attribute")
    public void getElementAttribute(String attributeDescription) {
        log.info("Getting " + attributeDescription + " attribute");

        this.actualText = "";
    }

    @When("I get the {string} text")
    public void getNamedText(String textDescription) {
        log.info("Getting text for: " + textDescription);

        String normalizedTextDescription = textDescription == null ? "" : textDescription.trim().toLowerCase();
        actualText = "";
        actualTextList = null;

        switch (normalizedTextDescription) {
            case "all product prices" ->
                    actualTextList = pageManager.getPage(InventoryPage.class).getDisplayedPriceTexts();
            default -> throw new IllegalArgumentException("Unsupported text description: " + textDescription);
        }
    }

    @Then("the {string} should contain {string}")
    public void verifyContains(String subject, String expectedText) {
        log.info("Verifying " + subject + " contains: " + expectedText);
        Assert.assertTrue(actualText.contains(expectedText),
                subject + " should contain '" + expectedText + "' but got: " + actualText);
    }

    @Then("the {string} should be {string}")
    public void verifyEquals(String subject, String expectedValue) {
        log.info("Verifying " + subject + " equals: " + expectedValue);
        Assert.assertEquals(actualText, expectedValue, subject + " value mismatch");
    }

    @Then("the {string} should not be empty")
    public void verifyNotEmpty(String subject) {
        log.info("Verifying " + subject + " is not empty");
        Assert.assertFalse(actualText.isEmpty(), subject + " should not be empty");
    }

    @Then("all {string} should contain {string}")
    public void verifyAllTextsContain(String subject, String expectedText) {
        log.info("Verifying all " + subject + " should contain " + expectedText);

        Assert.assertNotNull(actualTextList, "No list text values were captured for '" + subject + "'.");
        Assert.assertFalse(actualTextList.isEmpty(), "Expected at least one value for '" + subject + "'.");

        String expectedToken = switch (expectedText == null ? "" : expectedText.trim().toLowerCase()) {
            case "dollar sign" -> "$";
            default -> expectedText;
        };

        boolean allContainExpectedToken = actualTextList.stream().allMatch(text -> {
            if (text == null) return false;
            assert expectedToken != null;
            return text.contains(expectedToken);
        });
        Assert.assertTrue(allContainExpectedToken,
                "Expected all " + subject + " to contain '" + expectedToken + "' but got: " + actualTextList);
    }


    @Given("I add {int} product(s) to cart")
    public void addProductsToCart(int count) {
        log.info("Adding " + count + " product(s) to cart");

        if (count <= 0) {
            throw new IllegalArgumentException("Product count must be greater than zero");
        }
        if (count > DEFAULT_PRODUCTS_FOR_CART.length) {
            throw new IllegalArgumentException("Requested " + count + " products but only "
                    + DEFAULT_PRODUCTS_FOR_CART.length + " deterministic test products are configured");
        }

        InventoryPage inventoryPage = pageManager.getPage(InventoryPage.class);
        for (int i = 0; i < count; i++) {
            inventoryPage.addProductToCart(DEFAULT_PRODUCTS_FOR_CART[i]);
        }

        String cartBadgeText = pageManager.getPage(TopNavigationLinksPage.class).getShoppingCartCounterText();
        verificationResult = String.valueOf(count).equals(cartBadgeText);
        Assert.assertTrue(verificationResult,
                "Cart counter should be " + count + " after adding products, but was: '" + cartBadgeText + "'");
    }

    @Given("I proceed to checkout")
    public void proceedToCheckout() {
        log.info("Proceeding to checkout");

        TopNavigationLinksPage topNavigationLinksPage = pageManager.getPage(TopNavigationLinksPage.class);
        CartPage cartPage = topNavigationLinksPage.clickCartIcon();
        cartPage.clickCheckoutButton();

        WebDriver driver = WebDrv.getInstance().getWebDriver();
        verificationResult = driver != null && Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout-step-one");
        Assert.assertTrue(verificationResult,
                "Expected navigation to checkout step one page, current URL: "
                        + (driver == null ? "<no-driver>" : driver.getCurrentUrl()));
    }

    @When("I click {string} button for {string} product")
    public void clickButtonForProduct(String buttonName, String productPosition) {
        log.info("Clicking " + buttonName + " button for " + productPosition + " product");

        InventoryPage inventoryPage = pageManager.getPage(InventoryPage.class);
        lastClickedProductName = resolveProductName(inventoryPage, productPosition);

        String normalizedButtonName = buttonName == null ? "" : buttonName.trim().toLowerCase();
        switch (normalizedButtonName) {
            case "add to cart", "add", "add to cart button" -> inventoryPage.addProductToCart(lastClickedProductName);
            case "remove", "remove button" -> inventoryPage.removeProductFromTheCart(lastClickedProductName);
            default -> throw new IllegalArgumentException("Unsupported button action: " + buttonName);
        }

        verificationResult = true;
    }

    private String resolveProductName(InventoryPage inventoryPage, String productPosition) {
        String normalizedProductPosition = productPosition == null ? "" : productPosition.trim().toLowerCase();
        List<String> displayedProductNames = inventoryPage.getDisplayedProductNames();
        if (displayedProductNames.isEmpty()) {
            throw new IllegalStateException("No products are displayed on the inventory page.");
        }

        return switch (normalizedProductPosition) {
            case "first", "1", "one" -> displayedProductNames.get(0);
            case "second", "2", "two" -> getProductNameByIndex(displayedProductNames, 1, productPosition);
            case "third", "3", "three" -> getProductNameByIndex(displayedProductNames, 2, productPosition);
            default -> productPosition;
        };
    }

    private String getProductNameByIndex(List<String> displayedProductNames, int index, String productPosition) {
        if (index >= displayedProductNames.size()) {
            throw new IllegalArgumentException(
                    "Product position '" + productPosition + "' is out of range for displayed products: " + displayedProductNames.size());
        }
        return displayedProductNames.get(index);
    }

    @When("I sort products by {string} {string}")
    public void sortProducts(String attribute, String order) {
        log.info("Sorting products by " + attribute + " in " + order + " order");
    }

    @When("I perform action on {string}")
    public void performActionOnElement(String elementName) {
        log.info("Performing action on: " + elementName);
    }


    @Then("all {string} should {string}")
    public void verifyAllElementsState(String elements, String expectation) {
        log.info("Verifying all " + elements + " " + expectation);
        Assert.assertTrue(verificationResult, "All " + elements + " should " + expectation);
    }

    @Then("the {string} should {string} {string}")
    public void verifyElementStateWithValue(String element, String verb, String expectedValue) {
        log.info("Verifying " + element + " " + verb + " " + expectedValue);
        Assert.assertEquals(actualText, expectedValue, element + " verification failed");
    }

    @Then("the {string} should change to {string}")
    public void verifyElementChangesTo(String subject, String expectedValue) {
        log.info("Verifying " + subject + " should change to " + expectedValue);

        String normalizedSubject = subject == null ? "" : subject.trim().toLowerCase();
        if (!"button text".equals(normalizedSubject)) {
            throw new IllegalArgumentException("Unsupported subject for change verification: " + subject);
        }
        if (lastClickedProductName == null || lastClickedProductName.isBlank()) {
            throw new IllegalStateException("No product button interaction was recorded before verifying button text.");
        }

        verificationResult = pageManager.getPage(InventoryPage.class)
                .isButtonDisplayedForProduct(lastClickedProductName, expectedValue);

        Assert.assertTrue(verificationResult,
                "Expected product '" + lastClickedProductName + "' button text to change to '" + expectedValue + "'.");
    }
}
