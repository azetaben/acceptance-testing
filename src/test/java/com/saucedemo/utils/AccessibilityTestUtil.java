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

        axeBuilder.withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa"));
        log.info("Running accessibility test on " + contextDescription);

        axeBuilder.disableRules(List.of("color-contrast"));

        Results results;
        if (contextElement != null) {
            results = axeBuilder.analyze(driver, contextElement);
            log.info("Analyzing accessibility for element: " + contextDescription);
        } else {
            results = axeBuilder.analyze(driver);
            log.info("Analyzing accessibility for entire page");

            log.error(String.valueOf("Page URL: " + driver.getCurrentUrl()));

        }

        List<Rule> violations = results.getViolations();

        if (!violations.isEmpty()) {
            log.error(String.valueOf("ACCESSIBILITY VIOLATIONS FOUND for: " + contextDescription));
            log.error("Accessibility violations found for: " + contextDescription);

            log.error(String.valueOf("=================================================="));
            log.error(String.valueOf("Page URL: " + driver.getCurrentUrl()));
            log.error("Page URL: " + driver.getCurrentUrl());
            log.error(String.valueOf("Timestamp: " + results.getTimestamp()));
            log.error("Timestamp: " + results.getTimestamp());
            log.error(String.valueOf("User Agent: " + results.getTestEnvironment().getUserAgent()));
            log.error("User Agent: " + results.getTestEnvironment().getUserAgent());
            log.error(String.valueOf("Axe Core Version: " + results.getTestEngine().getVersion()));
            log.error("Axe Core Version: " + results.getTestEngine().getVersion());
            log.error(String.valueOf("Total Violations: " + violations.size()));
            log.error("Total Violations: " + violations.size());
            log.error(String.valueOf("--------------------------------------------------"));

            for (int i = 0; i < violations.size(); i++) {
                Rule violation = violations.get(i);
                log.error(String.valueOf("Violation " + (i + 1) + ": " + violation.getId() + " (Impact: " + violation.getImpact() + ")"));
                log.error(String.valueOf("  Description: " + violation.getDescription()));
                log.error(String.valueOf("  Help: " + violation.getHelp()));
                log.error(String.valueOf("  Help URL: " + violation.getHelpUrl()));
                log.error(String.valueOf("  Tags: " + String.join(", ", violation.getTags())));

                List<CheckedNode> nodes = violation.getNodes();
                if (!nodes.isEmpty()) {
                    log.error(String.valueOf("  Affected Nodes (" + nodes.size() + "):"));
                    for (int j = 0; j < nodes.size(); j++) {
                        CheckedNode node = nodes.get(j);
                        log.error(String.valueOf("    Node " + (j + 1) + ":"));
                        log.error(String.valueOf("      HTML: " + node.getHtml()));
                        log.error(String.valueOf("      Impact: " + node.getImpact()));


                        if (node.getFailureSummary() != null && !node.getFailureSummary().isEmpty()) {
                            log.error(String.valueOf("      Failure Summary: " + node.getFailureSummary()));
                        } else {

                            final boolean[] detailsPrinted = {false};

                            if (node.getAny() != null && !node.getAny().isEmpty()) {
                                node.getAny().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        log.error(String.valueOf("      Failure Detail (from 'any' check): " + check.getMessage()));
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }
                            if (node.getAll() != null && !node.getAll().isEmpty()) {
                                node.getAll().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        log.error(String.valueOf("      Failure Detail (from 'all' check): " + check.getMessage()));
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }
                            if (node.getNone() != null && !node.getNone().isEmpty()) {
                                node.getNone().forEach(check -> {
                                    if (check.getMessage() != null && !check.getMessage().isEmpty()) {
                                        log.error(String.valueOf("      Failure Detail (from 'none' check): " + check.getMessage()));
                                        detailsPrinted[0] = true;
                                    }
                                });
                            }

                            if (!detailsPrinted[0]) {
                                log.error(String.valueOf("      Failure Summary: (No specific details found in checks, and main summary was null/empty)"));
                            }
                        }


                    }
                }
                log.error(String.valueOf("--------------------------------------------------"));
            }


            throw new AssertionError("Accessibility violations found: " + violations.size() +
                    " on " + contextDescription + " for page " + driver.getCurrentUrl() +
                    ". Check console error output (stderr) for details.");
        } else {
            log.info(String.valueOf("No accessibility violations found for " + contextDescription + " on page: " + driver.getCurrentUrl()));
            log.info(String.valueOf("Axe Core Version: " + results.getTestEngine().getVersion()));
        }
    }
}
