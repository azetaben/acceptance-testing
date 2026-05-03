@login_logout @all
Feature: Login Functionality
  As a user,
  I want to login into the application
  inorder to make online purchase.

  Background: navigate from home page to sign up or login page
    Given I navigate to "/"
    #And I check for broken buttons, input fields and links on the webpage
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


  Scenario Outline: login with different Accepted usernames and Password for all users
    When I login in login page with all "<allAccepted usernames>" and password for all users "secret_sauce"
    Then I should see "<expected confirmation message>"
    Examples:
      | allAccepted usernames   | expected confirmation message                       |
      | standard_user           | Products                                            |
      | locked_out_user         | Epic sadface: Sorry, this user has been locked out. |
      | problem_user            | Products                                            |
      | performance_glitch_user | Products                                            |
      | error_user              | Products                                            |
      | visual_user             | Products                                            |

  Scenario Outline: login with different Accepted usernames and password
    When I login in login page with all "<allAccepted usernames>" and password for all users "<Password for all users>"
    Then I should see "<expected confirmation message>"
    Examples:
      | allAccepted usernames   | Password for all users | expected confirmation message                       |
      | standard_user           | secret_sauce           | Products                                            |
      | locked_out_user         | secret_sauce           | Epic sadface: Sorry, this user has been locked out. |
      | problem_user            | secret_sauce           | Products                                            |
      | performance_glitch_user | secret_sauce           | Products                                            |
      | error_user              | secret_sauce           | Products                                            |
      | visual_user             | secret_sauce           | Products                                            |


  @TC_LLF_001 @validLogin @regression @smoke @sanity
  Scenario:  001 - login with valid credentials as a standard user
    When I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    And I should be redirected to products page as "inventory.html"
    And I should be taken to the "Products" page
    And I should be redirected to the products page
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And Product count is greater than 0
    And I should see a list of products
    And I should see 6 products displayed

  @TC_LLF_002 @validLogin @regression
  Scenario: 002 - login in login page
    When I login in login page
      | standard_user | secret_sauce |
    Then I can see product page header "Products" displayed
    And Product count is greater than 0
    And I should see 6 products displayed

  @TC_LLF_003 @invalidLogin @regression
  Scenario:  003 - login with invalid credentials as a locked_out_user
    When I login with valid credentials as standard user
      | username        | password     |
      | locked_out_user | secret_sauce |
    And page url should be "saucedemo.com"
    And page title should be "Swag Labs"
    Then an error message "Epic sadface: Sorry, this user has been locked out." should be displayed
    And "Epic sadface: Sorry, this user has been locked out." should be displayed

  @TC_LLF_004 @validLogin @regression
  Scenario:  004 - login with valid credentials as a problem_user
    When I login with valid credentials as standard user
      | username     | password     |
      | problem_user | secret_sauce |
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I should see a list of products
    And I should see 6 products displayed

  @TC_LLF_005 @validLogin @regression
  Scenario:  005 - login with valid credentials as a performance_glitch_user
    When I login with valid credentials as standard user
      | username                | password     |
      | performance_glitch_user | secret_sauce |
    And page url should be "inventory.html"
    Then I can see product page header "Products" displayed
    And I should see a list of products
    And I should see 6 products displayed

  @TC_LLF_006 @ValidLogin @regression
  Scenario Outline: 006 - login with Accepted usernames valid credentials
    When I login with username "<username>" and "<password>"
    Then I should see "<confirmation message>" is displayed
    And I should be taken to the "Products" page
    And I should be redirected to the products page
    And I can see product page header "Products" displayed
    And I should see a list of products
    And I should see 6 products displayed
    Examples:
      | username                | password     | confirmation message |
      | standard_user           | secret_sauce | Products             |
      | problem_user            | secret_sauce | Products             |
      | performance_glitch_user | secret_sauce | Products             |

  @TC_LLF_007 @login @validLogin @regression
  Scenario: 007 - Successful login with standard user
    And I enter username "standard_user" and password "secret_sauce"
    When I tap "Login" button
    And I should be taken to the "Products" page
    Then I should be redirected to the products page
    And I should see a list of products
    And I should see 6 products displayed

  @ErrorValidation
  @TC_LLF_008 @login @invalidLogin @regression
  Scenario: 008 - Failed login with invalid Accepted usernames credentials
    And I enter username "standard_user" and password "secret_sauce00"
    When I tap "Login" button
    Then an error message "Epic sadface: Username and password do not match any user in this service" should be displayed

  @ErrorValidation
  @TC_LLF_009 @login @invalidLogin @regression
  Scenario: 009 - Failed login with invalid Accepted usernames credentials- locked out user
    When I enter username "locked_out_user" and password "secret_sauce"
    And I tap "Login" button
    Then an error message "Epic sadface: Sorry, this user has been locked out." should be displayed

  @TC_LLF_010 @login @validLogin @regression @performance
  Scenario: 010 - login as Accepted usernames credentials - performance glitch user
    When I enter username "performance_glitch_user" and password "secret_sauce"
    And I tap "Login" button
    Then I should be redirected to the products page
    And I should be taken to the "Products" page

  @TC_LLF_011 @ErrorValidation @login @invalidLogin @regression
  Scenario Outline: 011 - login with invalid credentials
    When I login with username "<username>" and "<password>"
    Then I should see an error message "<confirmation message>" displayed
    Examples:
      | username                                      | password                                      | confirmation message                                                      |
      | locked_out_user                               | secret_sauce                                  | Epic sadface: Sorry, this user has been locked out.                       |
      | locked_out_user@                              | secret_sauce                                  | Epic sadface: Username and password do not match any user in this service |
      | locked_out_user@                              |                                               | Epic sadface: Password is required                                        |
      |                                               | secret_sauce                                  | Epic sadface: Username is required                                        |
      |                                               |                                               | Epic sadface: Username is required                                        |
      | wet                                           |                                               | Epic sadface: Password is required                                        |
      | £$%&*=                                        |                                               | Epic sadface: Password is required                                        |
      | 1                                             | 1                                             | Epic sadface: Username and password do not match any user in this service |
      | a                                             | a                                             | Epic sadface: Username and password do not match any user in this service |
      | ^                                             | ^                                             | Epic sadface: Username and password do not match any user in this service |
      | ^                                             | 012345678901234567890123456789009800000000000 | Epic sadface: Username and password do not match any user in this service |
      | 012345678901234567890123456789009800000000000 | 123456                                        | Epic sadface: Username and password do not match any user in this service |


