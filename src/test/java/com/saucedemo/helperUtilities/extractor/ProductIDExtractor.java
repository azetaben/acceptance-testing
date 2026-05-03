package com.saucedemo.helperUtilities.extractor;

import com.saucedemo.helperUtilities.string.StringSplitter;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class ProductIDExtractor {
    private static WebDriver driver;

    public static String extractProductIDFromCurrentPageUrl() {
        String currentUrl = driver.getCurrentUrl();
        List<String> urlSegments = StringSplitter.splitStringIntoAnArrayWithADelimiterAndReturnIt("/", currentUrl);
        String ProductID = "";

        if (urlSegments.size() >= 4) {
            ProductID = urlSegments.get(4);
        }

        return ProductID;
    }
}
