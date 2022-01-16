package restService.Application;

import messaging.Event;
import messaging.MessageQueue;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenService {
    private MessageQueue messageQueue;
    private CompletableFuture<Event> getStatus = new CompletableFuture<>();
    private CompletableFuture<Event> sessionHandled = new CompletableFuture<>();

    public TokenService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getStatus() {
        messageQueue.addHandler("TokenStatusResponse", this::handleGetStatus);
        messageQueue.publish(new Event("TokenStatusRequest", new Object[] { "" }));
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    getStatus.complete(new Event("", new Object[] { "Token no reply from a Token service" }));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return getStatus.join().getArgument(0, String.class);
    }

    public void handleGetStatus(Event event) {
        getStatus.complete(event);
    }

    public void handleGetTokens(Event event) {
        // String sessionId = event.getArgument(1, String.class);
        // HashSet returnVal = event.getArgument(0, HashSet.class);
        sessionHandled.complete(event);
    }

    public Event getTokensMessageSerivce(String customerId, int numOfTokens) {
        String sessionID = UUID.randomUUID().toString();
        String topic = "TokenCreationRequest";
        sessionHandled = new CompletableFuture<>();
        Event e = new Event(topic, new Object[] { customerId, numOfTokens, sessionID });
        messageQueue.addHandler("TokenCreationResponse" + "#" + sessionID, this::handleGetTokens);
        messageQueue.publish(e);
        return sessionHandled.join();
    }

    public Event getTokensMessageSerivce(String customerId, int numOfTokens, String uid) {
        String sessionID = uid;
        String topic = "TokenCreationRequest";
        sessionHandled = new CompletableFuture<>();
        Event e = new Event(topic, new Object[] { customerId, numOfTokens, sessionID });
        messageQueue.addHandler("TokenCreationResponse" + "#" + sessionID, this::handleGetTokens);
        messageQueue.publish(e);
        return sessionHandled.join();
    }
}
