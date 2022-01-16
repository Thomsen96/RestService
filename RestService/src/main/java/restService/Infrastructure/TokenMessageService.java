package restService.Infrastructure;


import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class TokenMessageService {
	private MessageQueue messageQueue;

	public TokenMessageService(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
		//this.messageQueue.addHandler("TokenVerificationRequested", this::handleTokenVerificationRequested);
		//this.messageQueue.addHandler("CustomerVerified", this::handleCustomerVerification);
	}

	// Send request?
	public Boolean verifyCustomer(String customerId) {
		// customerVerified = new CompletableFuture<>();
		// Event event = new Event("CustomerVerificationRequested", new Object[] {
		// customerId });
		// messageQueue.publish(event);
		// return customerVerified.join();
		return false;
	}

	public void handleCustomerVerification(Event e) {
		// var s = e.getArgument(0, Boolean.class);
		// customerVerified.complete(s);
	}

	// Handle incoming requests?
	public void handleTokenVerificationRequested(Event e) {
		// var token = e.getArgument(0, String.class);
		// Boolean tokenValid = tokenService.verifyToken(token);
		// Event event = new Event("TokenVerificationResponse", new Object[] {
		// tokenValid });
		// messageQueue.publish(event);
	}

}
