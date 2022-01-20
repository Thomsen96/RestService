package restService.Application;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {
	
	public TokenService(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}
		
	public static final String TOKEN_STATUS_REQUEST = "TokenStatusRequest";
	public static final String TOKEN_STATUS_RESPONSE = "TokenStatusResponse";

    public static final String TOKEN_CREATION_REQUEST = "TokenCreationRequest";
    public static final String TOKEN_CREATION_RESPONSE = "TokenCreationResponse";
    
	
    private MessageQueue messageQueue;
    ServiceHelper serviceHelper = new ServiceHelper();
    
    private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();
    
    // TODO: Christian du får problemer med at vi har . i global strings her på getStatus
    public String getStatus(String sessionId) throws Exception {
    	sessions.put(sessionId, new CompletableFuture<Event>());

    	messageQueue.addHandler(TOKEN_STATUS_RESPONSE + "." + sessionId, this::handleResponse);
        messageQueue.publish(new Event(TOKEN_STATUS_REQUEST, new EventResponse(sessionId, true, null)));

		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No reply from a Token service");
		
		Event event = sessions.get(sessionId).join();
		EventResponse eventResponse = event.getArgument(0, EventResponse.class);

        if (eventResponse.isSuccess()) {
            return eventResponse.getArgument(0, String.class);
        }
        throw new Exception(eventResponse.getErrorMessage());
		
    }

    public Event getTokensMessageService(String sessionId, String customerId, int numOfTokens) {

    	sessions.put(sessionId, new CompletableFuture<Event>());
        
		EventResponse eventArgs = new EventResponse(sessionId, true, null, customerId, numOfTokens);
		Event event = new Event(TOKEN_CREATION_REQUEST, eventArgs);
		messageQueue.addHandler(TOKEN_CREATION_RESPONSE + "." + sessionId, this::handleResponse);
        messageQueue.publish(event);

        // TODO: Add timeout handling

        return sessions.get(sessionId).join();
    }

    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }


}
