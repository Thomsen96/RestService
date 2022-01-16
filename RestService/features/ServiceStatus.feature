Feature: Query services for status

  Scenario: Token service status
    When the Token service is requested for its status
    Then the event "TokenStatusRequest" have been sent
    When the token service replies with the status message "Token service ready"
    Then the status message is "Token service ready"

  Scenario: Customer service status
    When the Customer service is requested for its status
    Then the event "CustomerStatusRequest" have been sent
    When the Customer service replies with the status message "Customer service ready"
    Then the status message is "Customer service ready"
