package com.saucedemo.steps;

import io.cucumber.java.en.Then;
import org.testng.Assert;

public class LighthousePerformanceAssertionSteps {

    @Then("the performance score should be above {double}")
    public void the_performance_score_should_be_above(double threshold) {
        String lighthouseFailure = LighthousePerformanceSteps.getLighthouseFailure();
        Assert.assertTrue(lighthouseFailure == null || lighthouseFailure.isBlank(),
                "Lighthouse audit failed before a score could be asserted: " + lighthouseFailure);

        Double performanceScore = LighthousePerformanceSteps.getPerformanceScore();
        Assert.assertNotNull(performanceScore, "Lighthouse audit did not produce a performance score.");
        Assert.assertTrue(performanceScore > threshold,
                "Performance score is below the threshold: " + performanceScore);
    }
}
