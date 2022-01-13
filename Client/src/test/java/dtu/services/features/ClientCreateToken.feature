Feature: Client create tokens feature

  Scenario: Customer requests 5 tokens
    Given a customer with id "1234561234"
    When the customer requests 5 tokens
    Then the customer has 5 tokens