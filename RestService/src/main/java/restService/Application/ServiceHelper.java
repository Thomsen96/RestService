package restService.Application;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventResponse;

public class ServiceHelper {

	public static int TIMEOUT;
	
	public ServiceHelper() {
		this.TIMEOUT = 5000;
	}

	public void addTimeOut(String sessionId, CompletableFuture<Event> session, String errorMessage) {
        new Thread(() -> {
        	try {
        		Thread.sleep(TIMEOUT);
        		session.complete(new Event("",
        				new EventResponse(sessionId, false, errorMessage)));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
			
		}).start();
	}

	public void addTimeOut2(String sessionId, CompletableFuture<Event> session, String errorMessage) {
        new Thread(() -> {
        	try {
        		Thread.sleep(TIMEOUT);
        		session.complete(new Event("",
        				new EventResponse(sessionId, false, null, errorMessage)));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
		}).start();
	}

}
