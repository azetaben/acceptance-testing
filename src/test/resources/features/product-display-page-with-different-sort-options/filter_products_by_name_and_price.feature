@ProductsDisplayedPage @ParallelRun @all @error_validation_tests
Feature: Product Sorting

  Background: navigate to product page
    Given I navigate to "https://saucedemo.com/"

  @TC_PDP_001
  Scenario Outline: By default should display product titles A to Z for acceptable users except locked out user
    When I login username and password with "<username>" and "<password>"
    Then I can see product page header "Products" displayed
    And I should see a list of products
    Then I should see 6 products displayed
    And I can see "Name (A to Z)" is selected by default
    Then I should see sorted result for "Name (A to Z)"
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @TC_PDP_002 @error_validation_tests
  Scenario Outline: should display product titles Z to A for acceptable users except locked out and problem user
    When I login username and password with "<username>" and "<password>"
    Then I can see product page header "Products" displayed
    And I should see a list of products
    Then I should see 6 products displayed
    When I select "Name (Z to A)" from the sort drop down list
    Then I should see sorted result for "Name (Z to A)"
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @TC_PDP_002_1 @error_validation_tests
  Scenario Outline: should display product titles Z to A for problem user
    When I login username and password with "<username>" and "<password>"
    Then I can see product page header "Products" displayed
    And I should see a list of products
    Then I should see 6 products displayed
    When I select "Name (Z to A)" from the sort drop down list
    Then I should see sorted result for "Name (Z to A)" not matching option
    Examples:
      | username     | password     |
      | problem_user | secret_sauce |

  @TC_PDP_003
  Scenario Outline: Price (high-to-low) prices should be displayed for acceptable users except locked out user and problem user
    When I login username and password with "<username>" and "<password>"
    And I should be taken to the "Products" page
    And Product count is greater than 0
    And I should see a list of products
    Then I should see 6 products displayed
    When I select price option "Price (high to low)" from the sort drop down list
    Then I should see sorted result for "Price (high to low)"
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @TC_PDP_004
  Scenario Outline: Price (low-to-high) should be displayed for acceptable users users except locked out and problem user
    When I login username and password with "<username>" and "<password>"
    And I should be taken to the "Products" page
    And Product count is greater than 0
    And I should see a list of products
    Then I should see 6 products displayed
    When I select price option "Price (low to high)" from the sort drop down list
    Then I should see sorted result for "Price (low to high)"
    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @TC_PDP_004
  Scenario Outline: Price (low-to-high) should be displayed but not matching for problem_user
    When I login username and password with "<username>" and "<password>"
    And I should be taken to the "Products" page
    And Product count is greater than 0
    And I should see a list of products
    Then I should see 6 products displayed
    When I select price option "Price (low to high)" from the sort drop down list
    Then I should see sorted result for "Price (low to high)" not matching option
    Examples:
      | username     | password     |
      | problem_user | secret_sauce |

