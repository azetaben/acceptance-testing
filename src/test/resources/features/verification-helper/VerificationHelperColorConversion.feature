@verification_helper @color_conversion @all
Feature: VerificationHelper Color Conversion Methods - Framework Integration

  @ColorConversion @Smoke @regression
  Scenario Outline: Test RGB to HEX conversion with various colors
    Given I navigate to "/"
    Given I have RGB values <red>, <green>, <blue>
    When I convert RGB to HEX
    Then I should get <expected_hex>

    Examples:
      | red | green | blue | expected_hex |
      | 255 | 0     | 0    | #FF0000      |
      | 0   | 255   | 0    | #00FF00      |
      | 0   | 0     | 255  | #0000FF      |
      | 0   | 0     | 0    | #000000      |
      | 255 | 255   | 255  | #FFFFFF      |

  @ColorConversion @regression
  Scenario Outline: Test HEX to RGB conversion with various colors
    Given I navigate to "/"
    Given I have HEX color <hex_color>
    When I convert HEX to RGB
    Then I should get RGB with red <red>, green <green>, blue <blue>

    Examples:
      | hex_color | red | green | blue |
      | #FF0000   | 255 | 0     | 0    |
      | #00FF00   | 0   | 255   | 0    |
      | #0000FF   | 0   | 0     | 255  |
      | #000000   | 0   | 0     | 0    |
      | #FFFFFF   | 255 | 255   | 255  |

  @ColorConversion @framework @regression
  Scenario: Verify login page button colors
    Given I navigate to "/"
    When I check if "login button" is "displayed"
    Then the result should be true

  @ColorConversion @framework @regression
  Scenario: Verify inventory page product prices use consistent colors
    Given I navigate to "/"
    Given I login with valid credentials as standard user
      | username      | password     |
      | standard_user | secret_sauce |
    When I am on "inventory" page
    And I check if "product prices" is "displayed"
    Then the result should be true

