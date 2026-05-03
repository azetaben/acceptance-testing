@login_logout @all
Feature: Login Functionality
  As a user,
  I want to login into the application
  inorder to make online purchase.

  Background: navigate from home page to sign up or login page
    Given I navigate to "/"

  Scenario: login with an Accepted username and Password for all users
    And my login credentials are:
      | Accepted username | Password for all users |
      | standard_user     | secret_sauce           |
    And I provide login credentials
    When I click on "Login" button
    Then I should see "Products"

