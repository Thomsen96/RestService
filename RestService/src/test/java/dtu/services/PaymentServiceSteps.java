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
import restService.Domain.DTO;

public class PaymentServiceSteps {

//	static MessageQueue messageQueue = new MockMessageQueue();
	static MessageQueue messageQueue = mock(MessageQueue.class);
	
	ServiceHelper serviceHelper = new ServiceHelper();
	PaymentService paymentService = new PaymentService(messageQueue);

	EventResponse eventResponse;
	DTO.CreatePayment paymentDTO;
	CompletableFuture<Boolean> requestCompleted = new CompletableFuture<>();
	
	String sessionId;
	
	
	int default_timeout;
	@Before()
	public void saveTimeout() { this.default_timeout = ServiceHelper.TIMEOUT; }
	@After()
	public void restoreTimeout() { ServiceHelper.TIMEOUT = this.default_timeout; }

	
	@Given("a payment")
	public void aPayment() {
	    this.paymentDTO = new DTO.CreatePayment();
	    this.sessionId = UUID.randomUUID().toString();
	}
	
	
	@When("the paymentRequest message is sent")
	public void thePaymentRequestMessageIsSent() {
		var thread = new Thread(() -> {
			try {
				eventResponse = paymentService.createPaymentRequest(sessionId, paymentDTO);
				requestCompleted.complete(true);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}
	
	@When("the paymentRequestResponse is received")
	public void thePaymentRequestResponseIsReceived() throws InterruptedException {
	    EventResponse paymentRequestResponse = new EventResponse(sessionId, true, null);
	    Thread.sleep(100);
	    paymentService.handlePaymentResponse(new Event(PaymentService.PAYMENT_RESPONSE  + sessionId, paymentRequestResponse));
	}
	
	
	@Then("the received EventResponse is a success")
	public void theReceivedEventResponseIsASuccess() throws InterruptedException {
		requestCompleted.join();
	    assertTrue(this.eventResponse.isSuccess());
	}

	@Given("the Payments ServiceHelpers timeout is {int} ms")
	public void thePaymentsServiceHelpersTimeoutIsMs(Integer newTimeout) {
		ServiceHelper.TIMEOUT = newTimeout;
	}

	@When("no paymentRequestResponse is received")
	public void noPaymentRequestResponseIsReceived() throws InterruptedException {
		requestCompleted.join();
	}

	@When("a failed paymentRequestResponse is received")
	public void aFailedPaymentRequestResponseIsReceived() throws InterruptedException {
	    EventResponse paymentRequestResponse = new EventResponse(sessionId, false, null);
	    Thread.sleep(100);
	    paymentService.handlePaymentResponse(new Event(PaymentService.PAYMENT_RESPONSE  + sessionId, paymentRequestResponse));
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
	
}
