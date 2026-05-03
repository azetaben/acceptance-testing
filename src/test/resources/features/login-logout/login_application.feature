@login&Logout @all
Feature: Login Functionality

  Background: navigate from home page to sign up or login page
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed
    And login usernames and password are displayed
    And I can see Accepted usernames are:
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |

    And I can see Password for all users:
      | secret_sauce |

  @TC_LF_018
  Scenario: Valid Login @TC_L&LF_018
    When I enter valid credentials with username "standard_user" and password "secret_sauce"
    And I click the "Login" button
    And I should be taken to the "Products" page
    Then I should be redirected to products page as "inventory.html"
    And Product count is greater than 0
    And I should see a list of products
    Then I should see 6 products displayed

  @TC_L&LF_019
  Scenario: Invalid Login
    When I enter invalid credentials with username "invalid_user" and password "wrong_password"
    And I click the "Login" button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"

  @TC_L&LF_020
  Scenario: Empty Fields Login @TC_LF_018
    When I leave the username and password fields empty
    And I click the "Login" button
    Then I should see an error message "Epic sadface: Username is required"

  @TC_L&LF_021
  Scenario: Login Page UI Elements
    Then I should see the username input field
    And I should see the password input field
    And I should see the "Login" button