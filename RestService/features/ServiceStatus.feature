Feature: Query services for status

  Scenario: Token service status
    When the Token service is requested for its status
    Then the event "TokenStatusRequest" have been sent
    When the Token service replies with the status message "Token service ready"
    Then the status message is "Token service ready"

  Scenario: Token service status request times out
  	Given the ServiceHelper timeout is 10 ms
    When the Token service is requested for its status
    And the service does not answer
    Then the status message is "No reply from a Token service"

  Scenario: Account service status
    When the Account service is requested for its status
    Then the event "AccountStatusRequest" have been sent
    When the Account service replies with the status message "Account service ready"
    Then the status message is "Account service ready"

  Scenario: Account service status request times out
  	Given the ServiceHelper timeout is 10 ms
    When the Account service is requested for its status
    And the service does not answer
    Then the status message is "No reply from a Account service"

   