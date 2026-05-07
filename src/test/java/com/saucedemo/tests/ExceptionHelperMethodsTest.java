package com.saucedemo.tests;

import com.saucedemo.exceptions.CriteriaSetNotMoreThanOneException;
import com.saucedemo.exceptions.NoSuchElementsException;
import com.saucedemo.exceptions.NotOnExpectedPageException;
import com.saucedemo.exceptions.TableRowDoesNotExistException;
import com.saucedemo.helperutilities.pageexception.PageException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExceptionHelperMethodsTest {

    private static WebElement anyWebElement() {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class},
                (proxy, method, args) -> null
        );
    }

    private static WebDriver webDriverWithCurrentUrl(String currentUrl) {
        return (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                new Class[]{WebDriver.class},
                (proxy, method, args) -> {
                    if ("getCurrentUrl".equals(method.getName())) {
                        return currentUrl;
                    }
                    return null;
                }
        );
    }

    @AfterMethod
    public void resetStaticDriver() {
        NotOnExpectedPageException.setDriver(null);
    }

    @Test
    public void checkIfCriteriaSetIsOverOne_allowsValuesGreaterThanOne() {
        CriteriaSetNotMoreThanOneException.checkIfCriteriaSetIsOverOneOrElseThrowException("ignored", 2);
    }

    @Test
    public void checkIfCriteriaSetIsOverOne_throwsWithDefaultMessageWhenMessageIsBlank() {
        try {
            CriteriaSetNotMoreThanOneException.checkIfCriteriaSetIsOverOneOrElseThrowException("  ", 1);
            Assert.fail("Expected CriteriaSetNotMoreThanOneException");
        } catch (CriteriaSetNotMoreThanOneException ex) {
            Assert.assertEquals(ex.getMessage(), "Criteria set number must be greater than one.");
        }
    }

    @Test
    public void checkIfCriteriaSetIsOverOne_throwsWithCustomMessage() {
        try {
            CriteriaSetNotMoreThanOneException.checkIfCriteriaSetIsOverOneOrElseThrowException("custom criteria error", 0);
            Assert.fail("Expected CriteriaSetNotMoreThanOneException");
        } catch (CriteriaSetNotMoreThanOneException ex) {
            Assert.assertEquals(ex.getMessage(), "custom criteria error");
        }
    }

    @Test
    public void checkIfWebElementListContainsElement_allowsValidElementAtIndex() {
        List<WebElement> webElements = Collections.singletonList(anyWebElement());
        NoSuchElementsException.checkIfWebElementListContainsElementOrElseThrowNoSuchElementException(webElements, 0);
    }

    @Test
    public void checkIfWebElementListContainsElement_throwsWhenListIsNull() {
        try {
            NoSuchElementsException.checkIfWebElementListContainsElementOrElseThrowNoSuchElementException(null, 0);
            Assert.fail("Expected NoSuchElementsException");
        } catch (NoSuchElementsException ex) {
            Assert.assertEquals(ex.getMessage(), "No web elements were found in the provided list.");
        }
    }

    @Test
    public void checkIfWebElementListContainsElement_throwsWhenIndexIsInvalid() {
        List<WebElement> webElements = Collections.singletonList(anyWebElement());
        try {
            NoSuchElementsException.checkIfWebElementListContainsElementOrElseThrowNoSuchElementException(webElements, 2);
            Assert.fail("Expected NoSuchElementsException");
        } catch (NoSuchElementsException ex) {
            Assert.assertEquals(ex.getMessage(), "No web element exists at index: 2");
        }
    }

    @Test
    public void checkIfWebElementListContainsElement_throwsWhenElementAtIndexIsNull() {
        List<WebElement> webElements = Collections.singletonList((WebElement) null);
        try {
            NoSuchElementsException.checkIfWebElementListContainsElementOrElseThrowNoSuchElementException(webElements, 0);
            Assert.fail("Expected NoSuchElementsException");
        } catch (NoSuchElementsException ex) {
            Assert.assertEquals(ex.getMessage(), "Web element at index 0 is null.");
        }
    }

    @Test
    public void checkIfTableRowExists_allowsValidRowAtIndex() {
        List<WebElement> rows = Collections.singletonList(anyWebElement());
        TableRowDoesNotExistException.checkIfTableRowExistsOrElseThrowException("ignored", rows, 0);
    }

    @Test
    public void checkIfTableRowExists_throwsDefaultMessageWhenRowsAreMissing() {
        try {
            TableRowDoesNotExistException.checkIfTableRowExistsOrElseThrowException(null, Collections.<WebElement>emptyList(), 0);
            Assert.fail("Expected TableRowDoesNotExistException");
        } catch (TableRowDoesNotExistException ex) {
            Assert.assertEquals(ex.getMessage(), "The specified row does not exist within the table.");
        }
    }

    @Test
    public void checkIfTableRowExists_throwsCustomMessageWhenIndexIsInvalid() {
        List<WebElement> rows = Collections.singletonList(anyWebElement());
        try {
            TableRowDoesNotExistException.checkIfTableRowExistsOrElseThrowException("custom row error", rows, 9);
            Assert.fail("Expected TableRowDoesNotExistException");
        } catch (TableRowDoesNotExistException ex) {
            Assert.assertEquals(ex.getMessage(), "custom row error");
        }
    }

    @Test
    public void checkIfTableRowExists_throwsWhenRowIsNull() {
        List<WebElement> rows = Arrays.asList((WebElement) null);
        try {
            TableRowDoesNotExistException.checkIfTableRowExistsOrElseThrowException("row is null", rows, 0);
            Assert.fail("Expected TableRowDoesNotExistException");
        } catch (TableRowDoesNotExistException ex) {
            Assert.assertEquals(ex.getMessage(), "row is null");
        }
    }

    @Test
    public void checkIfOnTheCorrectPage_allowsMatchingUrlWithDriverParameter() throws PageException {
        WebDriver webDriver = webDriverWithCurrentUrl("https://expected/page");

        NotOnExpectedPageException.checkIfOnTheCorrectPageOtherwiseThrowException(
                webDriver,
                "https://expected/page",
                "ignored"
        );
    }

    @Test
    public void checkIfOnTheCorrectPage_throwsWithResolvedDefaultMessageWhenUrlMismatches() {
        WebDriver webDriver = webDriverWithCurrentUrl("https://actual/page");

        try {
            NotOnExpectedPageException.checkIfOnTheCorrectPageOtherwiseThrowException(
                    webDriver,
                    "https://expected/page",
                    null
            );
            Assert.fail("Expected NotOnExpectedPageException");
        } catch (NotOnExpectedPageException ex) {
            Assert.assertEquals(ex.getMessage(), "Expected URL 'https://expected/page' but found 'https://actual/page'.");
        } catch (PageException pageException) {
            Assert.fail("Did not expect PageException", pageException);
        }
    }

    @Test
    public void checkIfOnTheCorrectPage_throwsPageExceptionWhenProvidedDriverIsNull() {
        try {
            NotOnExpectedPageException.checkIfOnTheCorrectPageOtherwiseThrowException(
                    (WebDriver) null,
                    "https://expected/page",
                    "ignored"
            );
            Assert.fail("Expected PageException");
        } catch (PageException ex) {
            Assert.assertEquals(ex.getMessage(), "Provided WebDriver is null.");
        }
    }

    @Test
    public void checkIfOnTheCorrectPage_staticOverloadThrowsPageExceptionWhenDriverNotInitialized() {
        try {
            NotOnExpectedPageException.checkIfOnTheCorrectPageOtherwiseThrowException(
                    "https://expected/page",
                    "ignored"
            );
            Assert.fail("Expected PageException");
        } catch (PageException ex) {
            Assert.assertEquals(ex.getMessage(), "WebDriver is not initialized for page validation.");
        }
    }

    @Test
    public void checkIfOnTheCorrectPage_staticOverloadUsesConfiguredDriver() throws PageException {
        WebDriver webDriver = webDriverWithCurrentUrl("https://expected/page");
        NotOnExpectedPageException.setDriver(webDriver);

        NotOnExpectedPageException.checkIfOnTheCorrectPageOtherwiseThrowException(
                "https://expected/page",
                "ignored"
        );
    }
}
