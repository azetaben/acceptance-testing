@login_logout @all @error_validation_tests
Feature: Login Functionality

  Background: Navigate to the login page
    Given I navigate to "https://www.saucedemo.com"
    And the login form is displayed

  Scenario Outline: Valid login with accepted usernames
    When I enter username "<username>" and password "secret_sauce"
    And I click the "Login" button
    And I should be taken to the "Products" page
    And Product count is greater than 0
    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |

  Scenario: Invalid Login
    When I enter username "invalid_user" and password "wrong_password"
    And I click the "Login" button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"

  Scenario: Empty Fields Login
    When I enter username "" and password ""
    And I click the "Login" button
    Then I should see an error message "Epic sadface: Username is required"


  Scenario Outline: Invalid login attempts
    When I enter username "<username>" and password "<password>"
    And I click the "Login" button
    Then I should see an error message "<error_message>"

    Examples:
      | username                 | password                     | error_message                                                             |
      | locked_out_user          | secret_sauce                 | Epic sadface: Sorry, this user has been locked out.                       |
      | invalid_user             | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | wrong_password               | Epic sadface: Username and password do not match any user in this service |
      |                          | secret_sauce                 | Epic sadface: Username is required                                        |
      | standard_user            |                              | Epic sadface: Password is required                                        |
      | !@#$%^&*                 | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | !@#$%^&*                     | Epic sadface: Username and password do not match any user in this service |
      | user12345678901234567890 | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | password12345678901234567890 | Epic sadface: Username and password do not match any user in this service |
      | ' OR '1'='1              | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | ' OR '1'='1                  | Epic sadface: Username and password do not match any user in this service |
      | alert('test')            | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | alert('test')                | Epic sadface: Username and password do not match any user in this service |
      |                          |                              | Epic sadface: Username is required                                        |
      | standard_user            |                              | Epic sadface: Password is required                                        |
      | Standard_User            | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |
      | standard_user            | Secret_Sauce                 | Epic sadface: Username and password do not match any user in this service |
      | user@example.com         | secret_sauce                 | Epic sadface: Username and password do not match any user in this service |


