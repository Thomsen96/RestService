#Feature: Customer endpoint
#
#  Scenario: Create a CustomerCreationRequest
#    Given a REST message with account number "0101010"
#		When a POST is made on /customer
#		Then a "CustomerCreationRequest" event with the account number and a sessionId has been published to the message queue
#	
#	
#  #Scenario: Receive a CustomerCreationResponse
#    #Given a CustomerCreationResponse message with sessionId "000111" and CustomerId "11111"
##		When a POST is made on /customer
##		Then a "CustomerCreationRequest" event with the account number and a sessionId has been published to the message queue
#
#  Scenario: CustomerCreationRequest Race Condition
#    Given a REST message with account number "0101010"
#    And another REST message with account number "0202020"
#  	When the two messages are sent at the same time
#  	And the requests are answered
#  	Then two distinct CustomerCreationResponses are received