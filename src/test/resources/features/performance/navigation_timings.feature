@performance
Feature: Navigation timing metrics

  Scenario: Collect navigation timing on each page transition
    Given I navigate to login page
    When I enter valid credentials with username "standard_user" and password "secret_sauce"
    And I click on the login button
    Then I wait for the inventory page to be displayed
    And I Sign out from my account

