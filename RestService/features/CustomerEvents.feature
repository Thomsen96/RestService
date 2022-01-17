Feature: Customer endpoint

  Scenario: Create a CustomerCreationRequest
    Given A REST message with account number "0101010"
		When a POST is made on /customer
		Then a "CustomerCreationRequest" event with the account number and a sessionId has been published to the message queue