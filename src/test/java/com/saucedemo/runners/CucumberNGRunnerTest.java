package com.saucedemo.runners;

import com.saucedemo.constants.RunnerConstants;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@CucumberOptions(
        monochrome = true,
        features = {RunnerConstants.FEATURE_ROOT},
        glue = {RunnerConstants.GLUE_STEPS},
        plugin = {RunnerConstants.REPORT_PRETTY,
                RunnerConstants.REPORT_HTML,
                RunnerConstants.REPORT_JSON,
                RunnerConstants.REPORT_CUCUMBER_HTML,
                RunnerConstants.REPORT_EXTENT},
        tags = "@Add2Cart")
public class CucumberNGRunnerTest extends AbstractTestNGCucumberTests {

    private static final Logger log = LogManager.getLogger(CucumberNGRunnerTest.class);
    private static final ThreadLocal<String> BROWSER = new ThreadLocal<>();

    public static String getBrowser() {
        return BROWSER.get();
    }

    @BeforeTest
    @Parameters({"Browser"})
    public void defineBrowser(@Optional(RunnerConstants.DEFAULT_BROWSER) String browser) {
        BROWSER.set(browser);
        log.info("Browser configured for test suite: " + browser);
    }
}
