package restService.Application;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.handler.timeout.TimeoutException;
import io.vertx.ext.web.Session;

public class TokenService {
    private MessageQueue messageQueue;

    private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

    public TokenService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getStatus(String sessionId) throws Exception {
        messageQueue.addHandler("TokenStatusResponse." + sessionId, this::handleResponse);
        sessions.put(sessionId, new CompletableFuture<Event>());
        messageQueue.publish(new Event("TokenStatusRequest", new EventResponse(sessionId, true, null)));

        new Thread(() -> {
        	try {
        		Thread.sleep(5000);
        		EventResponse eventResponse = new EventResponse(sessionId, false, "No reply from a Token service");
        		sessions.get(sessionId).complete(new Event("", eventResponse));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
			
		}).start();

        EventResponse eventResponse = sessions.get(sessionId).join().getArgument(0, EventResponse.class);

        if (eventResponse.isSuccess()) {
            return eventResponse.getArgument(0, String.class);
        }
        throw new Exception(eventResponse.getErrorMessage());
    }

    public Event getTokensMessageService(String sessionId, String customerId, int numOfTokens) {
        sessions.put(sessionId, new CompletableFuture<Event>());
		EventResponse eventArgs = new EventResponse(sessionId, true, null, customerId, numOfTokens);
		Event event = new Event("TokenCreationRequest", eventArgs);
        messageQueue.addHandler("TokenCreationResponse." + sessionId, this::handleResponse);
        messageQueue.publish(event);

        // TODO: Add timeout handling

        return sessions.get(sessionId).join();
    }

    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }
}
