package com.saucedemo.runners;

import com.saucedemo.constants.RunnerConstants;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        monochrome = true,
        features = {RunnerConstants.FEATURE_ROOT + "login-logout/"},
        glue = {RunnerConstants.GLUE_STEPS},
        plugin = {RunnerConstants.REPORT_PRETTY,
                RunnerConstants.REPORT_HTML,
                RunnerConstants.REPORT_JSON,
                RunnerConstants.REPORT_CUCUMBER_HTML,
                RunnerConstants.REPORT_EXTENT,
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},

        tags = "@login_datatable_collections")
public class LoginRunnerTest extends AbstractTestNGCucumberTests {

}
