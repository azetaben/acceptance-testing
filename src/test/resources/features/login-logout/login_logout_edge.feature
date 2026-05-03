@login&Logout @edgeUrlOuterHtml @all
Feature: Login and logout edge coverage with URL and outerHtml checks

  Background: Open login page
    Given I navigate to "https://www.saucedemo.com/v1/index.html"
    And the login form is displayed

  @TC_EDGE_URL_HTML_001
  Scenario: Successful login shows inventory URL and inventory markup
    When I enter username "standard_user" and password "secret_sauce"
    And I click on the login button
    Then the current url should contain "inventory.html"

  @TC_EDGE_URL_HTML_002
  Scenario: Invalid login keeps user on login URL and shows error markup
    When I enter invalid credentials with username "standard_user" and password "wrong_password"
    Then the current url should contain "https://www.saucedemo.com"
    And the login error outerHtml should contain "Epic sadface: Username and password do not match any user in this service"

  @TC_EDGE_URL_HTML_003
  Scenario: Empty credentials keeps password DOM value blank
    When I leave the username and password fields empty
    Then the current url should contain "https://www.saucedemo.com"
    And the login error outerHtml should contain "Username is required"
    And the password field DOM value should be blank

  @TC_EDGE_URL_HTML_004
  Scenario: Logout returns user to login URL and login form markup
    When I enter username "problem_user" and password "secret_sauce"
    And I click on the login button
    And I tap on the toggle button
    And I click on the "Logout" link
    Then I should be redirected to the login page
    And the current url should contain "https://www.saucedemo.com"
    And the login form should contain:
      | Login |
    And the current url should not contain "inventory.html"
