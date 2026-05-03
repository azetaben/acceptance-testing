@CheckoutProcess @regression @all
Feature: Checkout Summary Verification

  Background: Navigate to checkout finish page
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed

    And login usernames and password are displayed
    And I can see Accepted usernames are:
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
    And I can see Password for all users:
      | secret_sauce |
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I add a product "Sauce Labs Backpack" to the cart
    And I add following products to cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Bike Light    |
    And I remove following products from the cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
    And I can see "2" items in the cart
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And the page heading should be "Your Cart"
    And I tap on "CONTINUE SHOPPING" button
    And I add following products to cart:
      | Test.allTheThings() T-Shirt (Red) |
    And I tap on "Cart" link
    And the page heading should be "Your Cart"
    And I have the following items in the cart:
      | DESCRIPTION                       | QTY |
      | Sauce Labs Bike Light             | 1   |
      | Sauce Labs Backpack               | 1   |
      | Test.allTheThings() T-Shirt (Red) | 1   |
    And I can see "CONTINUE SHOPPING" button
    And click on "CHECKOUT" button
    And the page heading should be "Checkout: Your Information"
    And I should see first name field, last name field, zip code field and "CANCEL" button are present, displayed and enabled
    When I enter first name "John", last name "Doe", and zip code "12345"
    And I click on the "CONTINUE" button
    And the page heading should be "Checkout: Overview"
    Then I verify the item names, quantities, and prices
    Then I verify the Payment Information as "SauceCard #"
    Then I verify the Shipping Information as "FREE PONY EXPRESS DELIVERY!"
    And I verify the item names, quantities, and prices
    Then I verify the subtotal, tax, and total
    And I can see the "CANCEL" button

  @TC_CO_0001
  Scenario: Verify checkout finish summary and pony image displayed
    When I tap on the "Finish" button
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"

  @TC_CO_0002
  Scenario: Click finish button and verify checkout complete page
    When I tap on the "Finish" button
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"

  @TC_CO_0003
  Scenario: Click cancel button
    When I click "CANCEL" button
    Then I should be redirected to the products page
    And I should be taken to the "Products" page
