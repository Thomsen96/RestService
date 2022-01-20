Feature: Payment endpoint

#	PaymentDTO  token, merchant, amount, description; 
		## Payment
  Scenario: Create successful payment
		Given a payment
		When the paymentRequest message is sent
		And the paymentRequestResponse is received
		Then the received EventResponse is a success


		## Payment Request timed out
  #Scenario: Payment request times out
#		Given a payment
#		And the Payments ServiceHelpers timeout is 20 ms
#		When the paymentRequest message is sent
#		And no paymentRequestResponse is received
#		Then a Payment Service timeout message is received

#		## Not enough money
  #Scenario: Unsuccessful payment
#		Given a payment
#		When the paymentRequest message is sent
#		And a failed paymentRequestResponse is received
#		Then the received EventResponse is not a success
		
#		## Multiple Payments -- Race condition
  #Scenario: Multiple payments
#		Given a payment
#		And another payment
#		When both of the paymentRequest message are sent
#		And the paymentRequestResponses are received in reverse order
#		Then the received EventResponse is a success