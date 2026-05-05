@login_logout @edge @all
Feature: Login and logout edge cases

  Background: Open login page with expected baseline state
    Given I navigate to "/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed

  @TC_EDGE_001
  Scenario: Empty username and empty password
    When I login with username "" and ""
    Then I should see an error message "Epic sadface: Username is required" displayed

  @TC_EDGE_002
  Scenario: Empty username with valid password
    When I login with username "" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username is required" displayed

  @TC_EDGE_003
  Scenario: Valid username with empty password
    When I login with username "standard_user" and ""
    Then I should see an error message "Epic sadface: Password is required" displayed

  @TC_EDGE_003_1
  Scenario Outline: Login with missing credentials
    When I login with username "<username>" and "<password>"
    Then I should see an error message "<error>" displayed

    Examples:
      | username  | password  | error                              |
      | validUser |           | Epic sadface: Password is required |
      |           | validPass | Epic sadface: Username is required |

  @TC_EDGE_003_2
  Scenario Outline: Login with invalid passwords
    When I login with username "validUser" and "<password>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

    Examples:
      | password     |
      | wrongPass123 |
      | SECRET_SAUCE |
      | validPass    |

  @TC_EDGE_004
  Scenario: Locked user with valid password
    When I login with username "locked_out_user" and "secret_sauce"
    Then I should see an error message "Epic sadface: Sorry, this user has been locked out." displayed

  @TC_EDGE_005
  Scenario: Unknown username with valid password
    When I login with username "valid_user" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_006
  Scenario: Valid username with wrong password
    When I login with username "standard_user" and "wrong_password"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_007
  Scenario: Username with SQL-like payload
    When I login with username "' OR '1'='1" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_008
  Scenario: Password with SQL-like payload
    When I login with username "standard_user" and "' OR '1'='1"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_009
  Scenario: Username with XSS-like payload
    When I login with username "<script>alert('x')</script>" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_010
  Scenario: Password with XSS-like payload
    When I login with username "standard_user" and "<script>alert('x')</script>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_011
  Scenario: Username with unicode characters
    When I login with username "test_user_测试" and "secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_012
  Scenario: Password with unicode characters
    When I login with username "standard_user" and "P@ss_测试"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_015
  Scenario: Password with leading spaces
    When I login with username "standard_user" and "  secret_sauce"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_016
  Scenario: Password with trailing spaces
    When I login with username "standard_user" and "secret_sauce  "
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

  @TC_EDGE_017
  Scenario Outline: Case sensitivity validation
    When I login with username "<username>" and "<password>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    Examples:
      | username      | password     |
      | STANDARD_USER | secret_sauce |
      | standard_user | SECRET_SAUCE |

  @TC_EDGE_018
  Scenario Outline: Long input boundary-like values
    When I login with username "<username>" and "<password>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed
    Examples:
      | username                                   | password                                   |
      | 012345678901234567890123456789009800000000 | secret_sauce                               |
      | standard_user                              | 012345678901234567890123456789009800000000 |

  @TC_EDGE_019
  Scenario: DataTableType login with one typed credential row
    When I login in login page
      | standard_user | secret_sauce |
    Then I should see "Products"

  @TC_EDGE_020
  Scenario: Logout invalidates product URL and exposes login form markup
    Given I navigate login page and login
    And I tap on the toggle button
    And I click on the "Logout" link
    Then I should be redirected to the login page
    And the current url should not contain "inventory.html"
    And the login form should contain:
      | Login    |
      | Username |
      | Password |

  @TC_EDGE_021
  Scenario Outline: Consecutive invalid attempts remain rejected
    When I login with username "<username>" and "<password>"
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service" displayed

    Examples:
      | username      | password   |
      | standard_user | wrongPass1 |
      | standard_user | wrongPass2 |
      | standard_user | wrongPass3 |

  @TC_EDGE_022
  Scenario: Re-login after explicit logout with same user
    Given I navigate login page and login
    And I tap on the toggle button
    And I click on the "Logout" link
    When I login with username "standard_user" and "secret_sauce"
    Then I should see "Products"

  @TC_EDGE_023
  Scenario: New session login with a different accepted user after logout
    Given I navigate login page and login
    And I tap on the toggle button
    And I click on the "Logout" link
    When I login with username "problem_user" and "secret_sauce"
    Then I should see "Products"

  @TC_EDGE_024
  Scenario: Session invalidation check by direct protected route after logout
    Given I navigate login page and login
    And I tap on the toggle button
    And I click on the "Logout" link
    When I navigate to "https://www.saucedemo.com/inventory.html"
    Then I should be redirected to the login page
    And I should see an error message "Epic sadface: You can only access '/inventory.html' when you are logged in." displayed

  @TC_EDGE_025
  Scenario: Post-logout direct access to cart is blocked
    Given I navigate login page and login
    And I tap on the toggle button
    And I click on the "Logout" link
    When I navigate to "https://www.saucedemo.com/cart.html"
    Then I should be redirected to the login page
    And I should see an error message "Epic sadface: You can only access '/cart.html' when you are logged in." displayed

  @TC_EDGE_026
  Scenario Outline: Login validation matrix
    When I login with username "<username>" and "<password>"
    Then I should see an error message "<expectedError>" displayed

    Examples:
      | username      | password      | expectedError                                                             |
      | standard_user | wrongPass     | Epic sadface: Username and password do not match any user in this service |
      |               | standard_user | Epic sadface: Username is required                                        |
      | standard_user |               | Epic sadface: Password is required                                        |
