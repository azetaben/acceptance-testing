package com.saucedemo.constants;

public final class RunnerConstants {
    public static final String FEATURE_ROOT = "src/test/resources/features/";
    public static final String GLUE_STEPS = "com.saucedemo";
    public static final String REPORT_PRETTY = "pretty";
    public static final String REPORT_HTML = "html:target/cucumber.html";
    public static final String REPORT_JSON = "json:target/cucumber.json";
    public static final String REPORT_CUCUMBER_HTML = "html:cucumber_report/cucumber.html";
    public static final String REPORT_EXTENT = "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:";
    public static final String DEFAULT_BROWSER = "chrome";

    private RunnerConstants() {
    }
}
