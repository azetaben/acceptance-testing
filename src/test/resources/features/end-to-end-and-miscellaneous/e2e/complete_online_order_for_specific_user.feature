@e2e @Regression @ParallelRun @all
Feature: Complete Online Order

  Background: navigate to product page
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com"
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

  @TC-e2e_001
  Scenario: End to end online order for standard user
    When As an accepted user I login with valid credentials
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I check for broken buttons, input fields and links on the webpage
    And I should see a list of products
    And I can see products displayed in the page is greater than "0"
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
    And I check for broken buttons, input fields and links on the webpage
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And I should be taken to the "Your Cart" page
    And the page heading should be "Your Cart"
    And I tap on "CONTINUE SHOPPING" button
    And I add following products to cart:
      | Test.allTheThings() T-Shirt (Red) |
    And I tap on "Cart" link
    And I check for broken buttons, input fields and links on the webpage
    And I have the following items in the cart:
      | DESCRIPTION                       | QTY |
      | Sauce Labs Bike Light             | 1   |
      | Sauce Labs Backpack               | 1   |
      | Test.allTheThings() T-Shirt (Red) | 1   |
    And I can see "CONTINUE SHOPPING" button
    And click on "CHECKOUT" button
    And I check for broken buttons, input fields and links on the webpage
    And I should be taken to the "Checkout: Your Information" page
    And the page heading should be "Checkout: Your Information"
    And I should see first name field, last name field, zip code field and "CANCEL" button are present, displayed and enabled
    When I enter first name "John", last name "Doe", and zip code "12345"
    And I click on the "CONTINUE" button
    And I check for broken buttons, input fields and links on the webpage
    And the page heading should be "Checkout: Overview"
    And I should be taken to the "Checkout: Overview" page
    Then I verify the item names, quantities, and prices
    Then I verify the Payment Information as "SauceCard #"
    Then I verify the Shipping Information as "FREE PONY EXPRESS DELIVERY!"
    Then I verify the subtotal, tax, and total
    And I can see the "CANCEL" button
    When I tap on the "FINISH" button
    And I check for broken buttons, input fields and links on the webpage
    And I should be taken to the "Checkout: Complete!" page
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"
    Then I should see the following social media links:
      | social_media |
      | Twitter      |
      | Facebook     |
      | LinkedIn     |
    And I should see the copyright information "© 2020 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy"
    And I tap on the toggle button
    And I check for broken buttons, input fields and links on the webpage
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    And I click on the "Logout" link
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And I check for broken buttons, input fields and links on the webpage

  @TC-e2e_001_1 @ErrorValidation
  Scenario: End to end online order for problem user
    When As an accepted user I login with valid credentials
      | username     | password     |
      | problem_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I should be taken to the "Products" page
    And I check for broken buttons, input fields and links on the webpage
    And I should see a list of products
    And I can see products displayed in the page is greater than "0"
    Then I should see 6 products displayed
    And I can see "Name (A to Z)" is selected by default
    Then I should see sorted result for "Name (A to Z)"
    When I select "Name (Z to A)" from the sort drop down list
    Then I should see sorted result for "Name (Z to A)" not matching option
    When I select price option "Price (high to low)" from the sort drop down list
    Then I should see sorted result for "Price (high to low)" not matching option
    When I select price option "Price (low to high)" from the sort drop down list
    Then I should see sorted result for "Price (low to high)" not matching option
    And I add a product "Sauce Labs Backpack" to the cart
    And I add following products to cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Bike Light    |
    And I remove following products from the cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
 #   And I can not see "2" items in the cart
    And I check for broken buttons, input fields and links on the webpage
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And I should be taken to the "Your Cart" page
    And the page heading should be "Your Cart"
    And I tap on "CONTINUE SHOPPING" button
    And I add following products to cart:
      | Test.allTheThings() T-Shirt (Red) |
    And I tap on "Cart" link
    And I check for broken buttons, input fields and links on the webpage
    And I have the following items in the cart:
      | DESCRIPTION           | QTY |
      | Sauce Labs Bike Light | 1   |
      | Sauce Labs Backpack   | 1   |
      #| Test.allTheThings() T-Shirt (Red) | 1   |
    And I can see "CONTINUE SHOPPING" button
    And click on "CHECKOUT" button
    And I check for broken buttons, input fields and links on the webpage
    And I should be taken to the "Checkout: Your Information" page
    And the page heading should be "Checkout: Your Information"
    And I should see first name field, last name field, zip code field and "CANCEL" button are present, displayed and enabled
    When I enter first name "John", last name "Doe", and zip code "12345"
    And I click on the "CONTINUE" button
    Then I should see an "Error: Last Name is required" displayed
    And the page heading should be "Checkout: Your Information"

  @TC-e2e_001_2
  Scenario: End to end online order for performance glitch user
    When As an accepted user I login with valid credentials
      | username                | password     |
      | performance_glitch_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I check for broken buttons, input fields and links on the webpage
    And I should see a list of products
    And I can see products displayed in the page is greater than "0"
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
    And I check for broken buttons, input fields and links on the webpage
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And I should be taken to the "Your Cart" page
    And the page heading should be "Your Cart"
    And I tap on "CONTINUE SHOPPING" button
    And I add following products to cart:
      | Test.allTheThings() T-Shirt (Red) |
    And I tap on "Cart" link
    And I check for broken buttons, input fields and links on the webpage
    And I have the following items in the cart:
      | DESCRIPTION                       | QTY |
      | Sauce Labs Bike Light             | 1   |
      | Sauce Labs Backpack               | 1   |
      | Test.allTheThings() T-Shirt (Red) | 1   |
    And I can see "CONTINUE SHOPPING" button
    And click on "CHECKOUT" button
    And I check for broken buttons, input fields and links on the webpage
    And I should be taken to the "Checkout: Your Information" page
    And the page heading should be "Checkout: Your Information"
    And I should see first name field, last name field, zip code field and "CANCEL" button are present, displayed and enabled
    When I enter first name "John", last name "Doe", and zip code "12345"
    And I click on the "CONTINUE" button
    And I check for broken buttons, input fields and links on the webpage
    And the page heading should be "Checkout: Overview"
    And I should be taken to the "Checkout: Overview" page
    Then I verify the item names, quantities, and prices
    Then I verify the Payment Information as "SauceCard #"
    Then I verify the Shipping Information as "FREE PONY EXPRESS DELIVERY!"
    Then I verify the subtotal, tax, and total
    And I can see the "CANCEL" button
    When I tap on the "FINISH" button
    And I check for broken buttons, input fields and links on the webpage
    And I should be taken to the "Checkout: Complete!" page
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"
    Then I should see the following social media links:
      | social_media |
      | Twitter      |
      | Facebook     |
      | LinkedIn     |
    And I should see the copyright information "© 2020 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy"
    And I tap on the toggle button
    And I check for broken buttons, input fields and links on the webpage
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    And I click on the "Logout" link
    And page url should be "saucedemo.com"
    And page title should be "Swag Labs"
    And I check for broken buttons, input fields and links on the webpage

  @TC-e2e_002
  Scenario Outline: End to end online order for acceptable users except problem user and locked_out_user
    When I enter username and password with "<username>" and "<password>"
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
    And I add a product "Sauce Labs Backpack" to the cart
    And I add following products to cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Bike Light    |
    And I remove following products from the cart:
      | Sauce Labs Onesie        |
      | Sauce Labs Fleece Jacket |
    And I can see "2" items in the cart
    And I check for broken buttons, input fields and links on the webpage
    And I can see products displayed in the page is greater than "0"
    And I tap on "Cart" link
    And page url should be "https://www.saucedemo.com/cart.html"
    And page title should be "Swag Labs"
    And the page heading should be "Your Cart"
    And I tap on "CONTINUE SHOPPING" button
    And I add following products to cart:
      | Test.allTheThings() T-Shirt (Red) |
    And I tap on "Cart" link
    And I check for broken buttons, input fields and links on the webpage
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
    And I check for broken buttons, input fields and links on the webpage
    And the page heading should be "Checkout: Overview"
    Then I verify the item names, quantities, and prices
    Then I verify the Payment Information as "SauceCard #"
    Then I verify the Shipping Information as "FREE PONY EXPRESS DELIVERY!"
    Then I verify the subtotal, tax, and total
    And I can see the "CANCEL" button
    When I tap on the "FINISH" button
    And I check for broken buttons, input fields and links on the webpage
    And the page heading should be "Checkout: Complete!"
    And I verify the heading text is "Thank you for your order!"
    And I verify "Your order has been dispatched"
    And I verify that the good green image is displayed
    Then I should see the following social media links:
      | social_media |
      | Twitter      |
      | Facebook     |
      | LinkedIn     |
    When I can on the "Back Home" button
    And I should see the copyright information "© 2026 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy"
    And I tap on the toggle button
    And I check for broken buttons, input fields and links on the webpage
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    And I click on the "Logout" link
    And page url should be "saucedemo.com"
    And page title should be "Swag Labs"
    And I check for broken buttons, input fields and links on the webpage
    Examples:
      | username      | password     |
      | standard_user | secret_sauce |
