package com.saucedemo.steps;

import com.saucedemo.helperUtilities.assertion.AssertionHelper;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Step definitions demonstrating the three AssertionHelper tab/list utility methods:
 *
 *   assertNewTabOpenedWithExpectedTitle(String)
 *     — switches to the newly opened tab, asserts driver.getTitle() matches, then
 *       closes the tab and returns to the original window.
 *
 *   assertNewTabIsOpenedWithExpectedPage(String)
 *     — switches to the newly opened tab, asserts By.id("heading").getText() matches,
 *       then closes the tab and returns to the original window.
 *       The "When" step writes controlled HTML into the tab so the heading element
 *       is guaranteed to exist regardless of which URL was used.
 *
 *   getModifiableIdListOfExpectedChildElements(List<String>)
 *     — returns a new ArrayList copy. Demonstrated by mutating the copy and
 *       verifying the original list is unaffected.
 */
public class AssertionHelperTabSteps {

    private final PageManager pm = PageManager.getInstance();

    private InventoryPage inventoryPage() {
        return pm.getPage(InventoryPage.class);
    }

    // ── TC_AH_TAB_001  assertNewTabOpenedWithExpectedTitle ───────────────────

    @When("I open {string} in a new browser tab")
    public void iOpenInANewBrowserTab(String url) {
        inventoryPage().executeJavaScript("window.open('" + url + "', '_blank')");
    }

    @Then("the new tab should have title {string}")
    public void theNewTabShouldHaveTitle(String expectedTitle) {
        AssertionHelper.assertNewTabOpenedWithExpectedTitle(expectedTitle);
    }

    // ── TC_AH_TAB_002  assertNewTabIsOpenedWithExpectedPage ──────────────────
    // assertNewTabIsOpenedWithExpectedPage looks for By.id("heading") on the new tab.
    // Instead of relying on an external URL having that element, the step writes
    // a minimal HTML page into the fresh tab, guaranteeing the id="heading" exists.

    @When("I open a new browser tab with a heading element containing {string}")
    public void iOpenANewBrowserTabWithAHeadingElementContaining(String headingText) {
        String script =
                "var w = window.open('', '_blank');" +
                "w.document.write('<html><body>" +
                "<h1 id=\"heading\">" + headingText + "</h1>" +
                "</body></html>');" +
                "w.document.close();";
        inventoryPage().executeJavaScript(script);
    }

    @Then("the new tab page heading should be {string}")
    public void theNewTabPageHeadingShouldBe(String expectedHeading) {
        AssertionHelper.assertNewTabIsOpenedWithExpectedPage(expectedHeading);
    }

    // ── TC_AH_TAB_003  getModifiableIdListOfExpectedChildElements ────────────
    // Demonstrates that the returned copy is a fully independent list:
    // adding an element to the copy must not change the original's size or content.

    @Then("a modifiable copy of {string} should not affect the original")
    public void aModifiableCopyOfIdsShouldNotAffectOriginal(String csvIds) {
        List<String> original = new ArrayList<>(Arrays.asList(csvIds.split(",")));
        int originalSize = original.size();

        List<String> copy = AssertionHelper.getModifiableIdListOfExpectedChildElements(original);
        copy.add("inventory_container");

        AssertionHelper.verifyTrue(copy.size() == originalSize + 1);
        AssertionHelper.verifyFalse(original.contains("inventory_container"));
        AssertionHelper.verifyTrue(original.size() == originalSize);
    }
}
