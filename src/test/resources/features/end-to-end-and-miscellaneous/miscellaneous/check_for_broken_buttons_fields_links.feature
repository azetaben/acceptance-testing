@miscellaneous @regression @all
Feature: Page Element Verification

  Background: Navigate to the login page
    Given I navigate to "https://saucedemo.com/"

  @TC-MC_002
  Scenario: Check for broken links on the webpage
    Then I check for broken links

  @TC-MC_003
  Scenario: Find buttons on the webpage
    Then I verify the buttons on the page

  @TC-MC_004
  Scenario: Find input fields on the webpage
    Then I find input fields on the page

  @TC-MC_005
  Scenario Outline: Find broken button, input and links on the page for specific user
    When I enter username and password with "<username>" and "<password>"
    And I check for broken buttons, input fields and links on the webpage
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
