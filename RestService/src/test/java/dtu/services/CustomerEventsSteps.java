package dtu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.*;
import messaging.Event;
import messaging.MessageQueue;
import restService.Application.AccountService;
import restService.Presentation.CustomerEventHandler;

public class CustomerEventsSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
//	CustomerEventHandler customerEventHandler = new CustomerEventHandler(messageQueue);
	AccountService customerEventHandler = new AccountService(messageQueue);
	
	
	CompletableFuture<String> result = new CompletableFuture<String>();
	CompletableFuture<String> result2 = new CompletableFuture<String>();
	String customerId;
	String customerId2;
	
	String accountNumber;
	String accountNumber2;
	String sessionId;
	String sessionId2;

	
	public void aPOSTIsMadeOnCustomer() {
	    //sessionId = customerEventHandler.createCustomerCreationRequest(this.accountNumber);
	}

	@Then("a {string} event with the account number and a sessionId has been published to the message queue")
	public void anEventWithTheAccountNumberAndASessionIdHasBeenPublishedToTheMessageQueue(String eventType) {
	    Event e = new Event(eventType, new Object[] {accountNumber, sessionId});
	    verify(messageQueue).publish(e);
	}

	
	@Given("another REST message with account number {string}")
	public void anotherRESTMessageWithAccountNumber(String accountNumber) {
		this.accountNumber2 = accountNumber;
	}

	@When("the two messages are sent at the same time")
	public void theTwoMessagesAreSentAtTheSameTime() {
		var thread1 = new Thread(() -> {
			customerEventHandler.createCustomerCreationRequest(sessionId, accountNumber);
		});
		var thread2 = new Thread(() -> {
			result2.complete(customerEventHandler.createCustomerCreationRequest(sessionId2, accountNumber2));
		});
		thread1.start();
		thread2.start();
	}
	
	@When("the requests are answered")
	public void theRequestsAreAnswered() {
		Event e = new Event(customerEventHandler.CUSTOMER_CREATION_RESPONSE, new Object[] {"71234781"});
		customerEventHandler.extracted(sessionId, e);
	    assertNotNull(result2);
	    assertNotEquals(result, result2);
	}
	
}
