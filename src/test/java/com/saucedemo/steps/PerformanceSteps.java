package com.saucedemo.steps;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class PerformanceSteps {
    private static final Logger log = LogManager.getLogger(PerformanceSteps.class);
    private static final ThreadLocal<Long> START_TIME = ThreadLocal.withInitial(() -> 0L);
    private static final ThreadLocal<Long> END_TIME = ThreadLocal.withInitial(() -> 0L);
    private final PageManager pm;

    public PerformanceSteps() {
        this.pm = PageManager.getInstance();
        ExplicitWaitFactory.setDriver(WebDrv.getInstance().getWebDriver());
    }

    static long getDuration() {
        return END_TIME.get() - START_TIME.get();
    }

    private LoginPage loginPage() {
        return this.pm.getPage(LoginPage.class);
    }

    @Given("I collect performance metrics for {string}")
    public void i_collect_performance_metrics_for(String url) {
        // Navigate to the specified URL
        WebDrv.getInstance().getWebDriver().get(url);

        // Clear any previous performance entries
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript("performance.clearResourceTimings();");
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript("performance.clearMarks();");
        ((JavascriptExecutor) WebDrv.getInstance().getWebDriver()).executeScript("performance.clearMeasures();");

        // Optionally, wait for a specific element to ensure the page is loaded
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, By.className("login_logo")); // Adjust the locator as needed
    }

    @Given("I log in with user {string} and password {string}")
    public void i_log_in_with_user_and_password(String username, String password) {
        loginPage().enterUsername(username);
        loginPage().enterPassword(password);
        loginPage().clickLoginButton();
    }

    @When("I start performance recording")
    public void i_start_performance_recording() {
        START_TIME.set(System.currentTimeMillis());
        log.info("Performance measurement started at " + START_TIME.get());
        log.info("Start Time: " + START_TIME.get());
    }

    @When("I wait for the inventory page to be displayed")
    public void i_wait_for_the_inventory_page_to_be_displayed() {
        ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, By.className("inventory_list"));
    }

    @When("I stop performance recording")
    public void i_stop_performance_recording() {
        END_TIME.set(System.currentTimeMillis());
        log.info("Performance measurement ended at " + END_TIME.get());
        log.info("Duration of login process: " + getDuration());
    }

    @When("I tap on the login and measure performance to the products page")
    public void i_tap_button_and_measure_performance() {
        START_TIME.set(System.currentTimeMillis());
        log.info("Performance measurement started at " + START_TIME.get());
        loginPage().clickLoginButton();
        END_TIME.set(System.currentTimeMillis());
        log.info("Performance measurement ended at " + END_TIME.get());
        log.info("Duration of login process: " + getDuration());
    }
}
