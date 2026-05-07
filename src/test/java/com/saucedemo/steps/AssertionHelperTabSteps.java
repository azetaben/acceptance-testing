package com.saucedemo.steps;

import com.saucedemo.helperutilities.assertion.AssertionHelper;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AssertionHelperTabSteps {

    private final PageManager pm = PageManager.getInstance();

    private InventoryPage inventoryPage() {
        return pm.getPage(InventoryPage.class);
    }


    @When("I open {string} in a new browser tab")
    public void iOpenInANewBrowserTab(String url) {
        inventoryPage().executeJavaScript("window.open('" + url + "', '_blank')");
    }

    @Then("the new tab should have title {string}")
    public void theNewTabShouldHaveTitle(String expectedTitle) {
        AssertionHelper.assertNewTabOpenedWithExpectedTitle(expectedTitle);
    }


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
