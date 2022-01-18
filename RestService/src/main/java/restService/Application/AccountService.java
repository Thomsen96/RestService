package restService.Application;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountService {

	public static final String PAYMENT_REQUEST = "PaymentRequest";

	public enum Role {
		CUSTOMER("CustomerCreationRequest", "CustomerCreationResponse"),
		MERCHANT("MerchantCreationRequest", "MerchantCreationResponse");

		public final String CREATION_REQUEST;
		public final String CREATION_RESPONSE;

		private Role(String request, String respons) {
			this.CREATION_REQUEST = request;
			this.CREATION_RESPONSE = respons;
		}

	}

	private static MessageQueue messageQueue;

	private CompletableFuture<Event> getStatus = new CompletableFuture<>();

	public String getStatus(String sessionId) {
		messageQueue.addHandler("AccountStatusResponse." + sessionId, this::handleGetStatus);
		messageQueue.publish(new Event("AccountStatusRequest", new Object[] { sessionId }));
		(new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					getStatus.complete(new Event("", new Object[] { "No reply from a Account service" }));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return getStatus.join().getArgument(0, EventResponse.class).getArgument(0, String.class);
	}

	public void handleGetStatus(Event event) {
		getStatus.complete(event);
	}

	public static HashMap<String, CompletableFuture<String>> customerCreationPending = new HashMap<String, CompletableFuture<String>>();

	public AccountService(MessageQueue messageQueue) {
		AccountService.messageQueue = messageQueue;
	}

	public String createCustomerCreationRequest(String sessionId, String accountNumber, Role role) {

		// Create a place for the response to reside
		customerCreationPending.put(sessionId, new CompletableFuture<String>());

		// Create the event and send it
		Event event = new Event(
				role.CREATION_REQUEST,
				new Object[] { accountNumber, sessionId });

		messageQueue.publish(event);

		// Create a handler for the response
		messageQueue.addHandler(role.CREATION_RESPONSE.concat(".").concat(sessionId), e -> {
			customerCreationResponseHandler(sessionId, event);
		});

		// Create a timeout
		(new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					customerCreationPending.get(sessionId).complete(null);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return customerCreationPending.get(sessionId).join();
	}

	public void customerCreationResponseHandler(String sessionId, Event event) {
		String customerId = event.getArgument(0, String.class);
		customerCreationPending.get(sessionId).complete(customerId);
	}

}
