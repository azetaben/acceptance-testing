package com.saucedemo.steps;

import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.Page;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Locale;

public class SecuritySteps {
    private final PageManager pm = PageManager.getInstance();

    private Page page() {
        return pm.getPage(Page.class);
    }

    private LoginPage loginPage() {
        return pm.getPage(LoginPage.class);
    }

    @Then("the {string} page url, title, and heading should be correct")
    public void thePageUrlTitleAndHeadingShouldBeCorrect(String pageName) {
        String normalized = pageName == null ? "" : pageName.trim().toLowerCase(Locale.ROOT);

        if ("login".equals(normalized)) {
            String expectedLoginUrl = FrameworkConfig.getInstance().getString("login_url",
                    FrameworkConfig.getInstance().getString("url", "https://www.saucedemo.com"));
            String actualUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
            String actualTitle = WebDrv.getInstance().getWebDriver().getTitle();

            Assert.assertNotNull(actualUrl, "Current URL was null");
            Assert.assertTrue(normalizeUrl(actualUrl).startsWith(normalizeUrl(expectedLoginUrl)),
                    "Expected login URL to start with '" + expectedLoginUrl + "' but was '" + actualUrl + "'");
            Assert.assertTrue(actualTitle != null && actualTitle.contains("Swag Labs"),
                    "Expected page title to contain 'Swag Labs' but was '" + actualTitle + "'");
            Assert.assertTrue(page().isLogoIsDisplayed(), "Expected login page logo/heading to be displayed");
            return;
        }

        throw new IllegalArgumentException("Unsupported page name for url/title/heading check: " + pageName);
    }

    @Given("I attempt to navigate directly to the product page")
    public void iAttemptToNavigateDirectlyToTheProductPage() {
        String baseUrl = FrameworkConfig.getInstance().getString("url", "https://www.saucedemo.com");
        String path = SauceDemoConstants.INVENTORY_PAGE_PATH == null ? "inventory.html" : SauceDemoConstants.INVENTORY_PAGE_PATH;
        WebDrv.getInstance().getWebDriver().navigate().to(joinUrl(baseUrl, path));
    }

    @Given("I attempt to navigate directly to the cart page")
    public void iAttemptToNavigateDirectlyToTheCartPage() {
        String baseUrl = FrameworkConfig.getInstance().getString("url", "https://www.saucedemo.com");
        WebDrv.getInstance().getWebDriver().navigate().to(joinUrl(baseUrl, "cart.html"));
    }

    @When("I input login name as {string}")
    public void iInputLoginNameAs(String username) {
        loginPage().enterUsername(username);
    }


    @Then("the page URL should not contain any SQL keywords")
    public void thePageUrlShouldNotContainAnySqlKeywords() {
        String url = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        Assert.assertNotNull(url, "Current URL was null");
        String lower = url.toLowerCase(Locale.ROOT);

        // Heuristic: ensure the URL does not reflect typical injection tokens/keywords.
        String[] disallowed = new String[]{
                "select", "union", "drop", "insert", "update", "delete",
                "--", "/*", "*/", "%27", "%22", "%3d", "%3b", "%23", "%2d%2d",
                "' or '", "\" or \"", " or 1=1", " or 1=1"
        };
        for (String token : disallowed) {
            Assert.assertFalse(lower.contains(token),
                    "URL contained a disallowed SQL token '" + token + "': " + url);
        }
    }

    private static String normalizeUrl(String url) {
        if (url == null) return "";
        String trimmed = url.trim();
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    private static String joinUrl(String baseUrl, String path) {
        String base = baseUrl == null ? "" : baseUrl.trim();
        String p = path == null ? "" : path.trim();
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        if (p.startsWith("/")) p = p.substring(1);
        return base + "/" + p;
    }
}

