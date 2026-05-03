package com.saucedemo.runners;

import com.saucedemo.constants.RunnerConstants;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        monochrome = true, features = {RunnerConstants.FEATURE_ROOT},
        glue = {RunnerConstants.GLUE_STEPS},
        plugin = {RunnerConstants.REPORT_PRETTY,
                RunnerConstants.REPORT_HTML,
                RunnerConstants.REPORT_JSON,
                RunnerConstants.REPORT_CUCUMBER_HTML,
                RunnerConstants.REPORT_EXTENT
        },
        // tags = "@smoke or @regression",
        // tags = "@smoke and @regression",
        // tags = "@smoke not @regression",
        // tags = "not @regression",
        tags = "@all or @regression")
public class MainRunner extends AbstractTestNGCucumberTests {

}
