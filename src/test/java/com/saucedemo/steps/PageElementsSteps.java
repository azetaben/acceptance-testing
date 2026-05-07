package com.saucedemo.steps;

import com.saucedemo.pages.PageManager;
import com.saucedemo.utils.PageElementChecker;
import io.cucumber.java.en.Then;

public class PageElementsSteps {
    private final PageManager pm;

    public PageElementsSteps() {
        this.pm = PageManager.getInstance();
    }

    private PageElementChecker pageElementChecker() {
        return this.pm.getPage(PageElementChecker.class);
    }

    @Then("I check for broken links")
    public void i_check_for_broken_links() {
        pageElementChecker().checkBrokenLinks();
    }

    @Then("I verify the buttons on the page")
    public void i_find_buttons_on_the_page() {
        pageElementChecker().findButtons();
    }

    @Then("I find input fields on the page")
    public void i_find_input_fields_on_the_page() {
        pageElementChecker().findInputFields();
    }

}
