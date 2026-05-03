@performance @all
Feature: Performance Testing for SauceDemo

  @TC-MC_013 @PageLoad @Thresholds
  Scenario: Homepage load performance should meet defined criteria
    Given I navigate to "https://saucedemo.com/"
    Given I collect performance metrics for "https://saucedemo.com/"
    Then the "FirstContentfulPaint" should be less than 4000 milliseconds
    And the "LargestContentfulPaint" should be less than 4500 milliseconds

  @TC-MC_014 @PageLoad @Interaction @Thresholds
  Scenario: Login process performance should meet defined criteria
    Given I navigate to "https://saucedemo.com/"
    And I log in with user "performance_glitch_user" and password "secret_sauce"
    When I start performance recording
    And I wait for the inventory page to be displayed
    And I stop performance recording
    Then the "TaskDuration" during the interaction should be less than 1000 milliseconds
    And the "ScriptDuration" during the interaction should be less than 400 milliseconds

  @TC-MC_015 @PageLoad @Thresholds
  Scenario: Performance Test for Glitch User
    Given I navigate to "https://saucedemo.com/"
    And I enter username and password with "performance_glitch_user" and "secret_sauce" respectively
    And I tap on the login and measure performance to the products page
    Then I observe page load performance issue as duration time is greater than "4000" ms
