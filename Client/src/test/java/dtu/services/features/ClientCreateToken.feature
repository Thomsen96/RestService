Feature: REST call on customer endpoint

  Scenario: A REST call is made on the customer endpoint
    Given the endpoint is "/customers" 
    When a REST GET is made
    Then the message "Hello from customer root" is received
    
# {customerId}/{numberOfTokens}