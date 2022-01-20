package restService.Application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountService {

	public AccountService(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

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

	private MessageQueue messageQueue;
	private ServiceHelper serviceHelper = new ServiceHelper();

	private ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

	public String getStatus(String sessionId) throws Exception {
		sessions.put(sessionId, new CompletableFuture<Event>());

		messageQueue.addHandler("AccountStatusResponse." + sessionId, this::handleGetStatus);
		messageQueue.publish(new Event("AccountStatusRequest", new EventResponse(sessionId, true, null)));


		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No reply from a Account service");

		Event event = sessions.get(sessionId).join();
		EventResponse eventResponse = event.getArgument(0, EventResponse.class);

		if (eventResponse.isSuccess()) {
			return eventResponse.getArgument(0, String.class);
		}
		
		throw new Exception(eventResponse.getErrorMessage());
	}

	public void handleGetStatus(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

	public Event accountCreationRequest(String sessionId, String accountNumber, Role role) throws InterruptedException, ExecutionException {

		sessions.put(sessionId, new CompletableFuture<Event>());
		
		EventResponse eventArgs = new EventResponse(sessionId, true, null, accountNumber);
		Event event = new Event(role.CREATION_REQUEST, eventArgs);

		messageQueue.addHandler(role.CREATION_RESPONSE + "." + sessionId, this::accountCreationResponseHandler);
		messageQueue.publish(event);

		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "ERROR: Request timed out");
		
		return sessions.get(sessionId).join();
	}

	public void accountCreationResponseHandler(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

}
