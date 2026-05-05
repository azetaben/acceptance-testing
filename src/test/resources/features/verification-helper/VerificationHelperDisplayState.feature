@verification_helper @display_state @all
Feature: VerificationHelper Element Display State Verification - Framework Integration

  Background:
    Given I navigate to "/"
    And I am on "login" page

  @DisplayState @Smoke @regression @login
  Scenario: Verify login form elements are displayed
    When I check if "login form" is "displayed"
    And I check if "username input field" is "displayed"
    And I check if "password input field" is "displayed"
    Then the result should be true

  @DisplayState @Smoke @regression @login
  Scenario: Verify login button is displayed and enabled
    When I check if "login button" is "displayed"
    And I check if "login button" is "enabled"
    Then the result should be true

  @DisplayState @regression @inventory
  Scenario: Verify product list is displayed on inventory page
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I check if "product list" is "displayed"
    Then the result should be true

  @DisplayState @regression @inventory
  Scenario: Verify product prices are displayed correctly
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I check if "all product prices" is "displayed"
    Then the result should be true

  @EnabledState @regression @inventory
  Scenario: Verify add to cart buttons are enabled
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I check if "add to cart buttons" is "enabled"
    Then the result should be true

  @DisplayedAndEnabled @Smoke @regression @login
  Scenario: Verify all login form fields are displayed and enabled
    When I check if "username field" is "displayed and enabled"
    And I check if "password field" is "displayed and enabled"
    Then the result should be true


  @DisplayState @regression @cart
  Scenario: Verify cart items are displayed correctly
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I add 2 products to cart
    When I navigate to "cart page"
    Then the result should be true

  @DisplayState @regression @inventory
  Scenario: Verify footer is displayed on all pages
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I check if "footer" is "displayed"
    And page url should be "inventory.html"
    Then the result should be true

