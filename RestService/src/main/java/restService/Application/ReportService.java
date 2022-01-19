package restService.Application;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


public class ReportService {
    private MessageQueue messageQueue;

    private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

    public ReportService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getStatus(String sessionId) {
        messageQueue.addHandler("ReportStatusResponse." + sessionId, this::handleResponse);
        sessions.put(sessionId, new CompletableFuture<Event>());
        messageQueue.publish(new Event("ReportStatusRequest", new Object[] { sessionId }));
        
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    EventResponse eventResponse = new EventResponse(sessionId, false, "No reply from a Report service");
                    sessions.get(sessionId).complete(new Event("", eventResponse));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        EventResponse eventResponse = sessions.get(sessionId).join().getArgument(0, EventResponse.class);

        if (eventResponse.isSuccess()) {
            return eventResponse.getArgument(0, String.class);
        }
        return eventResponse.getErrorMessage();
    }
    
    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }
}
