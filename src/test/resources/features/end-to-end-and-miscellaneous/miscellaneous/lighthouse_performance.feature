@miscellaneous @regression @performance @all
Feature: Lighthouse Performance Audit

  @TC-MC_007
  Scenario: Run Lighthouse audit for index page
    Given I navigate to "https://saucedemo.com/"
    When I run Lighthouse audit for "https://saucedemo.com/"
    Then the performance score should be above 0.7

  @TC-MC_007_1
  Scenario: Run Lighthouse audit for products page
    Given I navigate to "https://saucedemo.com/"
    And I login as performance glitch user "performance_glitch_user" and "secret_sauce"
    When I run Lighthouse audit for "https://www.saucedemo.com/v1/inventory.html"
    Then the performance score should be above 0.45
