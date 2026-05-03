@login_general_examples
Feature: Login examples using general page contracts and helper utilities

  Background:
    Given I navigate to "https://www.saucedemo.com/"
    And the login form is displayed

  @login_general_form_validation
  Scenario: Validate the login page using general element, blank-field, and button contracts
    Then the general login element "login form" should be visible
    And the general login element "username" should be visible
    And the general login element "password" should be visible
    And all login fields should be blank on load
    And the login button should read "Login" using button contract
    And I should verify the login page URL and title match expected values

  @login_general_typed_input
  Scenario: Fill the login form using the typed field input contract
    When I fill login form using general page contract with:
      | username | standard_user |
      | password | secret_sauce  |
    Then the following textual values should match on login fields
      | username | standard_user |
      | password | secret_sauce  |

  @login_general_field_data
  Scenario: Fill the username field using the field data contract
    When I type "standard_user" in "username" using field data contract
    Then username field should contain "standard_user" in general login page
    And the following textual values should match on login fields
      | username | standard_user |



