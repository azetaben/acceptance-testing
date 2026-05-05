@Add2Cart @regression @all @ShippingCart
Feature: Verify Add-To-Cart Functionality

  Background:
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
    And I should be taken to the "Products" page
    And page url should be "inventory.html"
    And I am on "inventory" page
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I can see products displayed in the page is greater than "0"
    And cart should be empty "0"

  @TC_ATC_002 @Add2Cart
  Scenario: should be able to navigate to products to cart page
    And I tap on "Cart" link
    And I am on "cart" page
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And I can see inventory page management related controls:
      | control       |
      | Name (A to Z) |
      | Add to cart   |
      | $             |

  @SelectionState @Smoke @regression @inventory
  Scenario: Verify add to cart button becomes remove after selection
    And I am on "inventory" page
    Then I can see product page header "Products" displayed
    And I check if "add to cart buttons" is "enabled"
    And I click "add to cart" button for "first" product
    Then the "button text" should change to "Remove"

  @TC_ATC_003 @Add2Cart @regression @inventory @cart
  Scenario: Should be able to add items to cart
    And I add a product "Sauce Labs Backpack" to the cart
    And I can see "Remove" button for product "Sauce Labs Backpack"
    And I add following products to cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Bike Light    |
    Then add to cart button text should change to "Remove"
    And I remove following products from the cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
    And I can see "2" items in the cart
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And I am on "cart" page
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And I can see cart page management related controls:
      | Checkout          |
      | Remove            |
      | Continue Shopping |
    And I have the following items in the cart:
      | DESCRIPTION           | QTY |
      | Sauce Labs Bike Light | 1   |
      | Sauce Labs Backpack   | 1   |
    And I click the first product item in the cart
    And I am on "inventory-item" page
    And page title should be "Swag Labs"
    And I can see inventory items page management related controls:
      | Remove            |
      | Continue Shopping |
    And I can see the product details:
      | Name        | Sauce Labs Backpack                                                                                                                    |
      | Description | carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection. |
      | Price ($)   | 29.99                                                                                                                                  |
    When I tap on the "Back to products" link
    And I am on "inventory" page
    And page url should be "https://www.saucedemo.com/inventory.html"
    And page title should be "Swag Labs"

  @TC_ATC_004 @Add2Cart
  Scenario Outline: Should be able to add to cart above products and verify in the cart
    And I add a product "<product_name>" to the cart
    And I can see "1" items in the cart
    And I can see 1 items in the cart
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    Then I should see 1 "<product_name>" in the cart

    Examples:
      | product_name          |
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |

  @TC_ATC_005 @Add2Cart
  Scenario Outline: Should be able to add to cart by cookies and verify in the cart
    And there is already "<product_name>" in the cart
    Then I should see 1 "<product_name>" in the cart

    Examples:
      | product_name             |
      | Sauce Labs Fleece Jacket |

  @DisplayedAndEnabled @regression @checkout
  Scenario: Verify checkout form elements are displayed and enabled
    And I am on "inventory" page
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I add 2 products to cart
    And I proceed to checkout
    When I check if "checkout form" is "displayed and enabled"
    Then the result should be true





