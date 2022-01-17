package dtu.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.cucumber.java.en.*;
import messaging.Event;
import messaging.MessageQueue;
import restService.Presentation.CustomerEventHandler;

public class CustomerEventsSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
	CustomerEventHandler customerEventHandler = new CustomerEventHandler(messageQueue);
	
	String accountNumber;
	String sessionId;
	
	@Given("A REST message with account number {string}")
	public void aRESTMessageWithAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@When("a POST is made on \\/customer")
	public void aPOSTIsMadeOnCustomer() {
	    sessionId = customerEventHandler.createCustomerCreationRequest(this.accountNumber);
	}

	@Then("a {string} event with the account number and a sessionId has been published to the message queue")
	public void anEventWithTheAccountNumberAndASessionIdHasBeenPublishedToTheMessageQueue(String eventType) {
	    Event e = new Event(eventType, new Object[] {accountNumber, sessionId});
	    verify(messageQueue).publish(e);
	}

	
}
