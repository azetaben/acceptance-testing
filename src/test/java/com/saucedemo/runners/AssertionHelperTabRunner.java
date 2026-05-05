package com.saucedemo.runners;

import com.saucedemo.constants.RunnerConstants;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        monochrome = true,
        features = {RunnerConstants.FEATURE_ROOT + "assertion-helper/assertion_helper_tab.feature"},
        glue = {RunnerConstants.GLUE_STEPS},
        plugin = {RunnerConstants.REPORT_PRETTY,
                RunnerConstants.REPORT_HTML,
                RunnerConstants.REPORT_JSON,
                RunnerConstants.REPORT_CUCUMBER_HTML,
                RunnerConstants.REPORT_EXTENT},
        tags = "@assertion_helper_tab")
public class AssertionHelperTabRunner extends AbstractTestNGCucumberTests {

}
