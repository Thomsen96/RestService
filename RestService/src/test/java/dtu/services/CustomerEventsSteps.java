package dtu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;
import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Application.ServiceHelper;

public class CustomerEventsSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
	
	ServiceHelper serviceHelper = new ServiceHelper();
	AccountService accountService = new AccountService(messageQueue);

	CompletableFuture<String> result = new CompletableFuture<String>();
	CompletableFuture<String> result2 = new CompletableFuture<String>();
	CompletableFuture<Event> customerCreationFuture1 = new CompletableFuture<>();
	CompletableFuture<Event> customerCreationFuture2 = new CompletableFuture<>();
	CompletableFuture<Boolean> customerIdComplete = new CompletableFuture<>();
	
	
	String accountNumber;
	String sessionId;
	String customerId;
	
	String accountNumber2;
	String sessionId2;
	String customerId2;
	
	Role role;
	
	
	int default_timeout;
	
	@BeforeAll()
	public void saveTimeout() {
		this.default_timeout = serviceHelper.TIMEOUT;
	}
	
	@AfterAll()
	public void restoreTimeout() {
		serviceHelper.TIMEOUT = this.default_timeout;
	}

	
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
			try {
				Event event = accountService.createCustomerCreationRequest(sessionId, accountNumber, role);
				EventResponse eventResponse = event.getArgument(0, EventResponse.class);
				this.customerId = eventResponse.isSuccess() ? eventResponse.getArgument(0, String.class) : eventResponse.getErrorMessage();
				customerIdComplete.complete(true);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}
	
	@When("the createCustomerResponse is received")
	public void theCreateCustomerResponseIsReceived() throws InterruptedException {
		EventResponse eventResponse = new EventResponse(sessionId, true, null, "123");
		Thread.sleep(100);
		accountService.customerCreationResponseHandler(new Event(role.CREATION_RESPONSE + "." + sessionId, eventResponse));
	}
	
	@Then("a new customer has been created with a customerId")
	public void aNewCustomerHasBeenCreatedWithACustomerId() throws InterruptedException {
		customerIdComplete.join();
	    assertNotNull(this.customerId);
	}

	
	
	

	@When("the messages are sent at the same time")
	public void theMessagesAreSentAtTheSameTime() {
		var thread1 = new Thread(() -> {
			try {
				Event event1 = accountService.createCustomerCreationRequest(sessionId, accountNumber, role);
				System.out.println("Event1: " + event1);
				EventResponse eventResponse1 = event1.getArgument(0, EventResponse.class);
				this.customerId = eventResponse1.isSuccess() ? eventResponse1.getArgument(0, String.class) : eventResponse1.getErrorMessage();
				customerCreationFuture1.complete(event1);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		var thread2 = new Thread(() -> {
			try {	
				Event event2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2, role);
				System.out.println("Event2: " + event2);
				EventResponse eventResponse2 = event2.getArgument(0, EventResponse.class);
				this.customerId2 = eventResponse2.isSuccess() ? eventResponse2.getArgument(0, String.class) : eventResponse2.getErrorMessage();
				customerCreationFuture2.complete(event2);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		thread1.start();
		thread2.start();
	}
	
	@When("the createCustomerResponses are received in reverse order")
	public void theCreateCustomerResponsesAreReceivedInReverseOrder() throws InterruptedException {
		EventResponse eventResponse1 = new EventResponse(sessionId, true, null, "123");
		Event event1 = new Event(role.CREATION_RESPONSE + "." + sessionId, eventResponse1);
		EventResponse eventResponse2 = new EventResponse(sessionId2, true, null, "321");
		Event event2 = new Event(role.CREATION_RESPONSE + "." + sessionId2, eventResponse2);
		
		Thread.sleep(200);
		accountService.customerCreationResponseHandler(event1);
		accountService.customerCreationResponseHandler(event2);
	}

	@Then("two distinct CustomerCreationResponses are received")
	public void twoDistinctCustomerCreationResponsesAreReceived() {
		Event incommingEvent1 = customerCreationFuture1.join();
		Event incommingEvent2 = customerCreationFuture2.join();
		assertNotEquals(incommingEvent1, incommingEvent2);
		
		EventResponse eventResponse1 = incommingEvent1.getArgument(0, EventResponse.class);
		EventResponse eventResponse2 = incommingEvent2.getArgument(0, EventResponse.class);

	    System.out.println("eventResponse1: " + eventResponse1);
	    System.out.println("eventResponse2: " + eventResponse2);
		assertNotEquals(eventResponse1, eventResponse2);
		
	    assertNotNull(this.result);
	    assertNotNull(this.result2);
	    assertNotEquals(this.result, this.result2);
	}
	
	@Given("the timeout is {int} ms")
	public void theTimeoutIsMs(Integer newTimeout) {
	    serviceHelper.TIMEOUT = newTimeout;
	}
	
	@When("not answered")
	public void notAnswered() throws InterruptedException {
		customerIdComplete.join();
	}

	@Then("a timeout message is received")
	public void aTimeoutMessageIsReceived() {
	    assertEquals("ERROR: Request timed out", customerId);
	}
	
}
