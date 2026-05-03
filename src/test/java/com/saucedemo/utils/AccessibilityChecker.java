package com.saucedemo.utils;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.saucedemo.webdriverutilities.WebDrv;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessibilityChecker {

    private static final Logger log = LoggerFactory.getLogger(AccessibilityChecker.class);

    public void checkAccessibility(String url) {
        AxeBuilder axeBuilder = new AxeBuilder();
        WebDriver webDriver = WebDrv.getInstance().getWebDriver();

        Results axeResults = axeBuilder.analyze(webDriver);
        log.info(axeResults.getTestEngine().getVersion());
        if (!axeResults.violationFree()) {
            log.warn("AXE analysis found accessibility issues!");
        } else {
            log.info("AXE analysis did not find any accessibility issues.");
        }
        if (axeResults.isErrored()) {
            log.error("AXE analysis failed due to errors. See below for details:");
        }
        if (axeResults.isErrored()) {
            log.error(String.valueOf(axeResults.getError()));
        }
        if (!axeResults.isErrored()) {
            log.info("AXE analysis completed successfully.");
        } else {
            log.error(String.valueOf(axeResults.getError()));
        }


        if (log.isDebugEnabled()) {
            log.debug("Full AXE results JSON: " + axeResults.toString());
        } else {
            log.info("AXE analysis summary: Violations=" + axeResults.getViolations().size() +
                    ", Passes=" + axeResults.getPasses().size() +
                    ", Incomplete=" + axeResults.getIncomplete().size() +
                    ", Inapplicable=" + axeResults.getInapplicable().size());
        }


    }
}
