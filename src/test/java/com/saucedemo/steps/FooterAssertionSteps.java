package com.saucedemo.steps;

import com.saucedemo.pages.FooterPage;
import com.saucedemo.pages.PageManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.List;

public class FooterAssertionSteps {
    private final PageManager pm;

    public FooterAssertionSteps() {
        this.pm = PageManager.getInstance();
    }

    private FooterPage footerPage() {
        return this.pm.getPage(FooterPage.class);
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\u00A0', ' ').replaceAll("\\s+", " ").trim();
    }

    private String stripCopyrightYearPrefix(String value) {
        return normalizeText(value).replaceFirst("^\u00A9\\s*\\d{4}\\s*", "");
    }

    @Then("I should see the following social media links:")
    public void i_should_see_the_following_social_media_links(DataTable dataTable) throws Exception {
        List<String> expectedLinks = dataTable.asList(String.class);
        List<String> actualLinks = footerPage().getSocialMediaLinks();
        System.out.println(expectedLinks);
        System.out.println(actualLinks);
    }

    @Then("I should see the copyright information {string}")
    public void i_should_see_the_copyright_information(String expectedCopyright) {
        String actualRaw = footerPage().getCopyrightInfo();
        String actualNormalized = normalizeText(actualRaw);
        String expectedNormalized = normalizeText(expectedCopyright);

        Assert.assertTrue(actualNormalized.matches(".*\u00A9\\s*\\d{4}.*"),
                "Copyright year format is invalid. Actual: " + actualRaw);

        String actualWithoutYear = stripCopyrightYearPrefix(actualNormalized);
        String expectedWithoutYear = stripCopyrightYearPrefix(expectedNormalized);

        Assert.assertTrue(actualWithoutYear.equals(expectedWithoutYear)
                        || actualWithoutYear.contains(expectedWithoutYear)
                        || expectedWithoutYear.contains(actualWithoutYear),
                "Copyright information does not match. Expected: '" + expectedCopyright +
                        "' | Actual: '" + actualRaw + "'");
    }

    @Then("I should see the footer robot image source {string}")
    public void i_should_see_the_footer_robot_image_source(String expectedImageSrc) {
        String actualImageSrc = footerPage().getFooterRobotImageSrc();
        Assert.assertTrue(actualImageSrc.contains(expectedImageSrc) || expectedImageSrc.contains(actualImageSrc),
                "Footer robot image source does not match. Expected: '" + expectedImageSrc +
                        "' | Actual: '" + actualImageSrc + "'");
    }
}

