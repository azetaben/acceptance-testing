package com.saucedemo.helperUtilities.extractor;

import com.saucedemo.helperUtilities.string.StringSplitter;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class InvoiceNumberExtractor {
    private static WebDriver driver;

    public static String extractInvoiceNumberFromCurrentPageUrl() {
        String currentUrl = driver.getCurrentUrl();
        List<String> urlSegments = StringSplitter.splitStringIntoAnArrayWithADelimiterAndReturnIt("/", currentUrl);
        String invoiceNumber = "";

        if (urlSegments.size() >= 4) {
            invoiceNumber = urlSegments.get(4);
        }

        return invoiceNumber;
    }
}
