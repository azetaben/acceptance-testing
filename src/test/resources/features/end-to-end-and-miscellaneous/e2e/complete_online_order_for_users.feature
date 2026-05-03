@e2e @regression @all
Feature: Complete Online Order

  @TC-e2e_003
  Scenario Outline: End to end online order
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And I check for broken buttons, input fields and links on the webpage
    And the login form is displayed
    And login usernames and password are displayed
    And I can see Accepted usernames are:
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
    And I can see Password for all users:
      | secret_sauce |
    When I enter username and password with "<username>" and "<password>"
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I can see products displayed in the page is greater than "0"
    And I should see a list of products
    Then I should see 6 products displayed
    And I can see "Name (A to Z)" is selected by default
    Then I should see sorted result for "Name (A to Z)"
    When I select "Name (Z to A)" from the sort drop down list
    Then I should see sorted result for "Name (Z to A)"
    When I select price option "Price (high to low)" from the sort drop down list
    Then I should see sorted result for "Price (high to low)"
    When I select price option "Price (low to high)" from the sort drop down list
    Then I should see sorted result for "Price (low to high)"
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
    And I have the following items in the cart:
      | DESCRIPTION                       | QTY |
      | Sauce Labs Bike Light             | 1   |
      | Sauce Labs Backpack               | 1   |
      | Test.allTheThings() T-Shirt (Red) | 1   |
    And I can see "Continue Shopping" button
    And click on "Checkout" button
    And the page heading should be "Checkout: Your Information"
    And I should see first name field, last name field, zip code field and "CANCEL" button are present, displayed and enabled
    When I enter first name "John", last name "Doe", and zip code "12345"
    And I click on the "CONTINUE" button
    And the page heading should be "Checkout: Overview"
    Then I verify the item names, quantities, and prices
    Then I verify the Payment Information as "SauceCard #"
    Then I verify the Shipping Information as "FREE PONY EXPRESS DELIVERY!"
    Then I verify the subtotal, tax, and total
    And I can see the "CANCEL" button
    When I tap on the "Finish" button
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"
    Then I should see the following social media links:
      | social_media |
      | Twitter      |
      | Facebook     |
      | LinkedIn     |
    And I should see the copyright information "© 2026 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy"
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    And I click on the "Logout" link
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |
