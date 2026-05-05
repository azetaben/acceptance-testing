@login_logout @all
Feature: Login to logout Functionality

  @TC_L_LF_012
  Scenario: navigate from home page to sign up or login page to logout
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed
    And I can see Accepted usernames are:
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
    And I can see Password for all users:
      | secret_sauce |
    And I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I should be redirected to products page as "inventory.html"
    And page url should be "inventory.html"
    And I can see "Products" displayed
    And Product count is greater than 0
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When I click on the "Logout" link
    Then page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
