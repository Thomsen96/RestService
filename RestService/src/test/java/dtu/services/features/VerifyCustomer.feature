Feature: Customer endpoint

	Scenario: A REST GET is posted to Customer
     Given the endpoint is "/customer"
     When a REST GET i posted
     Then the message "Hello from customer root" is received

    Scenario: A customer request tokens
      Given a customer requests tokens
      Then an event is placed on the rabbitMQ queue
