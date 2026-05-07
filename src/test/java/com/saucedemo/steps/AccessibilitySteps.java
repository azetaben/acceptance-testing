package com.saucedemo.steps;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.helperutilities.pageload.CheckPageIsLoaded;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.PageManager;
import com.saucedemo.utils.AccessibilityChecker;
import com.saucedemo.utils.AccessibilityTestUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.openqa.selenium.By;


public class AccessibilitySteps {
    private static final Logger log = LogManager.getLogger(AccessibilitySteps.class);

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
        loginPage().login(GlobalVarsHelper.getStandardUser(), GlobalVarsHelper.getPasswordForAllUsers());
        CheckPageIsLoaded.waitForDocumentReadyState();
    }

    @When("I check the inventory page for accessibility violations")
    public void iCheckTheInventoryPageForAccessibilityViolations() {

        loginPage().login(SauceDemoConstants.USER_STANDARD, SauceDemoConstants.USER_PASSWORD);
        CheckPageIsLoaded.waitForDocumentReadyState();
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
            log.info(String.valueOf("Accessibility Violations:"));
            results.getViolations().forEach(violation -> {
                log.info(String.valueOf("Violation: " + violation.getId()));
                log.info(String.valueOf("Description: " + violation.getDescription()));
                log.info(String.valueOf("Impact: " + violation.getImpact()));
                log.info(String.valueOf("Help: " + violation.getHelp()));
                log.info(String.valueOf("Help URL: " + violation.getHelpUrl()));
                log.info(String.valueOf("Nodes: " + violation.getNodes()));
            });
        } else {
            log.info(String.valueOf("No accessibility violations found."));
        }
    }
}
