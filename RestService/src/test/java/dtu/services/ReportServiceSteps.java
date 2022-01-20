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
import restService.Application.ReportService;
import restService.Application.ReportService.Role;
import restService.Application.ServiceHelper;

public class ReportServiceSteps {

	static MessageQueue messageQueue = mock(MessageQueue.class);
	
	ServiceHelper serviceHelper = new ServiceHelper();
	ReportService reportService = new ReportService(messageQueue);

	
	EventResponse eventResponse;
	CompletableFuture<Boolean> requestCompleted = new CompletableFuture<>();
	
	String userID;
	String sessionId;
	Role role;
	
	int default_timeout;

	
	@Before()
	public void saveTimeout() { this.default_timeout = serviceHelper.TIMEOUT; }
	@After()
	public void restoreTimeout() { serviceHelper.TIMEOUT = this.default_timeout; }

	
	@Given("a merchant")
	public void aPayment() {
		this.userID = UUID.randomUUID().toString();
	    this.sessionId = UUID.randomUUID().toString();
	    this.role = Role.MERCHANT;
	}
	
	
	@When("a getReport request message is sent")
	public void thePaymentRequestMessageIsSent() {
		var thread = new Thread(() -> {
			try {
				eventResponse = reportService.getReport(sessionId, userID, role);
				requestCompleted.complete(true);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}
	
	@When("the getReport response message is received")
	public void thePaymentRequestResponseIsReceived() {
	    EventResponse paymentRequestResponse = new EventResponse(sessionId, true, null);
	    reportService.reportResponseHandler(new Event(role.RESPONSE + "." + sessionId, paymentRequestResponse));
	}
		
	@Then("a successful EventResponse is received")
	public void theReceivedEventResponseIsASuccess() throws InterruptedException {
		requestCompleted.join();
		assertTrue(this.eventResponse.isSuccess());
	}
	
}
