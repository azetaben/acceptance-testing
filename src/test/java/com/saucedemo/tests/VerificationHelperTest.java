package com.saucedemo.tests;

import com.saucedemo.helperutilities.assertion.VerificationHelper;
import com.saucedemo.pages.VerificationHelperTestPage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mockito.Mockito;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class VerificationHelperTest {
    private static final Logger log = LogManager.getLogger(VerificationHelperTest.class);

    private VerificationHelper verificationHelper;
    private VerificationHelperTestPage testPage;

    @BeforeClass
    public void setUp() {
        log.info("========== VerificationHelper Test Suite START ==========");
        verificationHelper = new VerificationHelper();
        testPage = new VerificationHelperTestPage();
        log.info("Test setup completed");
    }

    @AfterClass
    public void tearDown() {
        log.info("========== VerificationHelper Test Suite END ==========");
    }


    @Test(description = "Test rgbToHex conversion with valid RGB values")
    public void testRgbToHexWithValidValues() {
        log.info("Testing rgbToHex with valid RGB values");
        String result = VerificationHelper.rgbToHex(255, 0, 0);
        Assert.assertEquals(result, "#FF0000", "RGB(255, 0, 0) should convert to #FF0000");

        result = VerificationHelper.rgbToHex(0, 255, 0);
        Assert.assertEquals(result, "#00FF00", "RGB(0, 255, 0) should convert to #00FF00");

        result = VerificationHelper.rgbToHex(0, 0, 255);
        Assert.assertEquals(result, "#0000FF", "RGB(0, 0, 255) should convert to #0000FF");

        log.info("RGB to HEX conversion test passed");
    }

    @Test(description = "Test rgbToHex with edge case values")
    public void testRgbToHexEdgeCases() {
        log.info("Testing rgbToHex with edge cases");
        String result = VerificationHelper.rgbToHex(0, 0, 0);
        Assert.assertEquals(result, "#000000", "All zeros should convert to #000000");

        result = VerificationHelper.rgbToHex(255, 255, 255);
        Assert.assertEquals(result, "#FFFFFF", "All 255 values should convert to #FFFFFF");

        log.info("RGB to HEX edge case test passed");
    }

    @Test(description = "Test rgbToHex with invalid RGB values - should throw exception",
            expectedExceptions = IllegalArgumentException.class)
    public void testRgbToHexWithInvalidValues() {
        log.info("Testing rgbToHex with invalid RGB values");
        VerificationHelper.rgbToHex(256, 0, 0);
    }

    @Test(description = "Test hexToRgb conversion with valid HEX values")
    public void testHexToRgbWithValidValues() {
        log.info("Testing hexToRgb with valid HEX values");
        int[] result = VerificationHelper.hexToRgb("#FF0000");
        Assert.assertEquals(result, new int[]{255, 0, 0}, "HEX #FF0000 should convert to RGB(255, 0, 0)");

        result = VerificationHelper.hexToRgb("#00FF00");
        Assert.assertEquals(result, new int[]{0, 255, 0}, "HEX #00FF00 should convert to RGB(0, 255, 0)");

        result = VerificationHelper.hexToRgb("#0000FF");
        Assert.assertEquals(result, new int[]{0, 0, 255}, "HEX #0000FF should convert to RGB(0, 0, 255)");

        log.info("HEX to RGB conversion test passed");
    }

    @Test(description = "Test hexToRgb with invalid format - should throw exception",
            expectedExceptions = IllegalArgumentException.class)
    public void testHexToRgbWithInvalidFormat() {
        log.info("Testing hexToRgb with invalid format");
        VerificationHelper.hexToRgb("INVALID");
    }

    @Test(description = "Test HexAndGetCssValue - get CSS values from element")
    public void testHexAndGetCssValue() {
        log.info("Testing HexAndGetCssValue");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.getCssValue("color")).thenReturn("rgb(255, 0, 0)");
        Mockito.when(mockElement.getCssValue("background-color")).thenReturn("rgb(0, 255, 0)");

        Map<String, String> cssValues = VerificationHelper.HexAndGetCssValue(mockElement);

        Assert.assertNotNull(cssValues, "CSS values map should not be null");
        Assert.assertEquals(cssValues.size(), 2, "Should have 2 CSS values");
        Assert.assertEquals(cssValues.get("color"), "rgb(255, 0, 0)");
        Assert.assertEquals(cssValues.get("background-color"), "rgb(0, 255, 0)");

        log.info("HexAndGetCssValue test passed");
    }


    @Test(description = "Test isDisplayed with visible element")
    public void testIsDisplayedWithVisibleElement() {
        log.info("Testing isDisplayed with visible element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);

        boolean result = verificationHelper.isDisplayed(mockElement);
        Assert.assertTrue(result, "Visible element should return true");

        log.info("isDisplayed test with visible element passed");
    }

    @Test(description = "Test isDisplayed with hidden element")
    public void testIsDisplayedWithHiddenElement() {
        log.info("Testing isDisplayed with hidden element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(false);

        boolean result = verificationHelper.isDisplayed(mockElement);
        Assert.assertFalse(result, "Hidden element should return false");

        log.info("isDisplayed test with hidden element passed");
    }

    @Test(description = "Test isDisplayed with element not found")
    public void testIsDisplayedWithElementNotFound() {
        log.info("Testing isDisplayed with element not found");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenThrow(new NoSuchElementException("Element not found"));

        boolean result = verificationHelper.isDisplayed(mockElement);
        Assert.assertFalse(result, "Non-existent element should return false");

        log.info("isDisplayed test with element not found passed");
    }

    @Test(description = "Test isNotDisplayed with hidden element")
    public void testIsNotDisplayedWithHiddenElement() {
        log.info("Testing isNotDisplayed with hidden element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(false);

        boolean result = verificationHelper.isNotDisplayed(mockElement);
        Assert.assertTrue(result, "Hidden element should return true");

        log.info("isNotDisplayed test with hidden element passed");
    }

    @Test(description = "Test isNotDisplayed with null element")
    public void testIsNotDisplayedWithNullElement() {
        log.info("Testing isNotDisplayed with null element");
        boolean result = verificationHelper.isNotDisplayed(null);
        Assert.assertTrue(result, "Null element should return true (treating as not displayed)");

        log.info("isNotDisplayed test with null element passed");
    }


    @Test(description = "Test isEnabled with enabled element")
    public void testIsEnabledWithEnabledElement() {
        log.info("Testing isEnabled with enabled element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isEnabled()).thenReturn(true);

        boolean result = verificationHelper.isEnabled(mockElement);
        Assert.assertTrue(result, "Enabled element should return true");

        log.info("isEnabled test with enabled element passed");
    }

    @Test(description = "Test isEnabled with disabled element")
    public void testIsEnabledWithDisabledElement() {
        log.info("Testing isEnabled with disabled element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isEnabled()).thenReturn(false);

        boolean result = verificationHelper.isEnabled(mockElement);
        Assert.assertFalse(result, "Disabled element should return false");

        log.info("isEnabled test with disabled element passed");
    }

    @Test(description = "Test isDisplayedAndEnabled with displayed and enabled element")
    public void testIsDisplayedAndEnabledBothTrue() {
        log.info("Testing isDisplayedAndEnabled with displayed and enabled element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.isEnabled()).thenReturn(true);

        boolean result = verificationHelper.isDisplayedAndEnabled(mockElement);
        Assert.assertTrue(result, "Displayed and enabled element should return true");

        log.info("isDisplayedAndEnabled test passed");
    }

    @Test(description = "Test isDisplayedAndEnabled with hidden element")
    public void testIsDisplayedAndEnabledHiddenElement() {
        log.info("Testing isDisplayedAndEnabled with hidden element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(false);
        Mockito.when(mockElement.isEnabled()).thenReturn(true);

        boolean result = verificationHelper.isDisplayedAndEnabled(mockElement);
        Assert.assertFalse(result, "Hidden element should return false");

        log.info("isDisplayedAndEnabled with hidden element test passed");
    }


    @Test(description = "Test isSelected with selected element")
    public void testIsSelectedWithSelectedElement() {
        log.info("Testing isSelected with selected element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isSelected()).thenReturn(true);

        boolean result = verificationHelper.isSelected(mockElement);
        Assert.assertTrue(result, "Selected element should return true");

        log.info("isSelected test with selected element passed");
    }

    @Test(description = "Test isSelected with unselected element")
    public void testIsSelectedWithUnselectedElement() {
        log.info("Testing isSelected with unselected element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isSelected()).thenReturn(false);

        boolean result = verificationHelper.isSelected(mockElement);
        Assert.assertFalse(result, "Unselected element should return false");

        log.info("isSelected test with unselected element passed");
    }

    @Test(description = "Test verifyNotSelected with unselected element")
    public void testVerifyNotSelectedWithUnselectedElement() {
        log.info("Testing verifyNotSelected with unselected element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isSelected()).thenReturn(false);

        boolean result = verificationHelper.verifyNotSelected(mockElement);
        Assert.assertTrue(result, "Unselected element should return true");

        log.info("verifyNotSelected test passed");
    }


    @Test(description = "Test getText with element containing text")
    public void testGetTextWithContent() {
        log.info("Testing getText with element containing text");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.getText()).thenReturn("Test Content");

        String result = verificationHelper.getText(mockElement);
        Assert.assertEquals(result, "Test Content", "Should return element text");

        log.info("getText test passed");
    }

    @Test(description = "Test getText with list of elements")
    public void testGetTextWithListOfElements() {
        log.info("Testing getText with list of elements");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.getText()).thenReturn("First Item");

        List<WebElement> elements = new ArrayList<>();
        elements.add(mockElement);

        String result = verificationHelper.getText(elements);
        Assert.assertEquals(result, "First Item", "Should return first element text");

        log.info("getText with list test passed");
    }

    @Test(description = "Test readValueFromElement")
    public void testReadValueFromElement() {
        log.info("Testing readValueFromElement");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.getText()).thenReturn("Value");

        String result = verificationHelper.readValueFromElement(mockElement);
        Assert.assertEquals(result, "Value", "Should return element value");

        log.info("readValueFromElement test passed");
    }

    @Test(description = "Test readValueFromElement with null element")
    public void testReadValueFromElementWithNull() {
        log.info("Testing readValueFromElement with null element");
        String result = verificationHelper.readValueFromElement(null);
        Assert.assertNull(result, "Null element should return null");

        log.info("readValueFromElement with null test passed");
    }

    @Test(description = "Test verifyTextEquals with matching text")
    public void testVerifyTextEqualsMatching() {
        log.info("Testing verifyTextEquals with matching text");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.getText()).thenReturn("Expected Text");

        boolean result = verificationHelper.verifyTextEquals(mockElement, "Expected Text");
        Assert.assertTrue(result, "Matching text should return true");

        log.info("verifyTextEquals with matching text test passed");
    }

    @Test(description = "Test verifyTextEquals with non-matching text")
    public void testVerifyTextEqualsNonMatching() {
        log.info("Testing verifyTextEquals with non-matching text");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.getText()).thenReturn("Actual Text");

        boolean result = verificationHelper.verifyTextEquals(mockElement, "Expected Text");
        Assert.assertFalse(result, "Non-matching text should return false");

        log.info("verifyTextEquals with non-matching text test passed");
    }

    @Test(description = "Test verifyElementContainsText")
    public void testVerifyElementContainsText() {
        log.info("Testing verifyElementContainsText");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.getText()).thenReturn("This is a test message");

        boolean result = verificationHelper.verifyElementContainsText(mockElement, "test message", "");
        Assert.assertTrue(result, "Element should contain the text");

        log.info("verifyElementContainsText test passed");
    }


    @Test(description = "Test getDomAttribute")
    public void testGetDomAttribute() {
        log.info("Testing getDomAttribute");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.getDomAttribute("id")).thenReturn("test-id");

        String result = verificationHelper.getDomAttribute(mockElement, "id");
        Assert.assertEquals(result, "test-id", "Should return attribute value");

        log.info("getDomAttribute test passed");
    }

    @Test(description = "Test getDomAttribute with null element")
    public void testGetDomAttributeWithNull() {
        log.info("Testing getDomAttribute with null element");
        String result = verificationHelper.getDomAttribute(null, "id");
        Assert.assertNull(result, "Null element should return null");

        log.info("getDomAttribute with null test passed");
    }


    @Test(description = "Test getTitle")
    public void testGetTitle() {
        log.info("Testing getTitle");
        String expectedTitle = "Test Page Title";

        String result = verificationHelper.getTitle();
        Assert.assertNotNull(result, "Title should not be null");

        log.info("getTitle test passed");
    }

    @Test(description = "Test getCurrentPageTitle")
    public void testGetCurrentPageTitle() {
        log.info("Testing getCurrentPageTitle");
        String result = verificationHelper.getCurrentPageTitle();
        Assert.assertNotNull(result, "Current page title should not be null");

        log.info("getCurrentPageTitle test passed");
    }

    @Test(description = "Test getCurrentPageUrl")
    public void testGetCurrentPageUrl() {
        log.info("Testing getCurrentPageUrl");
        String result = verificationHelper.getCurrentPageUrl();
        Assert.assertNotNull(result, "Current page URL should not be null");

        log.info("getCurrentPageUrl test passed");
    }


    @Test(description = "Test isElementPresent with existing element")
    public void testIsElementPresentWithExistingElement() {
        log.info("Testing isElementPresent with existing element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);

        boolean result = verificationHelper.isElementPresent(mockElement);
        Assert.assertTrue(result, "Existing element should return true");

        log.info("isElementPresent test passed");
    }

    @Test(description = "Test isElementPresent with non-existent element")
    public void testIsElementPresentWithNonExistentElement() {
        log.info("Testing isElementPresent with non-existent element");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenThrow(new NoSuchElementException("Element not found"));

        boolean result = verificationHelper.isElementPresent(mockElement);
        Assert.assertFalse(result, "Non-existent element should return false");

        log.info("isElementPresent with non-existent element test passed");
    }

    @Test(description = "Test verifyElementPresent")
    public void testVerifyElementPresent() {
        log.info("Testing verifyElementPresent");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.getText()).thenReturn("Present Element");

        boolean result = verificationHelper.verifyElementPresent(mockElement);
        Assert.assertTrue(result, "Element should be present");

        log.info("verifyElementPresent test passed");
    }

    @Test(description = "Test verifyElementNotPresent")
    public void testVerifyElementNotPresent() {
        log.info("Testing verifyElementNotPresent");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenThrow(new NoSuchElementException("Element not found"));

        boolean result = verificationHelper.verifyElementNotPresent(mockElement);
        Assert.assertTrue(result, "Element should not be present");

        log.info("verifyElementNotPresent test passed");
    }


    @Test(description = "Test areElementsDisplayed with all visible elements")
    public void testAreElementsDisplayedAllVisible() {
        log.info("Testing areElementsDisplayed with all visible elements");
        WebElement element1 = createMockWebElement();
        WebElement element2 = createMockWebElement();
        Mockito.when(element1.isDisplayed()).thenReturn(true);
        Mockito.when(element2.isDisplayed()).thenReturn(true);

        List<WebElement> elements = Arrays.asList(element1, element2);
        boolean result = verificationHelper.areElementsDisplayed(elements);
        Assert.assertTrue(result, "All visible elements should return true");

        log.info("areElementsDisplayed test passed");
    }

    @Test(description = "Test assertItemDisplayedFromList")
    public void testAssertItemDisplayedFromList() {
        log.info("Testing assertItemDisplayedFromList");
        WebElement element1 = createMockWebElement();
        WebElement element2 = createMockWebElement();
        Mockito.when(element1.getText()).thenReturn("Item One");
        Mockito.when(element2.getText()).thenReturn("Item Two");

        List<WebElement> elements = Arrays.asList(element1, element2);
        boolean result = verificationHelper.assertItemDisplayedFromList(elements, "Item One");
        Assert.assertTrue(result, "Item should be found in list");

        result = verificationHelper.assertItemDisplayedFromList(elements, "Item Three");
        Assert.assertFalse(result, "Item should not be found in list");

        log.info("assertItemDisplayedFromList test passed");
    }

    @Test(description = "Test verifyListOfElementInAscendingOrder")
    public void testVerifyListOfElementInAscendingOrder() {
        log.info("Testing verifyListOfElementInAscendingOrder");
        WebElement element1 = createMockWebElement();
        WebElement element2 = createMockWebElement();
        WebElement element3 = createMockWebElement();

        Mockito.when(element1.getText()).thenReturn("Apple");
        Mockito.when(element2.getText()).thenReturn("Banana");
        Mockito.when(element3.getText()).thenReturn("Cherry");

        List<WebElement> elements = Arrays.asList(element1, element2, element3);
        boolean result = verificationHelper.verifyListOfElementInAscendingOrder(elements);
        Assert.assertTrue(result, "Elements in ascending order should return true");

        log.info("verifyListOfElementInAscendingOrder test passed");
    }

    @Test(description = "Test verifyListOfElementInDescendingOrder")
    public void testVerifyListOfElementInDescendingOrder() {
        log.info("Testing verifyListOfElementInDescendingOrder");
        WebElement element1 = createMockWebElement();
        WebElement element2 = createMockWebElement();
        WebElement element3 = createMockWebElement();

        Mockito.when(element1.getText()).thenReturn("Cherry");
        Mockito.when(element2.getText()).thenReturn("Banana");
        Mockito.when(element3.getText()).thenReturn("Apple");

        List<WebElement> elements = Arrays.asList(element1, element2, element3);
        boolean result = verificationHelper.verifyListOfElementInDescendingOrder(elements);
        Assert.assertTrue(result, "Elements in descending order should return true");

        log.info("verifyListOfElementInDescendingOrder test passed");
    }


    @Test(description = "Test verifyElementSelectedFromDropdownList")
    public void testVerifyElementSelectedFromDropdownList() {
        log.info("Testing verifyElementSelectedFromDropdownList");
        WebElement dropdownElement = createMockWebElement();
        WebElement selectedOption = createMockWebElement();
        Mockito.when(selectedOption.getText()).thenReturn("Option 1");


        log.info("verifyElementSelectedFromDropdownList test setup completed");
    }


    @Test(description = "Test verifyElementIsClickable")
    public void testVerifyElementIsClickable() {
        log.info("Testing verifyElementIsClickable");


        log.info("verifyElementIsClickable test setup completed");
    }

    @Test(description = "Test isElementVisibleEnabledAndPresent")
    public void testIsElementVisibleEnabledAndPresent() {
        log.info("Testing isElementVisibleEnabledAndPresent");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.isDisplayed()).thenReturn(true);
        Mockito.when(mockElement.isEnabled()).thenReturn(true);

        boolean result = verificationHelper.isElementVisibleEnabledAndPresent(mockElement);
        Assert.assertTrue(result, "Visible and enabled element should return true");

        log.info("isElementVisibleEnabledAndPresent test passed");
    }


    private WebElement createMockWebElement() {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class},
                (proxy, method, args) -> {

                    if ("isDisplayed".equals(method.getName())) return false;
                    if ("isEnabled".equals(method.getName())) return true;
                    if ("isSelected".equals(method.getName())) return false;
                    if ("getText".equals(method.getName())) return "";
                    return null;
                }
        );
    }


    @DataProvider(name = "validRgbValues")
    public Object[][] validRgbValues() {
        return new Object[][]{
                {255, 0, 0, "#FF0000"},
                {0, 255, 0, "#00FF00"},
                {0, 0, 255, "#0000FF"},
                {0, 0, 0, "#000000"},
                {255, 255, 255, "#FFFFFF"},
                {128, 128, 128, "#808080"}
        };
    }


    @Test(dataProvider = "validRgbValues", description = "Test rgbToHex with various RGB values")
    public void testRgbToHexParametrized(int red, int green, int blue, String expected) {
        log.info("Testing rgbToHex: RGB(" + red + ", " + green + ", " + blue + ") = " + expected);
        String result = VerificationHelper.rgbToHex(red, green, blue);
        Assert.assertEquals(result, expected, "RGB conversion should match expected HEX");
    }


    @DataProvider(name = "validHexValues")
    public Object[][] validHexValues() {
        return new Object[][]{
                {"#FF0000", new int[]{255, 0, 0}},
                {"#00FF00", new int[]{0, 255, 0}},
                {"#0000FF", new int[]{0, 0, 255}},
                {"#000000", new int[]{0, 0, 0}},
                {"#FFFFFF", new int[]{255, 255, 255}},
                {"#808080", new int[]{128, 128, 128}}
        };
    }


    @Test(dataProvider = "validHexValues", description = "Test hexToRgb with various HEX values")
    public void testHexToRgbParametrized(String hex, int[] expected) {
        log.info("Testing hexToRgb: " + hex + " = RGB(" + expected[0] + ", " + expected[1] + ", " + expected[2] + ")");
        int[] result = VerificationHelper.hexToRgb(hex);
        Assert.assertEquals(result, expected, "HEX conversion should match expected RGB");
    }


    @DataProvider(name = "displayStates")
    public Object[][] displayStates() {
        return new Object[][]{
                {true, "Displayed element"},
                {false, "Hidden element"}
        };
    }


    @DataProvider(name = "textContent")
    public Object[][] textContent() {
        return new Object[][]{
                {"Sample Text", "Sample Text", true},
                {"Sample Text", "Different Text", false},
                {"", "", true}
        };
    }


    @Test(dataProvider = "textContent", description = "Test text verification with various content")
    public void testTextVerificationParametrized(String actualText, String expectedText, boolean shouldMatch) {
        log.info("Testing text verification: '" + actualText + "' vs '" + expectedText + "'");
        WebElement mockElement = createMockWebElement();
        Mockito.when(mockElement.getText()).thenReturn(actualText);

        boolean result = verificationHelper.verifyTextEquals(mockElement, expectedText);
        Assert.assertEquals(result, shouldMatch, "Text verification result should match expectation");
    }
}
