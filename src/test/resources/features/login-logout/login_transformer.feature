@login_transformer @regression @all
Feature: Login using LoginDataTransformer
  Verifies that LoginDataTransformer correctly maps DataTable rows into
  LoginData records and that those records drive login behaviour end-to-end.

  Background:
    Given I navigate to "/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  # ---------------------------------------------------------------------------
  # TC_LT_001  Positive — transformer maps valid credentials; login succeeds
  # ---------------------------------------------------------------------------
  @TC_LT_001 @positive
  Scenario: Successful login mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username      | password     | expectedError |
      | standard_user | secret_sauce |               |
    Then I verify the transformer login outcome

  # ---------------------------------------------------------------------------
  # TC_LT_002  Negative — transformer maps invalid credentials; error shown
  # ---------------------------------------------------------------------------
  @TC_LT_002 @negative
  Scenario: Failed login with wrong credentials mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username     | password       | expectedError                                                             |
      | invalid_user | wrong_password | Epic sadface: Username and password do not match any user in this service |
    Then I verify the transformer login outcome

  # ---------------------------------------------------------------------------
  # TC_LT_003  Negative — transformer maps locked-out user; error shown
  # ---------------------------------------------------------------------------
  @TC_LT_003 @negative
  Scenario: Failed login for locked-out user mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username        | password     | expectedError                                       |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out. |
    Then I verify the transformer login outcome

  # ---------------------------------------------------------------------------
  # TC_LT_004  Negative — transformer maps empty credentials; errors shown
  # ---------------------------------------------------------------------------
  @TC_LT_004 @negative
  Scenario: Failed login with empty username mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username | password     | expectedError                      |
      |          | secret_sauce | Epic sadface: Username is required |
    Then I verify the transformer login outcome

  @TC_LT_004b @negative
  Scenario: Failed login with empty password mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username      | password | expectedError                      |
      | standard_user |          | Epic sadface: Password is required |
    Then I verify the transformer login outcome

  # ---------------------------------------------------------------------------
  # TC_LT_005  Scenario Outline — transformer is exercised for multiple users;
  #            each example row is converted independently via the transformer
  # ---------------------------------------------------------------------------
  @TC_LT_005
  Scenario Outline: Login outcome for <username> mapped by LoginDataTransformer
    When I login with transformer mapped credentials:
      | username   | password   | expectedError   |
      | <username> | <password> | <expectedError> |
    Then I verify the transformer login outcome

    Examples: Valid users
      | username                | password     | expectedError |
      | standard_user           | secret_sauce |               |
      | problem_user            | secret_sauce |               |
      | performance_glitch_user | secret_sauce |               |

    Examples: Invalid credentials
      | username        | password       | expectedError                                                             |
      | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | Standard_User   | secret_sauce   | Epic sadface: Username and password do not match any user in this service |
      | standard_user   | Secret_Sauce   | Epic sadface: Username and password do not match any user in this service |
      |                 |                | Epic sadface: Username is required                                        |
