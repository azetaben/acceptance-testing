@login&Logout @filter @regression @all
Feature: Login to top menu toggle links Functionalities

  Background: navigate from home page to sign up or login page to logout
    Given I navigate to "https://saucedemo.com"
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And page url should be "inventory.html"
    And I should be taken to the "Products" page
    And I tap on the toggle button
    And I tap on the cross X button
    And I tap on the toggle button
    And I should see the following links:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |

  @TC_L&LF_014 @filter @regression
  Scenario: verify All Items link is clickable
    When I click on the "All Items" link
    And page url should be "https://www.saucedemo.com/inventory.html"

  @TC_L&LF_015 @filter @regression
  Scenario: verify About link is clickable
    When I click on the "About" link
    And page url should be "https://saucelabs.com/"

  @TC_L&LF_016 @filter @regression
  Scenario: verify Logout link is clickable
    When I click on the "Logout" link
    And page url should be "https://www.saucedemo.com/"

  @TC_L&LF_017 @filter @regression
  Scenario: verify Reset App State link is clickable
    When I click on the "Reset App State" link





