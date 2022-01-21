package restService.Application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.HANDLE.*;
import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.PUBLISH.ACCOUNT_STATUS_RESPONDED;
import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.PUBLISH.MERCHANT_CREATION_RESPONDED;
import static messaging.GLOBAL_STRINGS.REST_SERVICE.HANDLE.CUSTOMER_CREATION_RESPONDED;

public class AccountService {

	public AccountService(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public static final String ACCOUNT_STATUS_REQUEST = ACCOUNT_STATUS_REQUESTED;
	public static final String ACCOUNT_STATUS_RESPONSE = ACCOUNT_STATUS_RESPONDED;
	
	public static final String CUSTOMER_CREATION_REQUEST = CUSTOMER_CREATION_REQUESTED;
	public static final String CUSTOMER_CREATION_RESPONSE = CUSTOMER_CREATION_RESPONDED;
	public static final String MERCHANT_CREATION_REQUEST = MERCHANT_CREATION_REQUESTED;
	public static final String MERCHANT_CREATION_RESPONSE = MERCHANT_CREATION_RESPONDED;
	
	public enum Role {
		CUSTOMER(CUSTOMER_CREATION_REQUEST, CUSTOMER_CREATION_RESPONSE),
		MERCHANT(MERCHANT_CREATION_REQUEST, MERCHANT_CREATION_RESPONSE);

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

  // TODO: Christian du får problemer med at vi har . i global strings her på getStatus
	public String getStatus(String sessionId) throws Exception {
		sessions.put(sessionId, new CompletableFuture<Event>());

		messageQueue.addHandler(ACCOUNT_STATUS_RESPONSE  + sessionId, this::handleGetStatus);
		messageQueue.publish(new Event(ACCOUNT_STATUS_REQUEST, new EventResponse(sessionId, true, null)));


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

		messageQueue.addHandler(role.CREATION_RESPONSE  + sessionId, this::accountCreationResponseHandler);
		messageQueue.publish(event);

		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "ERROR: Request timed out");
		
		return sessions.get(sessionId).join();
	}

	public void accountCreationResponseHandler(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

}
