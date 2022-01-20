Feature: A user requests a report

  Scenario: Merchant report request
  	Given a merchant
  	When a getReport request message is sent
  	And the getReport response message is received
  	Then a successful EventResponse is received
  
  #Scenario: Merchant report request times out
  
  
  #Scenario: Create successful payment
#		Given a payment
#		When the paymentRequest message is sent
#		And the paymentRequestResponse is received
#		Then the received EventResponse is a success
#
#
#		## Payment Request timed out
  #Scenario: Payment request times out
#		Given a payment
#		And the Payments ServiceHelpers timeout is 20 ms
#		When the paymentRequest message is sent
#		And no paymentRequestResponse is received
#		Then a Payment Service timeout message is received
#
#		## Not enough money
  #Scenario: Unsuccessful payment
#		Given a payment
#		When the paymentRequest message is sent
#		And a failed paymentRequestResponse is received
#		Then the received EventResponse is not a success
		