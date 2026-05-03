@regression @security @auth @login
Feature: Login security and penetration testing
  As a security-conscious application owner
  I want login flows to resist common attack patterns
  So that authentication remains protected against misuse and tampering

  Background:
    # Common public-entry setup so security scenarios begin from the unauthenticated landing experience.
    Given I navigate to "/"
    Then I should see the login form
    And I should see the login form with the following input fields and buttons
      | Username |
      | Password |
      | Login    |

  @regression @login @ui @security
  Scenario: Login form is present with correct labels and buttons
    When I enter username "standard_user" and password "secret_sauce"
    Then the password should be hidden

  @critical @regression @authorization @security @bug
  Scenario: Unauthorized access prevention - product (Gap Check)
    Given I attempt to navigate directly to the product page
    And I should see the login form


  @critical @regression @authorization @security
  Scenario: Unauthorized access prevention - cart page (Gap Check)
    Given I attempt to navigate directly to the cart page
    And I should see the login form


  @regression @security @negative
  Scenario Outline: SQL injection prevention on login
    When I input login name as "<sqli_payload>"
    And I input password "Password123!"
    And I click on the login button
    Then the page URL should not contain any SQL keywords

    Examples:
      | sqli_payload               |
      | ' OR '1'='1                |
      | admin' --                  |
      | ' UNION SELECT NULL, NULL# |

  @regression @security @validation
  Scenario: Secure password masking
    Then the password field should have the type "password"
    And any typed character in the password field should be masked
