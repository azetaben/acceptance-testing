@login&Logout @all
Feature: Logout Functionality

  Background: Navigate to the login page and log in
    Given I navigate to "https://www.saucedemo.com"
    And the login form is displayed

  @TC_L&LF_022
  Scenario Outline: Valid logout
    When I enter username "<username>" and password "secret_sauce"
    And I click the "Login" button
    And I should be taken to the "Products" page
    And I tap on the toggle button
    When I click on the "Logout" link
    Then I should be redirected to the login page
    And the login form is displayed
    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |

