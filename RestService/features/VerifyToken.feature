Feature: Customer endpoint

  Scenario: A customer request tokens
    Given the customer with id "idofCustomer" wants 5 tokens request tokens
    When the token service responds with a token
    Then the token request is sent to the tokenService
    When token service sends a response on the queue
    Then the service can return the token to the user
