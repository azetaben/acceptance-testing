@login_logout @all
Feature: Login to top menu toggle links Functionalities


  Background: navigate from home page to sign up or login page
    Given I navigate to "https://saucedemo.com/"

  @TC_L_LF_013
  Scenario: verify online order flow to cart page and reset app state from the top menu toggle links
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
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When I click on the "Reset App State" link
    Then Cart should be empty
    And cart should be empty "0"

  @TC_L_LF_013A
  Scenario: verify online order flow to cart page and logout from the top menu toggle links
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I can see products displayed in the page is greater than "0"
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When I click on the "Logout" link
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed


  @TC_L_LF_013B
  Scenario: verify online order flow to cart page and click about from the top menu toggle links
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I can see products displayed in the page is greater than "0"
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When I click on the "About" link
    And page url should be "https://saucelabs.com/"
    And page title should be "Sauce Labs: Cross Browser Testing, Selenium Testing & Mobile Testing"


  @TC_L_LF_013B
  Scenario: verify online order flow to cart page and click all items from the top menu toggle links
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I can see products displayed in the page is greater than "0"
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When I click on the "All Items" link
    And page url should be "https://www.saucedemo.com/inventory.html"
    And page title should be "Swag Labs"
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |



