package restService.Application;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


public class ReportService {
	
	public ReportService() {
	}
	
    private MessageQueue messageQueue;
    ServiceHelper serviceHelper = new ServiceHelper();

//    public static final String CUSTOMER_REPORT_REQUEST = "ReportCustomerRequest";
//    public static final String CUSTOMER_REPORT_RESPONSE = "HandleReportCustomerResponse"; // .SessionId
//    
//    public static final String MERCHANT_REPORT_REQUEST = "ReportMerchantRequest";
//    public static final String MERCHANT_REPORT_RESPONSE = "HandleReportMerchantResponse"; // .SessionId
//    
//    public static final String MANAGER_REPORT_REQUEST = "ReportManagerRequest";
//    public static final String MANAGER_REPORT_RESPONSE = "HandleReportManagerResponse"; // .SessionId
    
	public enum Role {
		CUSTOMER("ReportCustomerRequest", "HandleReportCustomerResponse"),
		MERCHANT("ReportMerchantRequest", "HandleReportMerchantResponse"),
		MANAGER ("ReportManagerRequest", "HandleReportManagerResponse");

		public final String REQUEST;
		public final String RESPONSE;

		private Role(String request, String respons) {
			this.REQUEST = request;
			this.RESPONSE = respons;
		}
	}
    
    private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

    public ReportService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getStatus(String sessionId) throws Exception {
        messageQueue.addHandler("ReportStatusResponse." + sessionId, this::handleResponse);
        sessions.put(sessionId, new CompletableFuture<Event>());
        messageQueue.publish(new Event("ReportStatusRequest", new EventResponse(sessionId, true, null)));
        
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
        throw new Exception(eventResponse.getErrorMessage());
    }
    
    public void handleResponse(Event event) {
        EventResponse eventResponse = event.getArgument(0, EventResponse.class);
        sessions.get(eventResponse.getSessionId()).complete(event);
    }
    
    
    public EventResponse getMerchantReport(String sessionId, String userId, Role role) throws InterruptedException, ExecutionException {
		sessions.put(sessionId, new CompletableFuture<Event>());

		messageQueue.addHandler(role.RESPONSE + "." + sessionId, this::reportResponseHandler);
		messageQueue.publish(new Event(role.REQUEST, new EventResponse(sessionId, true, null, new Object[] {userId} )));

		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "ERROR: Request timed out");
		
		sessions.get(sessionId).join();
		return sessions.get(sessionId).get().getArgument(0, EventResponse.class);
    }
    
	public void reportResponseHandler(Event event) {
		String sessionId = event.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(event);
	}

}
