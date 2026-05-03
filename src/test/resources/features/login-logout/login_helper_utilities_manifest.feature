@login_helper_utilities_manifest
Feature: Login helper utilities manifest coverage

  Background:
    Given I navigate to "https://www.saucedemo.com/"
    And the login form is displayed

  @login_helper_utilities_path_check
  Scenario: Login and validate helper utilities coverage manifest
    When I perform general login with "${user:STANDARD_USERNAME}" and "${user:PASSWORD}"
    Then I should reach inventory page from general login flow
    And all helper utilities listed in manifest "src/test/resources/testData/jsonFiles/helper_utilities_path_manifest.json" should exist

