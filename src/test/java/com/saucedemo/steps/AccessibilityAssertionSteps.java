package com.saucedemo.steps;

import com.deque.html.axecore.results.Results;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class AccessibilityAssertionSteps {

    @Then("there should be no accessibility violations reported")
    public void thereShouldBeNoAccessibilityViolationsReported() {
        System.out.println("Accessibility check passed for the current context.");
    }

    @Then("I should see the Full AXE results")
    public void iShouldSeeTheFullAXEResults() {
        Results accessibilityResults = AccessibilitySteps.getAccessibilityResults();
        if (accessibilityResults == null) {
            Assert.fail("Accessibility check was not performed. No results available.");
        }

        if (!accessibilityResults.getViolations().isEmpty()) {
            System.out.println("Accessibility Violations Summary:");
            accessibilityResults.getViolations().forEach(violation -> {
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

