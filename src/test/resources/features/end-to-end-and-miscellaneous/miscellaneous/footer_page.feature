@miscellaneous @regression @all
Feature: Footer Section Verification

  @TC-MC_006
  Scenario: Verify footer elements
    Given I navigate to "https://saucedemo.com/"
    When I login in login page
      | standard_user | secret_sauce |
    Then I can see product page header "Products" displayed
    Then I should see the following social media links:
      | social_media |
      | Twitter      |
      | Facebook     |
      | LinkedIn     |
    And I should see the copyright information "© 2020 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy"
    And I should see the footer robot image source "img/SwagBot_Footer_graphic.png"
