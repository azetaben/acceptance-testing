@regression @performance
Feature: Performance checks
  As an application user
  I want the main pages to meet practical speed expectations
  So that performance regressions are detected before release

  Background:
  Common starting point for measuring performance characteristics from the application entry page.
    Given I navigate to "/"

  @regression @performance @login
  Scenario: Login page performance checks
    Then the page load time should be less than 5000 milliseconds

  @regression @performance @books-catalog
  Scenario: Product page performance checks
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    Then the page load time should be less than 5000 milliseconds



