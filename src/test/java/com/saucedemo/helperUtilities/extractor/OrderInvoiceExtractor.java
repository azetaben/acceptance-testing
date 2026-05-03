package com.saucedemo.helperUtilities.extractor;

import com.saucedemo.helperUtilities.string.StringSplitter;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class OrderInvoiceExtractor {
    private static WebDriver driver;

    public static String extractOrderInvoiceFromCurrentPageUrl() {
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        List<String> urlSegments = StringSplitter.splitStringIntoAnArrayWithADelimiterAndReturnIt("/", currentUrl);
        String orderInvoiceNumber = "";

        if (urlSegments.size() >= 4) {
            orderInvoiceNumber = urlSegments.get(4);
        }

        return orderInvoiceNumber;
    }
}
