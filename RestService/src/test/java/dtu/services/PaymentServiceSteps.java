package dtu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;
import restService.Application.PaymentService;
import restService.Application.ServiceHelper;
import restService.Domain.PaymentDTO;

public class PaymentServiceSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
	
	ServiceHelper serviceHelper = new ServiceHelper();
	PaymentService paymentService = new PaymentService(messageQueue);

	EventResponse eventResponse;
	PaymentDTO paymentDTO;
	CompletableFuture<Boolean> requestCompleted = new CompletableFuture<>();
	
	
	////
	CompletableFuture<String> result = new CompletableFuture<String>();
	CompletableFuture<String> result2 = new CompletableFuture<String>();
	CompletableFuture<Event> customerCreationFuture1 = new CompletableFuture<>();
	CompletableFuture<Event> customerCreationFuture2 = new CompletableFuture<>();
	
	String sessionId;
	String customerId;
	
	String accountNumber2;
	String sessionId2;
	String customerId2;
	////
	
	
	int default_timeout;
	@Before()
	public void saveTimeout() { this.default_timeout = serviceHelper.TIMEOUT; }
	@After()
	public void restoreTimeout() { serviceHelper.TIMEOUT = this.default_timeout; }

	
	@Given("a payment")
	public void aPayment() {
	    this.paymentDTO = new PaymentDTO();
	    this.sessionId = UUID.randomUUID().toString();
	}
	
	
	@When("the paymentRequest message is sent")
	public void thePaymentRequestMessageIsSent() {
		var thread = new Thread(() -> {
			try {
				eventResponse = paymentService.createPayment(sessionId, paymentDTO);
				requestCompleted.complete(true);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		thread.start();
		
		//verify(messageQueue).publish(new Event(PaymentService.PAYMENT_REQUEST, new EventResponse(sessionId, true, null, paymentDTO)));
	}
	
	@When("the paymentRequestResponse is received")
	public void thePaymentRequestResponseIsReceived() {
	    EventResponse paymentRequestResponse = new EventResponse(sessionId, true, null, null);
	    paymentService.handlePaymentResponse(new Event(PaymentService.PAYMENT_RESPONSE + "." + sessionId, eventResponse));
	}
	
	
	@Then("the received EventResponse is a success")
	public void theReceivedEventResponseIsASuccess() {
		requestCompleted.join();
	    assertTrue(eventResponse.isSuccess());
	}

	@Given("the Payments ServiceHelpers timeout is {int} ms")
	public void thePaymentsServiceHelpersTimeoutIsMs(Integer newTimeout) {
		serviceHelper.TIMEOUT = newTimeout;
	}

	@When("no paymentRequestResponse is received")
	public void noPaymentRequestResponseIsReceived() throws InterruptedException {
		requestCompleted.join();
	}

	@When("a failed paymentRequestResponse is received")
	public void aFailedPaymentRequestResponseIsReceived() {
	    EventResponse paymentRequestResponse = new EventResponse(sessionId, false, null, null);
	    paymentService.handlePaymentResponse(new Event(PaymentService.PAYMENT_RESPONSE + "." + sessionId, eventResponse));
	    requestCompleted.join();
	}

	@Then("the received EventResponse is not a success")
	public void theReceivedEventResponseIsNotASuccess() {
	    assertFalse(eventResponse.isSuccess());
	}
	
	@Then("a Payment Service timeout message is received")
	public void aTimeoutMessageIsReceived() {
	    assertEquals("Payment timed out", eventResponse.getErrorMessage());
	}
	
	
//	@Then("a new customer has been created with a customerId")
//	public void aNewCustomerHasBeenCreatedWithACustomerId() throws InterruptedException {
//		customerIdComplete.join();
//	    assertNotNull(this.customerId);
//	}

	
	
	
//	@When("the messages are sent at the same time")
//	public void theMessagesAreSentAtTheSameTime() {
//		var thread1 = new Thread(() -> {
//			try {
//				Event event1 = accountService.createCustomerCreationRequest(sessionId, paymentDTO, role);
//				System.out.println("Event1: " + event1);
//				EventResponse eventResponse1 = event1.getArgument(0, EventResponse.class);
//				this.customerId = eventResponse1.isSuccess() ? eventResponse1.getArgument(0, String.class) : eventResponse1.getErrorMessage();
//				customerCreationFuture1.complete(event1);
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//		});
//		var thread2 = new Thread(() -> {
//			try {	
//				Event event2 = accountService.createCustomerCreationRequest(sessionId2, accountNumber2, role);
//				System.out.println("Event2: " + event2);
//				EventResponse eventResponse2 = event2.getArgument(0, EventResponse.class);
//				this.customerId2 = eventResponse2.isSuccess() ? eventResponse2.getArgument(0, String.class) : eventResponse2.getErrorMessage();
//				customerCreationFuture2.complete(event2);
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//		});
//		thread1.start();
//		thread2.start();
//	}
	
//	@When("the createCustomerResponses are received in reverse order")
//	public void theCreateCustomerResponsesAreReceivedInReverseOrder() throws InterruptedException {
//		EventResponse eventResponse1 = new EventResponse(sessionId, true, null, "123");
//		Event event1 = new Event(role.CREATION_RESPONSE + "." + sessionId, eventResponse1);
//		EventResponse eventResponse2 = new EventResponse(sessionId2, true, null, "321");
//		Event event2 = new Event(role.CREATION_RESPONSE + "." + sessionId2, eventResponse2);
//		
//		Thread.sleep(200);
//		accountService.customerCreationResponseHandler(event1);
//		accountService.customerCreationResponseHandler(event2);
//	}

//	@Then("two distinct CustomerCreationResponses are received")
//	public void twoDistinctCustomerCreationResponsesAreReceived() {
//		Event incommingEvent1 = customerCreationFuture1.join();
//		Event incommingEvent2 = customerCreationFuture2.join();
//		assertNotEquals(incommingEvent1, incommingEvent2);
//		
//		EventResponse eventResponse1 = incommingEvent1.getArgument(0, EventResponse.class);
//		EventResponse eventResponse2 = incommingEvent2.getArgument(0, EventResponse.class);
//
//	    System.out.println("eventResponse1: " + eventResponse1);
//	    System.out.println("eventResponse2: " + eventResponse2);
//		assertNotEquals(eventResponse1, eventResponse2);
//		
//	    assertNotNull(this.result);
//	    assertNotNull(this.result2);
//	    assertNotEquals(this.result, this.result2);
//	}
	
//	@Given("the timeout is {int} ms")
//	public void theTimeoutIsMs(Integer newTimeout) {
//	    serviceHelper.TIMEOUT = newTimeout;
//	}
	
//	@When("not answered")
//	public void notAnswered() throws InterruptedException {
//		customerIdComplete.join();
//	}
	
}
