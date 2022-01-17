package restService.Presentation;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import messaging.MessageQueue;
import messaging.Event;

public class CustomerEventHandler {

    private MessageQueue messageQueue;

    private HashMap<String, CompletableFuture<Event>> customerCreationPending;
    
    public CustomerEventHandler(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;

        this.messageQueue.addHandler("CustomerCreationRequest", this::createCustomerCreationResponse);
    }
    
    private String generateSessionId() {
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
    
    public String createCustomerCreationRequest(String accountNumber) {
    	String sessionId = generateSessionId();
    	Event event = new Event("CustomerCreationRequest", new Object[] { accountNumber, sessionId } );
    	messageQueue.publish(event);
    	return sessionId;
    }
    
    public void createCustomerCreationResponse(Event event) {
    	String accountNumber = event.getArgument(0, String.class);
        Event response = new Event("MerchantVerificationRequest", new Object[] {  } );
        messageQueue.publish(response);
    }
    
}
