Feature: Login security checks for Sauce Demo
  As a user,
  I want to ensure that the login functionality is secure against common attack vectors
  So that my account and data are protected.


  Background: navigate from home page to sign up or login page
    Given I navigate to "https://saucedemo.com/"
    And I check for broken buttons, input fields and links on the webpage
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed

  @TC_L_LF_023 @security @login @invalidLogin @regression
  Scenario: 023 - reject SQL injection style payload
    When I login with username "' OR '1'='1" and "' OR '1'='1"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"

  @TC_L_LF_024 @security @login @invalidLogin @regression
  Scenario: 024 - reject XSS style payload in username
    When I login with username "<script>alert('xss')</script>" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"

  @TC_L_LF_025 @security @login @invalidLogin @regression
  Scenario Outline: 025 - repeated invalid password attempts do not grant access
    When I login with username "standard_user" and "<password>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    Examples:
      | password        |
      | wrong_password1 |
      | wrong_password2 |
      | wrong_password3 |
      | wrong_password4 |
      | wrong_password5 |

  @TC_L_LF_026 @security @login @invalidLogin @regression
  Scenario: 026 - consistent error message for invalid user and invalid password
    When I login with username "non_existing_user_123" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    When I login with username "standard_user" and "wrong_password"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"

  @TC_L_LF_027 @security @login @regression
  Scenario: 027 - password is hidden by default and visible
    When I enter username "standard_user" and password "secret_sauce"
    Then the password should be hidden

  @TC_L_LF_028 @security @login @invalidLogin @regression
  Scenario: 028 - block unauthenticated direct navigation to inventory page
    Given I navigate to "https://www.saucedemo.com/inventory.html"
    Then I should be redirected to the login page
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  @TC_L_LF_029 @security @login @invalidLogin @regression
  Scenario: 029 - block unauthenticated direct navigation to inventory with query parameters
    Given I navigate to "https://www.saucedemo.com/inventory.html?source=deep_link"
    Then I should be redirected to the login page
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  @TC_L_LF_030 @security @login @invalidLogin @regression
  Scenario: 030 - block unauthenticated direct navigation to inventory with hash fragment
    Given I navigate to "https://www.saucedemo.com/inventory.html#products"
    Then I should be redirected to the login page
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed


  @TC_L_LF_032 @security @login @invalidLogin @regression
  Scenario: 032 - failed login should not allow direct navigation to inventory page
    When I login with username "standard_user" and "wrong_password"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    Given I navigate to "https://www.saucedemo.com/inventory.html"
    Then I should be redirected to the login page
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed
    Then I should see an error message "Epic sadface: You can only access '/inventory.html' when you are logged in." displayed
