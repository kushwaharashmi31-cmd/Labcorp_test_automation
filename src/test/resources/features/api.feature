Feature: API Testing

  Scenario: Validate GET API
    Given I send GET request
    Then I validate response fields

  Scenario: Validate POST API
    Given I send POST request
    Then I validate POST response
