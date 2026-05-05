@login_logout @dataDriven @externalData18 @all
Feature: Login Data-Driven Tests From External Sources
  As a user
  I want to execute login validations using external test data
  So that credentials and expected outcomes are maintained outside feature files

  Background: Navigate to the SauceDemo login page
    Given I navigate to "https://saucedemo.com/"
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And the login form is displayed

  @jsonData @excelData @regression @ErrorValidation
  Scenario Outline: <source> <testCaseId> external login validation
    When I execute login using "<source>" source for test case "<testCaseId>"
    Then the login outcome should match the external data

    Examples:
      | source | testCaseId  |
      | json   | JSON_TC_001 |
      | json   | JSON_TC_002 |
      | json   | JSON_TC_003 |
      | json   | JSON_TC_004 |
      | json   | JSON_TC_005 |
      | json   | JSON_TC_006 |
      | json   | JSON_TC_007 |
      | json   | JSON_TC_008 |
      | json   | JSON_TC_009 |
      | json   | JSON_TC_010 |
      | json   | JSON_TC_011 |
      | json   | JSON_TC_012 |
      | excel  | XL_TC_001   |
      | excel  | XL_TC_002   |
      | excel  | XL_TC_003   |
      | excel  | XL_TC_004   |
      | excel  | XL_TC_008   |
      | excel  | XL_TC_010   |

  Scenario: Login driven from JSON row
    When I execute login using "json" source for test case "JSON_TC_001"
    Then the login outcome should match the external data

  Scenario: Login driven from Excel row
    When I execute login using "excel" source for test case "XL_TC_002"
    Then the login outcome should match the external data
