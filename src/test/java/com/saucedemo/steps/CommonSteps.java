package com.saucedemo.steps;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.constants.EndPoint;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.pages.*;
import com.saucedemo.utils.PageElementChecker;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.net.URI;
import java.time.Duration;

public class CommonSteps {
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private final PageManager pm = PageManager.getInstance();
    private final WebDrv webDrv = WebDrv.getInstance();

    private void navigateWithExistingOrNewSession(String url) {
        if (WebDrv.getInstance().getWebDriver() == null) {
            webDrv.openBrowser(url);
            return;
        }

        WebDriver driver = WebDrv.getInstance().getWebDriver();
        driver.manage().deleteAllCookies();
        driver.navigate().to(url);
        driver.manage().window().maximize();


        try {
            Object readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState");
            log.info("Navigation readyState: " + readyState + " | currentUrl: " + driver.getCurrentUrl());
        } catch (Exception e) {
            log.warn("Unable to read document readyState after navigation", e);
        }

        log.info("INFO: Navigating to URL: " + url);
    }

    private String resolveNavigationUrl(String candidateUrl) {
        if (candidateUrl == null || candidateUrl.isBlank()) {
            return getUiTestingBaseUrl();
        }

        String trimmedUrl = candidateUrl.trim();
        if (trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://")) {
            return trimmedUrl;
        }

        String baseUrl = getUiTestingBaseUrl();
        URI baseUri = URI.create(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/");
        String relativePath = trimmedUrl.startsWith("/") ? trimmedUrl.substring(1) : trimmedUrl;
        return baseUri.resolve(relativePath).toString();
    }

    private String getUiTestingBaseUrl() {
        return FrameworkConfig.getInstance().getString(
                "useruitesting.url",
                FrameworkConfig.getInstance().getString(
                        "uitesting.url",
                        FrameworkConfig.getInstance().getString("url", GlobalVarsHelper.getInstance().getURL())
                )
        );
    }

    @Given("I navigate to {string}")
    @Given("I navigate to the url {string}")
    public void i_navigate_to(String url) {
        navigateWithExistingOrNewSession(resolveNavigationUrl(url));
    }

    @Given("I'm on the login Page")
    public void i_am_on_the_login_page() {
        this.pm.getPage(LoginPage.class).load(EndPoint.LOGIN.url);
    }

    @And("I navigate to next page")
    @Then("I tap on the Continue button")
    public void clickOnContinueButton() {
        pm.getPage(Page.class).clickOnContinueButton();
    }

    @And("I click on the Cancel button")
    public void i_click_on_the_continue_button() {
        pm.getPage(Page.class).clickCancelButton();
    }

    @And("^I Sign out from my account$")
    @Then("^I tapped on the Sign out link$")
    public void clickOnLogoutLink() {
        TopNavigationLinksPage topNaviLinksPage = pm.getPage(TopNavigationLinksPage.class);
        TogglePage togglePage = topNaviLinksPage.clickMenuIcon();
        togglePage.clickOnTopLeftMenuItem(SauceDemoConstants.MENU_ITEM_LOGOUT);
    }

    @Given("I navigate to home page")
    @Given("I navigate to index page")
    @Given("I navigate to login page")
    public void i_navigate_to_login_page() {
        if (WebDrv.getInstance().getWebDriver() == null) {
            navigateWithExistingOrNewSession(GlobalVarsHelper.getInstance().getURL());
        } else {
            navigateWithExistingOrNewSession(FrameworkConfig.getInstance().getString("url", GlobalVarsHelper.getInstance().getURL()));
        }
    }

    @Given("I restore the session to the login page")
    public void iRestoreTheSessionToTheLoginPage() {

        if (WebDrv.getInstance().getWebDriver() == null) {
            i_navigate_to_login_page();
            return;
        }

        WebDriver driver = WebDrv.getInstance().getWebDriver();
        try {
            driver.manage().deleteAllCookies();

            try {
                ((JavascriptExecutor) driver).executeScript("window.localStorage && window.localStorage.clear();");
                ((JavascriptExecutor) driver).executeScript("window.sessionStorage && window.sessionStorage.clear();");
            } catch (Exception ignored) {

            }
        } catch (Exception ignored) {
        }

        i_navigate_to_login_page();
    }

    @And("I restore the session to the inventory page")
    public void iRestoreTheSessionToTheInventoryPage() {


        String baseUrl = FrameworkConfig.getInstance().getString("url", GlobalVarsHelper.getInstance().getURL());
        String inventoryPath = SauceDemoConstants.INVENTORY_PAGE_PATH != null && !SauceDemoConstants.INVENTORY_PAGE_PATH.isBlank()
                ? SauceDemoConstants.INVENTORY_PAGE_PATH
                : "inventory.html";
        String targetUrl = resolveNavigationUrl(inventoryPath.startsWith("/") ? inventoryPath : "/" + inventoryPath);

        if (WebDrv.getInstance().getWebDriver() == null) {
            navigateWithExistingOrNewSession(targetUrl);
            return;
        }

        WebDriver driver = WebDrv.getInstance().getWebDriver();
        driver.navigate().to(targetUrl);
        driver.manage().window().maximize();


        try {
            pm.getPage(Page.class).isPageFullyLoaded();

            driver.findElements(By.className("inventory_list"));
        } catch (Exception ignored) {
        }

        log.info("INFO: Restored session navigation to inventory page: " + targetUrl + " (baseUrl=" + baseUrl + ")");
    }

    @Given("I login and navigate to products page")
    @Given("I navigate to products page")
    public void i_navigate_to_products_page() {
        webDrv.openBrowser(GlobalVarsHelper.getLoginPageUrl());
        WebDrv.getInstance().getWebDriver().get(GlobalVarsHelper.getLoginPageUrl());
        LoginPage loginPage = pm.getPage(LoginPage.class);
        loginPage.enterUsername(GlobalVarsHelper.getStandardUser());
        loginPage.enterPassword(GlobalVarsHelper.getPasswordForAllUsers());
        loginPage.clickLoginButton();
    }

    @Then("I check for broken buttons, input fields and links on the webpage")
    public void i_check_for_broken_buttons_input_fields_and_links_on_the_webpage() {
        PageElementChecker pageElementChecker = pm.getPage(PageElementChecker.class);
        pageElementChecker.checkBrokenLinks();
        pageElementChecker.findButtons();
        pageElementChecker.findInputFields();
        pageElementChecker.imageValidation();
    }

}
