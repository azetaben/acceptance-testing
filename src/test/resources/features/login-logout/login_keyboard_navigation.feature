@regression @auth @login @keyboard @accessibility
Feature: Login keyboard navigation
  As a keyboard user
  I want to navigate the login form with the Tab key
  So that the login controls are reachable without using a mouse

  Background:
    Given I navigate to "/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed

  Scenario: Tab key moves focus through the login form controls
    When I enter username "standard_user"
    And I press the "Tab"
    And I enter password "secret_sauce"
    And I press the "Tab"
    And I press the "Tab"
    And I press the "Enter"
    Then I should be redirected to products page as "inventory.html"

  Scenario Outline: Tab key moves focus through the login form controls for all accepted usernames
    When I enter username "<username>"
    And I press the "Tab"
    And I enter password "<password>"
    And I press the "Tab"
    And I press the "Tab"
    And I press the "Enter"
    Then I should be redirected to products page as "<expectedPageUrl>"
    And I should be taken to the "<expectedHeader>" page

    Examples:
      | username                | password     | expectedHeader | expectedPageUrl |
      | standard_user           | secret_sauce | Products       | inventory.html  |
      | problem_user            | secret_sauce | Products       | inventory.html  |
      | performance_glitch_user | secret_sauce | Products       | inventory.html  |
      | error_user              | secret_sauce | Products       | inventory.html  |
      | visual_user             | secret_sauce | Products       | inventory.html  |
      | locked_out_user         | secret_sauce |                | saucedemo.com   |
