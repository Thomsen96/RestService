package dtu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
		this.role = Role.CUSTOMER;
	}

	@Given("another new createCustomer session has started with accountId {string}")
	public void anotherNewCreateCustomerSessionHasStartedWithAccountId(String accountId) {
	    this.accountNumber2 = accountId;
	    this.sessionId2 = UUID.randomUUID().toString();
	    this.role = Role.CUSTOMER;
	}
	
	@Given("a new createMerchant session has started with accountId {string}")
	public void aNewCreateMerchantSessionHasStartedWithAccountId(String accountId) {
		this.accountNumber = accountId;
		this.sessionId = UUID.randomUUID().toString();
		this.role = Role.MERCHANT;
	}

	@Given("another new createMerchant session has started with accountId {string}")
	public void anotherNewCreateMerchantSessionHasStartedWithAccountId(String accountId) {
	    this.accountNumber2 = accountId;
	    this.sessionId2 = UUID.randomUUID().toString();
	    this.role = Role.MERCHANT;
	}

	
	@When("the message is sent")
	public void theMessageIsSent() {
		var thread = new Thread(() -> {
			this.customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber, role);
		});
		thread.start();
	}
	
	@When("the createCustomerResponse is received")
	public void theCreateCustomerResponseIsReceived() {
		Event e = new Event(role.CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()});
		accountService.customerCreationResponseHandler(sessionId, e);
	}
	
	@Then("a new customer has been created with a customerId")
	public void aNewCustomerHasBeenCreatedWithACustomerId() throws InterruptedException {
		Thread.sleep(300);
	    assertNotNull(this.customerId);
	}

	@When("the messages are sent at the same time")
	public void theMessagesAreSentAtTheSameTime() {
		var thread1 = new Thread(() -> {
			this.customerId = accountService.createCustomerCreationRequest(sessionId, accountNumber, role);
		});
		var thread2 = new Thread(() -> {
			this.customerId2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2, role);
		});
		thread1.start();
		thread2.start();
	}

	@Then("two distinct CustomerCreationResponses are received")
	public void twoDistinctCustomerCreationResponsesAreReceived() {
	    assertNotNull(this.result);
	    assertNotNull(this.result2);
	    assertNotEquals(this.result, this.result2);
	}
	
	@When("the createCustomerResponses are received in reverse order")
	public void theCreateCustomerResponsesAreReceivedInReverseOrder() {
		accountService.customerCreationResponseHandler(sessionId2,
				new Event(role.CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()}));
		
		accountService.customerCreationResponseHandler(sessionId, 
				new Event(role.CREATION_RESPONSE, new Object[] {UUID.randomUUID().toString()}));
	}
	
}
