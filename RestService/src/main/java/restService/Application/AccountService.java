package restService.Application;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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

	private ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

	public String getStatus(String sessionId) {
		messageQueue.addHandler("AccountStatusResponse." + sessionId, this::handleGetStatus);
		sessions.put(sessionId, new CompletableFuture<Event>());
		messageQueue.publish(new Event("AccountStatusRequest", new Object[] { sessionId }));
		(new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					sessions.get(sessionId).complete(new Event("", new EventResponse(sessionId, false, null, "No reply from a Account service")));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return sessions.get(sessionId).join().getArgument(0, EventResponse.class).getArgument(0, String.class);
	}

	public void handleGetStatus(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

	public AccountService(MessageQueue messageQueue) {
		AccountService.messageQueue = messageQueue;
	}

	public String createCustomerCreationRequest(String sessionId, String accountNumber, Role role) throws InterruptedException, ExecutionException {

		// Create a place for the response to reside
		sessions.put(sessionId, new CompletableFuture<Event>());

		// Create the event and send it
		Event event = new Event(role.CREATION_REQUEST, accountNumber, sessionId);

		messageQueue.addHandler(role.CREATION_RESPONSE + "."+  sessionId, this::customerCreationResponseHandler);
		messageQueue.publish(event);

		// Create a timeout
		(new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					sessions.get(sessionId).complete(new Event("", new EventResponse(sessionId, false, "No response in time")));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		if(sessions.get(sessionId).join().getArgument(0, EventResponse.class).isSuccess())
		{
			return sessions.get(sessionId).get().getArgument(0, EventResponse.class).getArgument(0, String.class);
		}
		return sessions.get(sessionId).get().getArgument(0, EventResponse.class).getErrorMessage();
	}

	public void customerCreationResponseHandler(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

}
