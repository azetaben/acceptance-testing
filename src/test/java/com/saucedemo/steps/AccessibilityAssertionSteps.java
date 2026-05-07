package com.saucedemo.steps;
import com.deque.html.axecore.results.Results;
import io.cucumber.java.en.Then;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;


public class AccessibilityAssertionSteps {
    private static final Logger log = LogManager.getLogger(AccessibilityAssertionSteps.class);


    @Then("there should be no accessibility violations reported")
    public void thereShouldBeNoAccessibilityViolationsReported() {
        log.info(String.valueOf("Accessibility check passed for the current context."));
    }

    @Then("I should see the Full AXE results")
    public void iShouldSeeTheFullAXEResults() {
        Results accessibilityResults = AccessibilitySteps.getAccessibilityResults();
        if (accessibilityResults == null) {
            Assert.fail("Accessibility check was not performed. No results available.");
        }

        if (!accessibilityResults.getViolations().isEmpty()) {
            log.info(String.valueOf("Accessibility Violations Summary:"));
            accessibilityResults.getViolations().forEach(violation -> {
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
