package com.saucedemo.utils;

import com.deque.html.axecore.results.CheckedNode;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class AccessibilityTestUtil {
    private static final Logger log = LogManager.getLogger(AccessibilityTestUtil.class);

    public static void runAccessibilityTest(WebDriver driver) {
        runAccessibilityTest(driver, null, "entire page");
    }

    public static void runAccessibilityTest(WebDriver driver, WebElement contextElement) {
        String contextDescription = (contextElement != null) ? "element: " + contextElement.getTagName() + (contextElement.getAttribute("id") != null ? "#" + contextElement.getAttribute("id") : "") : "entire page";
        runAccessibilityTest(driver, contextElement, contextDescription);
    }

    private static void runAccessibilityTest(WebDriver driver, WebElement contextElement, String contextDescription) {
        AxeBuilder axeBuilder = new AxeBuilder();
        // Example: focus on WCAG 2 A and AA. Requires import java.util.Arrays;
        axeBuilder.withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa"));
        log.info("Running accessibility test on " + contextDescription);
        // Example: disable a specific rule. Requires import java.util.Arrays;
        axeBuilder.disableRules(List.of("color-contrast"));

        Results results;
        if (contextElement != null) {
            results = axeBuilder.analyze(driver, contextElement);
            log.info("Analyzing accessibility for element: " + contextDescription);
        } else {
            results = axeBuilder.analyze(driver);
            log.info("Analyzing accessibility for entire page");
            // Log statement here
            System.err.println("Page URL: " + driver.getCurrentUrl());

        }

        List<Rule> violations = results.getViolations();

        if (!violations.isEmpty()) {
            System.err.println("ACCESSIBILITY VIOLATIONS FOUND for: " + contextDescription);
            log.error("Accessibility violations found for: " + contextDescription);

            System.err.println("==================================================");
            System.err.println("Page URL: " + driver.getCurrentUrl());
            log.error("Page URL: " + driver.getCurrentUrl());
            System.err.println("Timestamp: " + results.getTimestamp());
            log.error("Timestamp: " + results.getTimestamp());
            System.err.println("User Agent: " + results.getTestEnvironment().getUserAgent());
            log.error("User Agent: " + results.getTestEnvironment().getUserAgent());
            System.err.println("Axe Core Version: " + results.getTestEngine().getVersion());
            log.error("Axe Core Version: " + results.getTestEngine().getVersion());
            System.err.println("Total Violations: " + violations.size());
            log.error("Total Violations: " + violations.size());
            System.err.println("--------------------------------------------------");

            for (int i = 0; i < violations.size(); i++) {
                Rule violation = violations.get(i);
                System.err.println("Violation " + (i + 1) + ": " + violation.getId() + " (Impact: " + violation.getImpact() + ")");
                System.err.println("  Description: " + violation.getDescription());
                System.err.println("  Help: " + violation.getHelp());
                System.err.println("  Help URL: " + violation.getHelpUrl());
                System.err.println("  Tags: " + String.join(", ", violation.getTags()));

                List<CheckedNode> nodes = violation.getNodes(); // This correctly returns List<CheckedNode>
                if (!nodes.isEmpty()) {
                    System.err.println("  Affected Nodes (" + nodes.size() + "):");
                    for (int j = 0; j < nodes.size(); j++) {
                        CheckedNode node = nodes.get(j); // Changed from Node to CheckedNode for type consistency
                        System.err.println("    Node " + (j + 1) + ":");
                        System.err.println("      HTML: " + node.getHtml());
                        System.err.println("      Impact: " + node.getImpact());

                        // Print Failure Summary - Refined Logic
                        if (node.getFailureSummary() != null && !node.getFailureSummary().isEmpty()) {
                            System.err.println("      Failure Summary: " + node.getFailureSummary());
                        } else {
                            // If the main summary is not available, try to get details from individual checks
                            final boolean[] detailsPrinted = {false}; // Use an array to modify in lambda

                            if (node.getAny() != null && !node.getAny().isEmpty()) {
                                node.getAny().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        System.err.println("      Failure Detail (from 'any' check): " + check.getMessage());
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }
                            if (node.getAll() != null && !node.getAll().isEmpty()) {
                                node.getAll().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        System.err.println("      Failure Detail (from 'all' check): " + check.getMessage());
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }
                            if (node.getNone() != null && !node.getNone().isEmpty()) {
                                node.getNone().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        System.err.println("      Failure Detail (from 'none' check): " + check.getMessage());
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }

                            if (!detailsPrinted[0]) {
                                System.err.println("      Failure Summary: (No specific details found in checks, and main summary was null/empty)");
                            }
                        }
                        // End of Refined Failure Summary Logic

                        // If you need more granular details from CheckedNode, you can access:
                        // node.getAny() -> List of checks, any of which must pass
                        // node.getAll() -> List of checks, all of which must pass
                        // node.getNone() -> List of checks, none of which must pass
                    }
                }
                System.err.println("--------------------------------------------------");
            }

            // For detailed debugging, you can print the full JSON
            // System.err.println("Full violations JSON:\n" + results.getViolationsJson());

            throw new AssertionError("Accessibility violations found: " + violations.size() +
                    " on " + contextDescription + " for page " + driver.getCurrentUrl() +
                    ". Check console error output (stderr) for details.");
        } else {
            System.out.println("No accessibility violations found for " + contextDescription + " on page: " + driver.getCurrentUrl());
            System.out.println("Axe Core Version: " + results.getTestEngine().getVersion());
        }
    }
}