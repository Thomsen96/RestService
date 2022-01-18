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
import restService.Application.AccountService.Role;

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
	
	Role role;

	
	@Given("a new createCustomer session has started with accountId {string}")
	public void aNewCreateCustomerSessionHasStartedWithAccountId(String accountNumber) {
		this.accountNumber = accountNumber;
		this.sessionId = UUID.randomUUID().toString();
	}

	@Given("another new createCustomer session has started with accountId {string}")
	public void anotherNewCreateCustomerSessionHasStartedWithAccountId(String accountId) {
	    this.accountNumber2 = accountId;
	    this.sessionId2 = UUID.randomUUID().toString();
	}
	
	@When("the message is sent")
	public void theMessageIsSent() {
		var thread = new Thread(() -> {
			customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber, Role.CUSTOMER);
		});
		thread.start();
	}
	
	@When("the createCustomerResponse is received")
	public void theCreateCustomerResponseIsReceived() {
		Event e = new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()});
		accountService.customerCreationResponseHandler(sessionId, e);
	}
	
	@Then("a new customer customer has been created with a customerId")
	public void aNewCustomerCustomerHasBeenCreatedWithACustomerId() {
	    assertNotNull(customerId);
	}

	@When("the messages are sent at the same time")
	public void theMessagesAreSentAtTheSameTime() {
		var thread1 = new Thread(() -> {
			customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber, Role.CUSTOMER);
		});
		var thread2 = new Thread(() -> {
			customerId2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2, Role.CUSTOMER);
		});
		thread1.start();
		thread2.start();
	}

	@Then("two distinct CustomerCreationResponses are received")
	public void twoDistinctCustomerCreationResponsesAreReceived() {
	    assertNotNull(result);
	    assertNotNull(result2);
	    assertNotEquals(result, result2);
	}
	
	@When("the createCustomerResponses are received in reverse order")
	public void theCreateCustomerResponsesAreReceivedInReverseOrder() {
		accountService.customerCreationResponseHandler(sessionId2,
				new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()}));
		
		accountService.customerCreationResponseHandler(sessionId, 
				new Event(AccountService.CUSTOMER_CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()}));
	}
	
}
