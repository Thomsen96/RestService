package restService.Application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;
import restService.Domain.DTO;
import restService.Domain.PaymentDTO;

public class PaymentService {

    public PaymentService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }
    
	public static final String PAYMENT_STATUS_REQUEST = "PaymentStatusRequest";
	public static final String PAYMENT_STATUS_RESPONSE = "PaymentStatusResponse";

    public static final String PAYMENT_REQUEST = "PaymentRequest";
    public static final String PAYMENT_RESPONSE = "PaymentResponse";


    ServiceHelper serviceHelper = new ServiceHelper();
    private MessageQueue messageQueue;

    ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

    public EventResponse createPaymentRequest(String sessionId, DTO.CreatePayment dto) throws InterruptedException, ExecutionException {
        sessions.put(sessionId, new CompletableFuture<>());
    	String token = dto.token;
    	String merchant = dto.merchant;
    	String amount = dto.amount;
    	String description = dto.description;

        messageQueue.addHandler(PAYMENT_RESPONSE + "." + sessionId, this::handlePaymentResponse);
        messageQueue.publish(new Event(PAYMENT_REQUEST, new EventResponse(sessionId, true, null, token, merchant, amount, description)));
        
        serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "Payment timed out");

        Event event = sessions.get(sessionId).join();
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        
    	return eventResponse;
    }

    public void handlePaymentResponse(Event event) {
    	String topic = event.getType().toString();
    	String sessionId = topic.split("[.]",0)[1];
        sessions.get(sessionId).complete(event);
    }
  

    // TODO: Christian du får problemer med at vi har . i global strings her på getStatus
    public String getStatus(String sessionId) throws Exception {
    	sessions.put(sessionId, new CompletableFuture<Event>());
    	
    	messageQueue.addHandler(PAYMENT_STATUS_RESPONSE + "." + sessionId, this::handleResponse);
        messageQueue.publish(new Event(PAYMENT_STATUS_REQUEST, new EventResponse(sessionId, true, null)));
        
        serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No reply from a Payment service");
        
        Event event = sessions.get(sessionId).join();
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);

        if (eventResponse.isSuccess()) {
            return eventResponse.getArgument(0, String.class);
        }
        throw new Exception(eventResponse.getErrorMessage());
    }    
    
    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }
    
	
}
