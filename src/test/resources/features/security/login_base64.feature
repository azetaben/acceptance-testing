@regression @auth @login @encoding
Feature: Login with Base64 encoded credentials
  As an application user
  I want encoded credential handling to be validated
  So that login behavior remains predictable when encoded inputs are used

  Background:
    # Common authenticated login preparation for Base64 encoding scenarios.
    Given I navigate to "/login"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed
    And I can see login form input field attribute value as:
      | Username |
      | Password |
    And I can see "Login" button displayed

  @regression @login @encoding
  Scenario: Login with valid Base64 encoded credentials
    When I login with Base64 encoded credentials
      | username | YWRtaW4= |
      | password | YWRtaW4= |
    Then I should be redirected to the products page
    And I should be taken to the "Products" page

  @regression @login @encoding @negative
  Scenario: Login with invalid Base64 encoded credentials
    When I login with Base64 encoded credentials
      | username | d3JvbmdfdXNlcg== |
      | password | d3JvbmdfcGFzcw== |
    Then an error message "Epic sadface: Username and password do not match any user in this service" should be displayed
