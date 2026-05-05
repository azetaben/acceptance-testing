@miscellaneous
@Accessibility
@login_logout
@login_logout
@all
Feature: Web Page Accessibility

  Background: navigates to login page
    Given I navigate to "https://saucedemo.com/"

  @TC-MC_008
  Scenario: Check accessibility of the Sauce Demo login page
    When I check the login page for accessibility violations
    Then there should be no accessibility violations reported

  @TC-MC_009
  Scenario: Check accessibility of the Sauce Demo inventory page after login
    When I check the inventory page for accessibility violations
    Then there should be no accessibility violations reported