package com.saucedemo.utils;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import org.apache.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class PageElementChecker {

    private static final org.apache.log4j.Logger log = LogManager.getLogger(PageElementChecker.class);
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;
    private final WebDriver driver;

    public PageElementChecker(WebDriver driver) {
        this.driver = driver;
        ExplicitWaitFactory.setDriver(driver);
    }

    // Method to check for broken links
    public void checkBrokenLinks() {
        List<WebElement> links = getElements(By.tagName("a"));
        String baseUrl = getCurrentUrlSafe();

        links.stream()
                .map(link -> link.getDomAttribute("href"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(href -> !href.isEmpty())
                .map(href -> normalizeToHttpUrl(baseUrl, href))
                .filter(Objects::nonNull)
                .distinct()
                .forEach(this::verifyLink);
    }

    // Method to find buttons on the page
    public void findButtons() {
        List<WebElement> buttons = getElements(By.tagName("button"));
        log.info("Found " + buttons.size() + " buttons on the page.");
        buttons.forEach(button -> {
            log.info("Button type: " + button.getDomAttribute("type"));
            log.info("Button text: " + button.getText());
        });
    }

    // Method to find input fields on the page
    public void findInputFields() {
        List<WebElement> inputFields = getElements(By.tagName("input"));
        log.info("Found " + inputFields.size() + " input fields on the page.");
        inputFields.forEach(inputField -> {
            log.info("Input field type: " + inputField.getDomAttribute("type"));
        });
    }

    public void imageValidation() {
        List<WebElement> images = getElements(By.tagName("img"));
        images.stream()
                .map(image -> image.getDomAttribute("src"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(src -> !src.isEmpty())
                .filter(src -> !src.startsWith("data:image"))
                .forEach(src -> {
                    log.info("Image source: " + src);
                });
        log.info("Found " + images.size() + " images on the page.");
    }

    private List<WebElement> getElements(By locator) {
        try {
            List<WebElement> result = ExplicitWaitFactory.performExplicitWaitForList(
                    WaitStrategy.PRESENCE_OF_ALL_ELEMENTS_LOCATED, locator);
            return result != null ? result : driver.findElements(locator);
        } catch (RuntimeException e) {
            // No elements of this type exist on the page — return empty list, not an error.
            log.debug("No elements found for locator " + locator + ": " + e.getMessage());
            return driver.findElements(locator);
        }
    }

    private String getCurrentUrlSafe() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            // If the driver isn't on a page yet, we can still validate absolute URLs.
            log.debug("Unable to read current URL from WebDriver: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns an absolute http(s) URL string or null if the href should be ignored.
     */
    private String normalizeToHttpUrl(String baseUrl, String href) {
        // Ignore common non-navigation links.
        String lower = href.toLowerCase();
        if (lower.startsWith("#") || lower.startsWith("javascript:") || lower.startsWith("mailto:") || lower.startsWith("tel:")) {
            return null;
        }

        try {
            URI uri = new URI(href);

            // Resolve relative links against the current page URL.
            if (!uri.isAbsolute()) {
                if (baseUrl == null || baseUrl.trim().isEmpty()) return null;
                uri = new URI(baseUrl).resolve(uri);
            }

            String scheme = uri.getScheme();
            if (scheme == null) return null;

            scheme = scheme.toLowerCase();
            if (!scheme.equals("http") && !scheme.equals("https")) {
                return null;
            }

            return uri.toString();
        } catch (URISyntaxException e) {
            log.warn("Skipping invalid URL in href: " + href);
            return null;
        }
    }

    // Method to verify the link status
    private void verifyLink(String linkUrl) {
        log.info("Checking link: " + linkUrl);

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(linkUrl).openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            httpURLConnection.setReadTimeout(READ_TIMEOUT_MS);
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();

            // Some servers/CDNs don't support HEAD and return 405/501.
            if (responseCode == HttpURLConnection.HTTP_BAD_METHOD || responseCode == HttpURLConnection.HTTP_NOT_IMPLEMENTED) {
                httpURLConnection.disconnect();
                httpURLConnection = (HttpURLConnection) new URL(linkUrl).openConnection();
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
                httpURLConnection.setReadTimeout(READ_TIMEOUT_MS);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
            }

            if (responseCode >= 400) {
                log.warn("Broken link: " + linkUrl + " - Response Code: " + responseCode);
            } else {
                log.info("Valid link: " + linkUrl + " - Response Code: " + responseCode);
            }
        } catch (IOException e) {
            log.error("Error checking link: " + linkUrl + " - " + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}

