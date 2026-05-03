package com.saucedemo.exceptions;

import org.openqa.selenium.WebElement;

import java.io.Serial;
import java.util.List;

public class NoSuchElementsException extends FrameworkException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoSuchElementsException(String message) {
        super(message);
    }

    public NoSuchElementsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void checkIfWebElementListContainsElementOrElseThrowNoSuchElementException(List<WebElement> webElements, int webElementIndexToBeRetrieved) {
        if (webElements == null || webElements.isEmpty()) {
            throw new NoSuchElementsException("No web elements were found in the provided list.");
        }
        if (webElementIndexToBeRetrieved < 0 || webElementIndexToBeRetrieved >= webElements.size()) {
            throw new NoSuchElementsException("No web element exists at index: " + webElementIndexToBeRetrieved);
        }
        if (webElements.get(webElementIndexToBeRetrieved) == null) {
            throw new NoSuchElementsException("Web element at index " + webElementIndexToBeRetrieved + " is null.");
        }
    }
}
