@miscellaneous @Accessibility @regression @all
Feature: Accessibility Check

  @TC-MC_001
  Scenario: Check accessibility of a webpage
    Given I navigate to "https://saucedemo.com/"
    When I check the accessibility of the page "https://saucedemo.com/"

