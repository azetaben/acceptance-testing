@regression @performance
Feature: Page load performance metrics
  As an application user
  I want key pages to load within acceptable performance thresholds
  So that the application feels responsive during normal use

  Background:
  Common starting point for measuring performance characteristics from the application entry page.
    Given I navigate to "/"

  @regression @performance @login
  Scenario: Verify login page performance
    Then the page load time should be less than 5000 milliseconds
    And the First Contentful Paint should be less than 4000 milliseconds

  @regression @performance @books
  Scenario: Verify books page performance
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    Then the page load time should be less than 5000 milliseconds
    And the First Contentful Paint should be less than 4000 milliseconds

  @regression @performance @add-book
  Scenario: Verify product page performance
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    Then the page load time should be less than 5000 milliseconds
    And the First Contentful Paint should be less than 4000 milliseconds
