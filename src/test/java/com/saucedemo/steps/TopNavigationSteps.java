package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.pages.TopNavigationLinksPage;
import io.cucumber.java.en.And;

public class TopNavigationSteps {
    private final PageManager pm;

    public TopNavigationSteps() {
        this.pm = PageManager.getInstance();
    }

    private TopNavigationLinksPage topNaviLinksPage() {
        return this.pm.getPage(TopNavigationLinksPage.class);
    }

    @And("I tap on the toggle button")
    public void iTapOnTopLeftMenuIcon() {
        topNaviLinksPage().clickMenuIcon();
    }

    @And("I tap on {string} link")
    public void iTapOnLink(String linkName) {
        switch (linkName) {
            case "Cart" -> topNaviLinksPage().clickCartIcon();
        }
    }
}
