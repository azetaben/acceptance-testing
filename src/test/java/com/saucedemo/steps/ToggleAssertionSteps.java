package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TogglePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.testng.Assert;

import java.util.List;

public class ToggleAssertionSteps {
    private final PageManager pm;

    public ToggleAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private TogglePage togglePage() {
        return this.pm.getPage(TogglePage.class);
    }

    @And("I should see the following links:")
    public void iShouldSeeTheFollowingLinks(DataTable dataTable) {
        String actualLinksText = togglePage().getTopLeftMenuItems();
        List<String> expectedLinks = dataTable.asList();

        for (String expectedLink : expectedLinks) {
            Assert.assertTrue(actualLinksText != null && actualLinksText.contains(expectedLink),
                    "Expected menu link not found: '" + expectedLink + "'. Actual links: " + actualLinksText);
        }
    }
}
