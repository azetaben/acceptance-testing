@TC_ATC_004 @inventory-item @Add2Cart @regression @cart @complete_order
Feature: Verify complete order functionality
  As a user,
  I want to be able to add items to the cart,
  view the cart details and complete the order successfully.

  Scenario: Should be able to add items to cart, view the product details in the cart and complete the order
    Given I navigate to "/"
    And I am on "login" page
    And page url should be "saucedemo.com"
    And page title should be "Swag Labs"
    And logo is displayed
    And I can see login form input field attribute value as:
      | Username |
      | Password |
    And I can see "Login" button displayed
    And Should see "Accepted usernames are:" and "Password for all users:" information:
      | Accepted usernames are: |
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |
      | Password for all users: |
      | secret_sauce            |

    And I can see "Password for all users:":
      | secret_sauce |

    When I attempt login with invalid credentials as lockout user
      | username        | password     |
      | locked_out_user | secret_sauce |
    Then an error message "Epic sadface: Sorry, this user has been locked out." should be displayed

    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |

    And I should be taken to the "Products" page
    And the page heading should be "Products"
    And page url should be "inventory.html"
    And I am on "inventory" page
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I can see products displayed in the page is greater than "0"
    And cart should be empty "0"
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
    And page url should be "cart.html"
    And page title should be "Swag Labs"
    And the page heading should be "Your Cart"
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
      | Name        | Sauce Labs Backpack                 |
      | Description | carry.allTheThings() with the sleek |
      | Price ($)   | 29.99                               |
    When I tap on the "Back to products" link
    And I am on "inventory" page
    And page url should be "inventory.html"
    And page title should be "Swag Labs"
    And I verify the inventory details images in the inventory page:
      | productName           | imageSrc                                                                 |
      | Sauce Labs Bike Light | https://www.saucedemo.com/static/media/bike-light-1200x1500.9c9c0933.jpg |
    And I tap on "Cart" link
    And I am on "cart" page
    And click on "Checkout" button
    And I am on "checkout" page
    And the page heading should be "Checkout: Your Information"
    And I can see checkout information form controls:
      | control     |
      | First Name  |
      | Last Name   |
      | Postal Code |
      | Continue    |
      | Cancel      |
    And I fill the checkout information form with:
      | First Name  | John  |
      | Last Name   | Doe   |
      | Postal Code | 12345 |
    And I tap "Continue"
    And I am on "checkout-step-two" page
    And the page heading should be "Checkout: Overview"
    And I can see checkout overview page controls:
      | control |
      | Finish  |
      | Cancel  |
    And I have the following items in the cart:
      | DESCRIPTION           | QTY | Price |
      | Sauce Labs Bike Light | 1   | 9.99  |
      | Sauce Labs Backpack   | 1   | 29.99 |
    And I can see the following product shipment, payment and price details:
      | Payment Information:  | SauceCard #31337            |
      | Shipping Information: | Free Pony Express Delivery! |
      | Item total:           | 39.98                       |
      | Tax:                  | 3.20                        |
      | Total:                | 43.18                       |
    And I can see the product details in the overview page:
      | Name        | Sauce Labs Backpack                 |
      | Description | carry.allTheThings() with the sleek |
      | Price ($)   | 29.99                               |
    When I tap on the "Finish" button
    And I am on "checkout-complete" page
    And the page heading should be "Checkout: Complete!"
    And I can see checkout complete page controls:
      | control   |
      | Back Home |
      | Twitter   |
      | Facebook  |
      | LinkedIn  |
    And I can see Pony Express image with src "https://www.saucedemo.com/static/media/pony-express.46394a5d.png"
    And I can see the success goodbye messages:
      | Thank you for your order!                                                               |
      | Your order has been dispatched, and will arrive just as fast as the pony can get there! |
    And I confirm "Your order has been dispatched" message
    And tap on the "Back Home" button
    And I am on "inventory" page
    And page url should be "inventory.html"
    And I tap on the toggle button
    And I click on the "Logout" link
    Then I should be redirected to the login page
    And I am on "login" page