Feature: Customer endpoint

  Scenario: CustomerCreationRequest
		Given a new createCustomer session has started with accountId "00001"
		When the message is sent
		And the createCustomerResponse is received
		Then a new customer customer has been created with a customerId
		
	Scenario: CustomerCreationRequest Race Condition 
		Given a new createCustomer session has started with accountId "00001"
		And another new createCustomer session has started with accountId "00002"
		When the messages are sent at the same time
		And the createCustomerResponses are received in reverse order
		Then two distinct CustomerCreationResponses are received
				
        

  #Scenario: Create a CustomerCreationRequest
    #Given a REST message with account number "0101010"
#		When a POST is made on /customer
#		Then a "CustomerCreationRequest" event with the account number and a sessionId has been published to the message queue
#	
#	Scenario: Create a customer
#		Given a new session has started
#		When createCustomer is requested
#		And the createCustomerResponse is received
#		Then a new customer customer has been created with a customerId

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
