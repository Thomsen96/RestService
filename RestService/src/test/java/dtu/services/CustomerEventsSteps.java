package dtu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.*;
import messaging.Event;
import messaging.MessageQueue;
import restService.Application.AccountService;

public class CustomerEventsSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
	
	AccountService accountService = new AccountService(messageQueue);
	
	
	CompletableFuture<String> result = new CompletableFuture<String>();
	CompletableFuture<String> result2 = new CompletableFuture<String>();
	
	
	String accountNumber;
	String sessionId;
	String customerId;
	
	String accountNumber2;
	String sessionId2;
	String customerId2;
//	
//	public void aaa() {
//		// Data from REST
//		String sessionID = UUID.randomUUID().toString();
//		String accountNo = UUID.randomUUID().toString();
//		// Invoke AccountService
//		String response = accountService.createCustomerCreationRequest(sessionID, accountNo);
//		// Create a thread to simulate response
//		var thread = new Thread(() -> {
//			Event e = new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {"71234781"});
//			accountService.customerCreationResponseHandler(sessionId, e);
//		});
//		thread.start();
//	}
//	
//	
//	@Then("a {string} event with the account number and a sessionId has been published to the message queue")
//	public void anEventWithTheAccountNumberAndASessionIdHasBeenPublishedToTheMessageQueue(String eventType) {
//	    Event e = new Event(eventType, new Object[] {accountNumber, sessionId});
//	    verify(messageQueue).publish(e);
//	}
//
//	@When("the two messages are sent at the same time")
//	public void theTwoMessagesAreSentAtTheSameTime() {
//		var thread1 = new Thread(() -> {
//			accountService.createCustomerCreationRequest(sessionId, accountNumber);
//		});
//		var thread2 = new Thread(() -> {
//			result2.complete(accountService.createCustomerCreationRequest(sessionId2, accountNumber2));
//		});
//		thread1.start();
//		thread2.start();
//	}
//	
//	@When("the requests are answered")
//	public void theRequestsAreAnswered() {
//		Event e = new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {"71234781"});
//		accountService.customerCreationResponseHandler(sessionId, e);
//	    assertNotNull(result2);
//	    assertNotEquals(result, result2);
//	}
//	
//	
//	@Given("a new createCustomer session has started with accountId {string}")
//	public void aNewCreateCustomerSessionHasStartedWithAccountId(String accountId) {
//	    this.accountNumber = accountId;
//	    this.sessionId = UUID.randomUUID().toString();
//	}
//	
//	@Given("another new createCustomer session has started with accountId {string}")
//	public void anotherNewCreateCustomerSessionHasStartedWithAccountId(String accountId) {
//	    this.accountNumber2 = accountId;
//	    this.sessionId2 = UUID.randomUUID().toString();
//	}
//	
//	@When("the message is sent")
//	public void theMessageIsSent() {
//	    customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber);
//	}
//	
//	@When("the createCustomerResponse is received")
//	public void theCreateCustomerResponseIsReceived() {
//		Event e = new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {"71234781"});
//		accountService.customerCreationResponseHandler(sessionId, e);
//	}
//	
//	@Then("a new customer customer has been created with a customerId")
//	public void aNewCustomerCustomerHasBeenCreatedWithACustomerId() {
//	    assertNotNull(customerId);
//	}
//	
//	@When("the messages are sent at the same time")
//	public void theMessagesAreSentAtTheSameTime() {
//		var thread1 = new Thread(() -> {
//			customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber);
//		});
//		var thread2 = new Thread(() -> {
//			customerId2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2);
//		});
//		thread1.start();
//		thread2.start();
//	}
//	
//	@When("the createCustomerResponses are received in reverse order")
//	public void theCreateCustomerResponsesAreReceivedInReverseOrder() {
//		var thread1 = new Thread(() -> {
//			customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber);
//		});
//		var thread2 = new Thread(() -> {
//			customerId2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2);
//		});
//		thread1.start();
//		thread2.start();
//	}
//	
//	@Then("two distinct CustomerCreationResponses are received")
//	public void twoDistinctCustomerCreationResponsesAreReceived() {
//	    assertNotNull(customerId);
//	    assertNotNull(customerId2);
//	    assertNotEquals(customerId, customerId2);
//	}
	
}
