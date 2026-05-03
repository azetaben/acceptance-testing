@login_datatable_collections @regression @all
Feature: Login DataTable collection types
  Demonstrates all five Cucumber DataTable collection forms for login scenarios.

  Background:
    Given I navigate to "/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  # ---------------------------------------------------------------------------
  # 1. List<List<String>> — raw rows, no header row
  #    Row 0 = [username, password]
  # ---------------------------------------------------------------------------
  @TC_DT_001 @listOfLists
  Scenario: Login using List<List<String>> raw rows
    When I login with raw credential rows:
      | standard_user | secret_sauce |
    Then I should be taken to the "Products" page

  # ---------------------------------------------------------------------------
  # 2. List<Map<String, String>> — first row is the header, remaining are data
  #    Supports multiple credential pairs in a single step
  # ---------------------------------------------------------------------------
  @TC_DT_002 @listOfMaps
  Scenario: Login using List<Map<String, String>> with named columns
    When I login with the following credentials list:
      | username      | password     | expectedPage |
      | standard_user | secret_sauce | Products     |
    Then I should be taken to the "Products" page

  # ---------------------------------------------------------------------------
  # 3. Map<String, String> — vertical key-value table (single credential pair)
  #    First column = key, second column = value
  # ---------------------------------------------------------------------------
  @TC_DT_003 @mapOfStrings
  Scenario: Login using Map<String, String> key-value pairs
    When I login with the following credentials map:
      | username | standard_user |
      | password | secret_sauce  |
    Then I should be taken to the "Products" page

  # ---------------------------------------------------------------------------
  # 4. Map<String, List<String>> — first column is the key, remaining columns
  #    are a list of values; tests all usernames with a shared password
  # ---------------------------------------------------------------------------
  @TC_DT_004 @mapOfLists
  Scenario: Login using Map<String, List<String>> grouped values
    When I login with grouped credential values:
      | username | standard_user | problem_user | performance_glitch_user |
      | password | secret_sauce  | secret_sauce | secret_sauce            |
    Then I should be taken to the "Products" page

  # ---------------------------------------------------------------------------
  # 5. Map<String, Map<String, String>> — first column is the test-case key,
  #    remaining columns are field-name/value pairs per test case
  # ---------------------------------------------------------------------------
  @TC_DT_005 @mapOfMaps
  Scenario: Login using Map<String, Map<String, String>> named test cases
    When I login with named test case credentials:
      | testCase | username                | password     |
      | TC_001   | standard_user           | secret_sauce |
      | TC_002   | performance_glitch_user | secret_sauce |
    Then I should be taken to the "Products" page

  # ===========================================================================
  # NEGATIVE SCENARIOS
  # ===========================================================================

  # ---------------------------------------------------------------------------
  # N1. List<List<String>> — raw rows: [username, password, expectedError]
  #     Each row is one failed attempt; error is asserted per row in the step.
  # ---------------------------------------------------------------------------
  @TC_DT_N001 @listOfLists @negative
  Scenario: Negative login using List<List<String>> — invalid credentials per row
    Then I attempt login with invalid raw credential rows and verify errors:
      | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      | standard_user   |                | Epic sadface: Password is required                                        |

  # ---------------------------------------------------------------------------
  # N2. List<Map<String, String>> — header row + data rows with expectedError
  #     column; step asserts each row's error after submission.
  # ---------------------------------------------------------------------------
  @TC_DT_N002 @listOfMaps @negative
  Scenario: Negative login using List<Map<String, String>> — multiple invalid users
    Then I attempt login with invalid credentials list and verify errors:
      | username        | password       | expectedError                                                             |
      | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      |                 | secret_sauce   | Epic sadface: Username is required                                        |
      | standard_user   |                | Epic sadface: Password is required                                        |

  # ---------------------------------------------------------------------------
  # N3. Map<String, String> — vertical key-value; locked-out user blocked entry.
  #     Error asserted by a separate Then step.
  # ---------------------------------------------------------------------------
  @TC_DT_N003 @mapOfStrings @negative
  Scenario: Negative login using Map<String, String> — locked-out user
    When I attempt login with invalid credentials map:
      | username | locked_out_user |
      | password | secret_sauce    |
    Then I should see an error message "Epic sadface: Sorry, this user has been locked out."

  # ---------------------------------------------------------------------------
  # N4. Map<String, List<String>> — first column = key, remaining = value list.
  #     All usernames share the same wrong password; each attempt is asserted.
  # ---------------------------------------------------------------------------
  @TC_DT_N004 @mapOfLists @negative
  Scenario: Negative login using Map<String, List<String>> — wrong password for multiple users
    Then I attempt login with invalid grouped credentials and verify errors:
      | username      | invalid_user_1                                                            | invalid_user_2                                                            | Standard_User                                                             |
      | password      | wrong_pass                                                                | wrong_pass                                                                | wrong_pass                                                                |
      | expectedError | Epic sadface: Username and password do not match any user in this service | Epic sadface: Username and password do not match any user in this service | Epic sadface: Username and password do not match any user in this service |

  # ---------------------------------------------------------------------------
  # N5. Map<String, Map<String, String>> — named test cases each with their own
  #     expectedError field; step iterates every case and asserts inline.
  # ---------------------------------------------------------------------------
  @TC_DT_N005 @mapOfMaps @negative
  Scenario: Negative login using Map<String, Map<String, String>> — named invalid cases
    Then I attempt login with invalid named test cases and verify errors:
      | testCase | username        | password       | expectedError                                                             |
      | TC_N_001 | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      | TC_N_002 | invalid_user    | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | TC_N_003 |                 |                | Epic sadface: Username is required                                        |
      | TC_N_004 | standard_user   |                | Epic sadface: Password is required                                        |