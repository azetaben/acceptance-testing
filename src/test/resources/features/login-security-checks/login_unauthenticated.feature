Feature: Login security checks for Sauce Demo
  As a user,
  I want to ensure that the login functionality is secure against common attack vectors
  So that my account and data are protected.

  @TC_L_LF_031 @security @login @invalidLogin @regression
  Scenario Outline: 031 - block unauthenticated direct navigation to protected routes
    When I navigate to "<protected_url>"
    Then I should be redirected to the login page
    And page url should be "saucedemo.com"
    Then I should see an error message "<error message>" displayed

    Examples:
      | protected_url          | error message                                                                       |
      | inventory.html         | Epic sadface: You can only access '/inventory.html' when you are logged in.         |
      | cart.html              | Epic sadface: You can only access '/cart.html' when you are logged in.              |
      | checkout-step-one.html | Epic sadface: You can only access '/checkout-step-one.html' when you are logged in. |
      | checkout-step-two.html | Epic sadface: You can only access '/checkout-step-two.html' when you are logged in. |
      | checkout-complete.html | Epic sadface: You can only access '/checkout-complete.html' when you are logged in. |