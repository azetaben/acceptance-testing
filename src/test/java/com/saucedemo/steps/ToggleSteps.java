package com.saucedemo.steps;

import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TogglePage;
import io.cucumber.java.en.And;

public class ToggleSteps {
    private final PageManager pm;

    public ToggleSteps() {
        this.pm = PageManager.getInstance();
    }

    private TogglePage togglePage() {
        return this.pm.getPage(TogglePage.class);
    }

    @And("I click on the logout link")
    public void iClickOnTheLogoutLink() {
        togglePage().clickOnTopLeftMenuItem(SauceDemoConstants.MENU_ITEM_LOGOUT);
    }

    @And("I click on the {string} link")
    public void iClickOnTheLink(String itemName) {
        togglePage().clickOnTopLeftMenuItem(itemName);
    }

    @And("I tap on the cross X button")
    public void iCloseTheLeftMenuIcon() {
        togglePage().clickCrossButton();
    }
}
