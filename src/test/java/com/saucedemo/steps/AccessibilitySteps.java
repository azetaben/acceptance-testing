package com.saucedemo.steps;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.utils.AccessibilityChecker;
import com.saucedemo.utils.AccessibilityTestUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.openqa.selenium.By;

import java.time.Duration;

public class AccessibilitySteps {
    private static final ThreadLocal<JSONArray> VIOLATIONS = new ThreadLocal<>();
    private static final ThreadLocal<Results> ACCESSIBILITY_RESULTS = new ThreadLocal<>();
    private final PageManager pm;

    public AccessibilitySteps() {
        this.pm = PageManager.getInstance();
    }

    static Results getAccessibilityResults() {
        return ACCESSIBILITY_RESULTS.get();
    }

    static JSONArray getViolations() {
        return VIOLATIONS.get();
    }

    private LoginPage loginPage() {
        return this.pm.getPage(LoginPage.class);
    }

    @When("I check the login page for accessibility violations")
    public void iCheckTheLoginPageForAccessibilityViolations() {
        AccessibilityTestUtil.runAccessibilityTest(WebDrv.getInstance().getWebDriver());
    }

    @Given("I log in to Sauce Demo successfully")
    public void iLogInToSauceDemoSuccessfully() {
        loginPage().login(GlobalVarsHelper.get_standard_user(), GlobalVarsHelper.getPasswordForAllUsers());
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(GlobalVarsHelper.TWO));
    }

    @When("I check the inventory page for accessibility violations")
    public void iCheckTheInventoryPageForAccessibilityViolations() {
        //loginPage().login(GlobalVarsHelper.get_standard_user(), GlobalVarsHelper.getPasswordForAllUsers());
        loginPage().login("standard_user", "secret_sauce");
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(GlobalVarsHelper.TWO));
        AccessibilityTestUtil.runAccessibilityTest(WebDrv.getInstance().getWebDriver());
    }

    @Then("I check the accessibility of the page {string}")
    public void iCheckTheAccessibilityOfThePage(String url) {
        AccessibilityChecker accessibilityChecker = new AccessibilityChecker();
        accessibilityChecker.checkAccessibility(url);
    }

    @When("Check the accessibility of the page {string}")
    public void i_check_the_accessibility_of_the_page(String url) {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, By.tagName("body"));

        AxeBuilder axeBuilder = new AxeBuilder();
        Results results = axeBuilder.analyze(WebDrv.getInstance().getWebDriver());
        ACCESSIBILITY_RESULTS.set(results);

        if (!results.getViolations().isEmpty()) {
            System.out.println("Accessibility Violations:");
            results.getViolations().forEach(violation -> {
                System.out.println("Violation: " + violation.getId());
                System.out.println("Description: " + violation.getDescription());
                System.out.println("Impact: " + violation.getImpact());
                System.out.println("Help: " + violation.getHelp());
                System.out.println("Help URL: " + violation.getHelpUrl());
                System.out.println("Nodes: " + violation.getNodes());
            });
        } else {
            System.out.println("No accessibility violations found.");
        }
    }
}