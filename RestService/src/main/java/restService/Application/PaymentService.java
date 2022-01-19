package restService.Application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;
import restService.Domain.PaymentDTO;

public class PaymentService {

	public PaymentService(MessageQueue messageQueue) {
		PaymentService.messageQueue = messageQueue;
	}
	
	public static final String PAYMENT_REQUEST = "PaymentRequest";
	
	private static MessageQueue messageQueue;
	ServiceHelper serviceHelper = new ServiceHelper();
	
    ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();
    
    
    public EventResponse createPayment(String sessionId, PaymentDTO dto) throws InterruptedException, ExecutionException {
        sessions.put(sessionId, new CompletableFuture<>());
        
        messageQueue.addHandler("PaymentResponse" + "." + sessionId, this::handlePaymentResponse);
        messageQueue.publish(new Event("PaymentRequest", new Object[] {new EventResponse(sessionId, true, null, dto)}));
		
        ServiceHelper serviceHelper = new ServiceHelper();
        serviceHelper.addTimeOut2(sessionId, sessions.get(sessionId), "Payment timed out");
        
        sessions.get(sessionId).join();
        
        return sessions.get(sessionId).get().getArgument(0, EventResponse.class);
    }
    
    public void handlePaymentResponse(Event event) {
        var sid = event.getType().split("\\.")[1];
        var future = sessions.get(sid);
        future.complete(event);
    }

    public String getStatus(String sessionId) {
        messageQueue.addHandler("PaymentStatusResponse." + sessionId, this::handleResponse);
        sessions.put(sessionId, new CompletableFuture<Event>());
        messageQueue.publish(new Event("PaymentStatusRequest", new Object[] { sessionId }));
        
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    EventResponse eventResponse = new EventResponse(sessionId, false, "No reply from a Token service");
                    sessions.get(sessionId).complete(new Event("", eventResponse));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        EventResponse eventResponse = sessions.get(sessionId).join().getArgument(0, EventResponse.class);
        return eventResponse.getArgument(0, String.class);
    }    
    
    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }
    
	
}
