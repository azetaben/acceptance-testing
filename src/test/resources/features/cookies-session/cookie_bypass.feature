@regression @auth @session @cookie-bypass
Feature: restore an authenticated session with cookie bypass
  As an authenticated user
  I want session cookies to restore my logged-in state
  So that I can return to the application without signing in again

  @regression @session@landing
  Scenario: restore an authenticated session to the login page
    Given I navigate to "/"
    And I restore the session to the login page
    And I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I should be taken to the "Products" page
    And I restore the session to the inventory page


  @regression @session @inventory
  Scenario: restore an authenticated session to the inventory page
#    And I restore the session to the login page
#   And I login with valid credentials as standard user
#    | username      | password     |
#    | standard_user | secret_sauce |
#   And I should be taken to the "Products" page
    And I restore the session to the inventory page


