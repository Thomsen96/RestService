package restService.Application;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class AccountService {

  private static MessageQueue messageQueue;

  private CompletableFuture<Event> getStatus = new CompletableFuture<>();

  
  public String getStatus(String sessionId) {
    messageQueue.addHandler("AccountStatusResponse." + sessionId, this::handleGetStatus);
    messageQueue.publish(new Event("AccountStatusRequest", new Object[] { sessionId }));
    (new Thread() {
      public void run() {
        try {
          Thread.sleep(5000);
          getStatus.complete(new Event("", new Object[] { "No reply from a Account service" }));
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
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  public static final String CUSTOMER_CREATION_REQUEST = "CustomerCreationRequest";
  public static final String CUSTOMER_CREATION_RESPONSE = "CustomerCreationResponse";
	
  
  public static HashMap<String, CompletableFuture<String>> customerCreationPending;
  
  public AccountService(MessageQueue messageQueue) {
	  AccountService.messageQueue = messageQueue;
  }
  
  
  public String createCustomerCreationRequest(String sessionId, String accountNumber) {

  	// Create a place for the response to reside
  	customerCreationPending.put(sessionId, new CompletableFuture<String>());
  	
  	// Create the event and send it
  	Event event = new Event(CUSTOMER_CREATION_REQUEST, new Object[] { accountNumber, sessionId } );
  	messageQueue.publish(event);
      
  	// Create a handler for the response
  	messageQueue.addHandler(CUSTOMER_CREATION_RESPONSE.concat(".").concat(sessionId), e -> {
  		customerCreationResponseHandler(sessionId, event);
  	});
  	
  	// Create a timeout
      (new Thread() {
          public void run() {
              try {
                  Thread.sleep(5000);
                  customerCreationPending.get(sessionId).complete("Timed out");
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }).start();
      
  	return customerCreationPending.get(sessionId).join();
  }


	public void customerCreationResponseHandler(String sessionId, Event event) {
		// Extract the customerId from response
		String customerId = event.getArgument(0, String.class);
		customerCreationPending.get(sessionId).complete(customerId);
	}
  

}
