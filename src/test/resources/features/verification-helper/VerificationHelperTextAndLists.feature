@verification_helper @text_and_lists @all
Feature: VerificationHelper Text and List Verification - Framework Integration

  Background:
    """
    Test Suite for text content and list verification on framework pages
    Tests text extraction, list operations, and element attributes
    Uses real pages: LoginPage, InventoryPage, CartPage, CheckoutPages
    """

  @TextVerification @Smoke @regression @login
  Scenario: Verify login page header text
    Given I navigate to "/"
    When I get the "login page header" text
    Then the "login page header" should contain "Swag Labs"

  @TextVerification @Smoke @regression @login
  Scenario: Verify error message text on failed login
    When I enter username "locked_out_user" and password "secret_sauce"
    And I tap "Login" button
    When I get the "error message" text
    Then the "error message" should contain "locked out"

  @TextVerification @regression @inventory
  Scenario: Verify product names can be extracted
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I get the "first product name" text
    Then the "product name" should not be empty

  @TextVerification @regression @inventory
  Scenario: Verify product prices are displayed as text
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I get the "all product prices" text
    Then all "prices" should contain "dollar sign"


  @Attributes @regression @login
  Scenario: Verify login input field placeholder attributes
    Given I navigate to "/"
    When I get the "username input placeholder" attribute
    Then the "placeholder attribute" should be "Username"

  @Attributes @regression @inventory
  Scenario: Verify product item data attributes
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I get the "product item data" attribute
    Then the "product data attribute" should contain "product identifier"

  @ListOperations @Smoke @regression @inventory
  Scenario: Check if product exists in product list
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I check if product "Sauce Labs Backpack" exists in list
    Then the "product in list" should be "found"

  @ListOperations @regression @inventory
  Scenario: Verify product prices are in correct order
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I sort products by "price" "ascending"
    Then the "product prices" should be "in ascending order"

  @ListOperations @regression @cart
  Scenario: Verify cart items match selected products
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I add 2 products to cart
    When I navigate to "cart page"
    Then all "cart items" should "be displayed"

  @ElementPresence @Smoke @regression @checkout
  Scenario: Verify all checkout fields are present
    And I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I proceed to checkout
    When I check if "checkout form fields" is "present"
    Then all "required fields" should "be present"

  @Clickability @Smoke @regression @inventory
  Scenario: Verify sort dropdown is clickable
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I check if "sort dropdown" is "clickable"
    Then the "sort dropdown" should "be clickable"

  @PageNavigation @regression @inventory
  Scenario: Get page title from inventory page
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I am on "inventory" page
    Then the "page title" should be "Swag Labs"

  @PageNavigation @regression @cart
  Scenario: Verify cart page URL
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I navigate to "cart page"
    Then the "page URL" should contain "cart.html"

