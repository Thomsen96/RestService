package restService.Resources;

import messaging.Event;
import messaging.MessageQueue;
import restService.Domain.Token;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenService {
    private MessageQueue mq;
    public CompletableFuture<Event> sessionHandled;
    public TokenService(MessageQueue mq){
        this.mq = mq;
    }

    public void handleGetTokens(Event event) {
//        String sessionId = event.getArgument(1, String.class);
//        HashSet returnVal = event.getArgument(0, HashSet.class);
        sessionHandled.complete(event);
    }

    public Event getTokensMessageSerivce(String customerId, int numOfTokens){
        String sessionID = UUID.randomUUID().toString();
        String topic = "TokenCreationRequest";
        sessionHandled = new CompletableFuture<>();
        Event e = new Event(topic, new Object[]{customerId, numOfTokens, sessionID});
        mq.addHandler("TokenCreationResponse" + "#" + sessionID, this::handleGetTokens);
        mq.publish(e);
        return sessionHandled.join();
    }

    public Event getTokensMessageSerivce(String customerId, int numOfTokens, String uid){
        String sessionID = uid;
        String topic = "TokenCreationRequest";
        sessionHandled = new CompletableFuture<>();
        Event e = new Event(topic, new Object[]{customerId, numOfTokens, sessionID});
        mq.addHandler("TokenCreationResponse" + "#" + sessionID, this::handleGetTokens);
        mq.publish(e);
        return sessionHandled.join();
    }
}
