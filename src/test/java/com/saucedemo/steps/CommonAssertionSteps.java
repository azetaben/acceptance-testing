package com.saucedemo.steps;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.pages.Page;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Map;


public class CommonAssertionSteps {
    private static final Logger log = LogManager.getLogger(CommonAssertionSteps.class);

    private final PageManager pm = PageManager.getInstance();

    private Page page() {
        return pm.getPage(Page.class);
    }

    @Then("the page correct heading should be {string}")
    public void the_page_correct_heading_should_be(String heading) {
        checkHeading(heading);
    }

    @Then("I am on the {string} page")
    public void checkHeadingOnThePage(String heading) {
        checkHeading(heading);
    }

    @Then("I should be taken to the {string} page")
    public void checkHeading(String heading) {
        Page page = page();
        page.isPageFullyLoaded();
        String expectedHeadingText;
        String lowerCaseInputHeading = heading.toLowerCase();
        switch (lowerCaseInputHeading) {
            case "products":
                expectedHeadingText = SauceDemoConstants.HEADING_PRODUCTS;
                break;
            case "cart":
                expectedHeadingText = SauceDemoConstants.HEADING_YOUR_CART;
                break;
            case "checkout your information":
            case "checkout: your information":
                expectedHeadingText = SauceDemoConstants.HEADING_CHECKOUT_YOUR_INFORMATION;
                break;
            case "checkout overview":
            case "checkout: overview":
                expectedHeadingText = SauceDemoConstants.HEADING_CHECKOUT_OVERVIEW;
                break;
            case "checkout complete":
            case "checkout: complete":
                expectedHeadingText = SauceDemoConstants.HEADING_CHECKOUT_COMPLETE;
                break;
            default:
                expectedHeadingText = heading;
                log.info(String.valueOf("INFO: No specific heading mapping for '" + heading + "'. Using the provided string '" + expectedHeadingText + "' directly for assertion."));
                break;
        }
        log.info(String.valueOf("INFO: Attempting to assert page heading. Expected: '" + expectedHeadingText + "' (derived from input: '" + heading + "')"));
        Assert.assertTrue(page.isHeadingTextDisplayed(expectedHeadingText),
                "Expected heading text '" + expectedHeadingText + "' was not found on the page.");
    }

    @Then("I should be redirected to products page as {string}")
    public void iShouldBeRedirectedToTheProductsPageAs(String urlPath) {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        assert currentUrl != null;
        Assert.assertTrue(currentUrl.contains(urlPath), "User is not redirected to the expected page.");
    }

    @Then("I should be redirected to the products page")
    public void iShouldBeRedirectedToProductsPage() {
        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        assert currentUrl != null;
        Assert.assertTrue(currentUrl.contains(SauceDemoConstants.INVENTORY_PAGE_PATH), "User is not redirected to the expected page.");
    }

    @And("page url and title should be:")
    public void pagePropertiesShouldBeAsFollows(List<Map<String, String>> dataTable) {
        Map<String, String> data = dataTable.get(0);
        Assert.assertEquals(data.get("url"), page().getThisPageUrl(), "Page URL is not as expected");
        Assert.assertEquals(data.get("title"), page().getThisPageTitle(), "Page Title is not as expected");
    }

    @And("page title should be {string}")
    public void pageTitleShouldBe(String expectedTitle) {
        Assert.assertTrue(expectedTitle.contains(page().getThisPageTitle()), "Expected page title to contain '" + expectedTitle + "' but found '" + page().getThisPageTitle() + "'.");
    }

    @And("page url should be {string}")
    public void pageUrlShouldBe(String expectedPathUrl) {
        Assert.assertTrue(page().getThisPageUrl().contains(expectedPathUrl));
    }

    @Then("the page heading should be {string}")
    public void verifyPageHeader(String heading) {
        Page page = page();
        WebDriverWait headingWait = new WebDriverWait(WebDrv.getInstance().getWebDriver(), Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));
        ExplicitWaitFactory.setDriver(WebDrv.getInstance().getWebDriver());

        try {
            headingWait.until(driver -> {
                String currentHeading = page.getThisPageSubHeaderText();
                return currentHeading != null && currentHeading.contains(heading);
            });
        } catch (TimeoutException ignored) {
        }

        String actualHeading = page.getThisPageSubHeaderText();
        Assert.assertTrue(actualHeading.contains(heading),
                "Expected heading to contain '" + heading + "' but found '" + actualHeading + "'.");
    }

    @Then("I should see the page title as {string}")
    public void i_should_see_the_page_title_as(String expectedPageTitle) {
        Assert.assertTrue(page().getThisPageTitle().contains(expectedPageTitle));
    }

    @When("I verify the page contains {string}")
    public void iVerifyThePageContains(String expectedText) {
        boolean isTextPresent = page().verifyPageContentByText(expectedText);
        Assert.assertTrue(isTextPresent, "The expected text was not found on the page.");
    }

    @And("logo is displayed")
    public void logoIsDisplayed() {
        Assert.assertTrue(page().isLogoIsDisplayed());
    }
}
