@assertion_helper_tab @regression @all
Feature: AssertionHelper new-tab and list utility methods — login example

  Background: Log in to reach the inventory page
    Given I navigate to "/"
    When I log in with username "standard_user" and password "secret_sauce"
    Then Page heading should be "Products"

  @TC_AH_TAB_001
  Scenario: assertNewTabOpenedWithExpectedTitle — SauceDemo opened in a new tab has title "Swag Labs"
    When I open "https://www.saucedemo.com/" in a new browser tab
    Then the new tab should have title "Swag Labs"

  @TC_AH_TAB_002
  Scenario: assertNewTabIsOpenedWithExpectedPage — heading element on new tab matches expected text
    When I open a new browser tab with a heading element containing "Sauce Labs Products"
    Then the new tab page heading should be "Sauce Labs Products"

  @TC_AH_TAB_003
  Scenario: getModifiableIdListOfExpectedChildElements — copy is independent from the original list
    Then a modifiable copy of "item_4,item_0,item_1" should not affect the original
