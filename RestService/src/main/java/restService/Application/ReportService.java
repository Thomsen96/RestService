package restService.Application;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


public class ReportService {
	
	public static final String REPORT_STATUS_REQUEST = "ReportStatusRequest";
	public static final String REPORT_STATUS_RESPONSE = "ReportStatusResponse";
	
	public static final String CUSTOMER_REPORT_REQUEST = "ReportCustomerRequest";
	public static final String CUSTOMER_REPORT_RESPONSE = "HandleReportCustomerResponse";
	
	public static final String MERCHANT_REPORT_REQUEST = "ReportMerchantRequest";
	public static final String MERCHANT_REPORT_RESPONSE = "HandleReportMerchantResponse";
	
	public static final String MANAGER_REPORT_REQUEST = "ReportManagerRequest";
	public static final String MANAGER_REPORT_RESPONSE = "HandleReportManagerResponse";
	
	
	public ReportService(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}
	
    private MessageQueue messageQueue;
    ServiceHelper serviceHelper = new ServiceHelper();
    
	public enum Role {
		CUSTOMER(CUSTOMER_REPORT_REQUEST, CUSTOMER_REPORT_RESPONSE),
		MERCHANT(MERCHANT_REPORT_REQUEST, MERCHANT_REPORT_RESPONSE),
		MANAGER (MANAGER_REPORT_REQUEST, MANAGER_REPORT_RESPONSE);

		public final String REQUEST;
		public final String RESPONSE;

		private Role(String request, String respons) {
			this.REQUEST = request;
			this.RESPONSE = respons;
		}
	}
    
    private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();


    public String getStatus(String sessionId) throws Exception {
    	sessions.put(sessionId, new CompletableFuture<Event>());

    	messageQueue.addHandler(REPORT_STATUS_RESPONSE + "." + sessionId, this::handleResponse);
        messageQueue.publish(new Event(REPORT_STATUS_REQUEST, new EventResponse(sessionId, true, null)));
        
        serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No reply from a Report service");
        
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
    
    
    public EventResponse getReport(String sessionId, String userId, Role role) throws InterruptedException, ExecutionException {
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
