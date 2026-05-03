package com.saucedemo.steps;

import com.saucedemo.webdriverutilities.WebDrv;
import io.cucumber.java.en.Then;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

public class PerformanceAssertionSteps {

    private long executeMetricScript(JavascriptExecutor js, String script) {
        Object result = js.executeScript(script);
        return result instanceof Number ? ((Number) result).longValue() : 0L;
    }

    @Then("the page load time should be less than {long} milliseconds")
    public void the_page_load_time_should_be_less_than_milliseconds(long thresholdMs) {
        // Reuse existing metric plumbing; "Load" maps to Navigation Timing loadEventEnd - navigationStart.
        the_metric_should_be_less_than("Load", thresholdMs);
    }

    @Then("the First Contentful Paint should be less than {long} milliseconds")
    public void the_first_contentful_paint_should_be_less_than_milliseconds(long thresholdMs) {
        // Reuse existing metric plumbing; "FirstContentfulPaint" maps to first-contentful-paint startTime.
        the_metric_should_be_less_than("FirstContentfulPaint", thresholdMs);
    }

    @Then("the {string} should be less than {long} milliseconds")
    public void the_metric_should_be_less_than(String metric, long threshold) {
        JavascriptExecutor js = (JavascriptExecutor) WebDrv.getInstance().getWebDriver();

        long metricValue = switch (metric) {
            case "FirstContentfulPaint" -> executeMetricScript(js,
                    "var entry = performance.getEntriesByName('first-contentful-paint')[0]; return entry ? entry.startTime : 0;");
            case "LargestContentfulPaint" -> executeMetricScript(js,
                    "var entry = performance.getEntriesByName('largest-contentful-paint')[0]; return entry ? entry.startTime : 0;");
            case "Load" -> executeMetricScript(js,
                    "return performance.timing.loadEventEnd - performance.timing.navigationStart;");
            case "ScriptDuration" -> executeMetricScript(js,
                    "return performance.getEntriesByType('script').reduce((total, script) => total + script.duration, 0);");
            case "Resources" -> executeMetricScript(js,
                    "return performance.getEntriesByType('resource').length;");
            case "JSHeapUsedSize" -> executeMetricScript(js,
                    "return performance.memory.usedJSHeapSize;");
            default -> throw new IllegalArgumentException("Unknown metric: " + metric);
        };

        Assert.assertTrue(metricValue < threshold, metric + " exceeded threshold: " + metricValue + " ms");
    }

    @Then("the {string} during the interaction should be less than {long} milliseconds")
    public void the_interaction_metric_should_be_less_than(String metric, long threshold) {
        long duration = PerformanceSteps.getDuration();
        Assert.assertTrue(duration < threshold, metric + " exceeded threshold");
    }

    @Then("I observe page load performance issue as duration time is greater than {string} ms")
    public void i_observe_page_load_performance_issue_for_this_user(String threshold) {
        long duration = PerformanceSteps.getDuration();
        long thresholdValue = Long.parseLong(threshold);
        Assert.assertTrue(duration > thresholdValue, "Performance issue not observed");
    }
}

