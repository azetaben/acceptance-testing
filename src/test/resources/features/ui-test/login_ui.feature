@login_logout @all
Feature: Login Functionality
  As a user,
  I want to login into the application
  inorder to make online purchase.

  Scenario: Navigate from home page to sign up or login page
    Given I navigate to "/"
    And I check for broken buttons, input fields and links on the webpage
    And page url should be "saucedemo.com/"
    And page title should be "Swag Labs"
    And logo is displayed
    And the login form is displayed
    And I can see login form input field attribute value as:
      | Username |
      | Password |
    And I can see "Login" button displayed
    And Should see "Accepted usernames are:" and "Password for all users:" information:
      | Accepted usernames are: |
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |
      | Password for all users: |
      | secret_sauce            |
    And I can see "Password for all users:":
      | secret_sauce |
    And Accepted usernames and Password for all users are displayed
    And I can see Accepted usernames are:
      | standard_user           |
      | locked_out_user         |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |
    And I can see Password for all users:
      | secret_sauce |