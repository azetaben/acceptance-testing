@Add2Cart @ShippingCart @Add2Cart @regression @all
Feature: Shopping Cart Functionality

  @TC_ATC_001
  Scenario: Add and remove items to/ from the cart
    Given I navigate to "https://saucedemo.com/"
    And I check for broken buttons, input fields and links on the webpage
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
    Then Cart should be empty
    And I add a product "Sauce Labs Backpack" to the cart
    And I add following products to cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Bike Light    |
    And I can see "REMOVE" button for product "Sauce Labs Onesie"
    And I can see "REMOVE" button for product "Sauce Labs Bike Light"
    And I remove following products from the cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
    And I can see "ADD TO CART" button for product "Sauce Labs Onesie"
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
    Given I have the following items in the cart:
      | DESCRIPTION                       | QTY |
      | Sauce Labs Bike Light             | 1   |
      | Sauce Labs Backpack               | 1   |
      | Test.allTheThings() T-Shirt (Red) | 1   |


