@login_logout @dataDriven @all
Feature: Login Data-Driven Tests
  As a user
  I want to verify login behaviour across all user types and credential combinations
  In order to ensure the authentication system works correctly

  Background: Navigate to the SauceDemo login page
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  # ─────────────────────────────────────────────────────────────────────────────
  # TC_LDD_001 – TC_LDD_006 : Valid credentials → successful login
  # ─────────────────────────────────────────────────────────────────────────────
  @TC_LDD_001 @TC_LDD_003 @TC_LDD_004 @TC_LDD_005 @TC_LDD_006
  @validLogin @regression
  Scenario Outline: <testCaseId> - Successful login with valid credentials for <username>
    When I login with username "<username>" and "<password>"
    Then I should see "<expectedHeader>" is displayed
    And I should be taken to the "Products" page
    And Product count is greater than 0
    And I should see a list of products
    And I should see 6 products displayed

    Examples:
      | testCaseId | username                | password     | expectedHeader |
      | TC_LDD_001 | standard_user           | secret_sauce | Products       |
      | TC_LDD_003 | problem_user            | secret_sauce | Products       |
      | TC_LDD_004 | performance_glitch_user | secret_sauce | Products       |
      | TC_LDD_005 | error_user              | secret_sauce | Products       |
      | TC_LDD_006 | visual_user             | secret_sauce | Products       |

  # ─────────────────────────────────────────────────────────────────────────────
  # TC_LDD_002 : locked_out_user with valid password → account locked error
  # ─────────────────────────────────────────────────────────────────────────────
  @TC_LDD_002 @invalidLogin @regression @ErrorValidation
  Scenario: TC_LDD_002 - Failed login for locked_out_user with valid password
    When I login with username "locked_out_user" and "secret_sauce"
    Then an error message "Epic sadface: Sorry, this user has been locked out." should be displayed
    And page url should be "saucedemo.com"
    And page title should be "Swag Labs"

  # ─────────────────────────────────────────────────────────────────────────────
  # TC_LDD_007 – TC_LDD_012 : Invalid credentials → error messages
  # ─────────────────────────────────────────────────────────────────────────────
  @TC_LDD_007 @TC_LDD_008 @TC_LDD_009 @TC_LDD_010 @TC_LDD_011 @TC_LDD_012
  @invalidLogin @regression @ErrorValidation
  Scenario Outline: <testCaseId> - Failed login with invalid credentials
    When I login with username "<username>" and "<password>"
    Then I should see an error message "<expectedMessage>" displayed

    Examples:
      | testCaseId | username        | password       | expectedMessage                                                           |
      | TC_LDD_007 | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | TC_LDD_008 | standard_user   | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | TC_LDD_009 |                 | secret_sauce   | Epic sadface: Username is required                                        |
      | TC_LDD_010 | standard_user   |                | Epic sadface: Password is required                                        |
      | TC_LDD_011 |                 |                | Epic sadface: Username is required                                        |
      | TC_LDD_012 | locked_out_user | wrong_password | Epic sadface: Username and password do not match any user in this service |


