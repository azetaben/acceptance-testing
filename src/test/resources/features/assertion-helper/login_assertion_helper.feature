@login_assertion_helper @login_logout @regression @all
Feature: Login functionality verified with AssertionHelper

  Background: Navigate to the SauceDemo login page
    Given I navigate to "/"
    #Then the login page elements are visible

  @TC_LAH_001
  Scenario: Valid login navigates to the Products page
    When I log in with username "standard_user" and password "secret_sauce"
    Then Page heading should be "Products"
    And the current page url should contain "inventory.html"

  @TC_LAH_002
  Scenario: Invalid credentials show the correct error message
    When I log in with username "invalid_user" and password "wrong_password"
    Then Login error message should be "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @TC_LAH_003
  Scenario: Empty username shows the username-required error
    When I log in with username "" and password "secret_sauce"
    Then Login error message should be "Epic sadface: Username is required"

  @TC_LAH_004
  Scenario: Empty password shows the password-required error
    When I log in with username "standard_user" and password ""
    Then Login error message should be "Epic sadface: Password is required"

  @TC_LAH_005
  Scenario: Locked-out user sees the account-locked error
    When I log in with username "locked_out_user" and password "secret_sauce"
    Then Login error message should be "Epic sadface: Sorry, this user has been locked out."

  @TC_LAH_006
  Scenario: Login button label is correct
    Then Login button text should be "Login"

  @TC_LAH_007
  Scenario: Password field is hidden by default
    When I enter the password "secret_sauce"
    Then Password field type should be "password"

  @TC_LAH_008 @smoke
  Scenario Outline: Multiple accepted users can log in successfully
    When I log in with username "<username>" and password "secret_sauce"
    Then Page heading should be "Products"
    And the current page url should contain "inventory.html"

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |

  @TC_LAH_009
  Scenario Outline: Invalid credential combinations always show the error banner
    When I log in with username "<username>" and password "<password>"
    Then Login error message should be "<expected_error>"

    Examples:
      | username      | password      | expected_error                                                            |
      | invalid_user  | wrong_pass    | Epic sadface: Username and password do not match any user in this service |
      | standard_user | wrong_pass    | Epic sadface: Username and password do not match any user in this service |
      |               | secret_sauce  | Epic sadface: Username is required                                        |
      | standard_user |               | Epic sadface: Password is required                                        |

  @TC_LAH_010
  Scenario: Login page UI elements are present — verifyElementPresent
    Then the login button element should be present
    And the username field element should be present
    And the password field element should be present

  @TC_LAH_011
  Scenario: After successful login the login form is no longer in the DOM — verifyElementNotPresent
    When I log in with username "standard_user" and password "secret_sauce"
    Then the login form element should not be present

  @TC_LAH_012
  Scenario: Login button label text matches — verifyTextEquals
    Then the login button element text should equal "Login"

  @TC_LAH_013
  Scenario: Error message text matches after a failed login — verifyTextEquals
    When I log in with username "invalid_user" and password "wrong_password"
    Then the error message element text should equal "Epic sadface: Username and password do not match any user in this service"
