package restService.Application;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class AccountService {

  private MessageQueue messageQueue;

  private CompletableFuture<Event> getStatus = new CompletableFuture<>();

  public AccountService(MessageQueue messageQueue) {
    this.messageQueue = messageQueue;
  }

  public String getStatus(String sessionId) {
    messageQueue.addHandler("AccountStatusResponse", this::handleGetStatus);
    messageQueue.publish(new Event("AccountStatusRequest." + sessionId, new Object[] { sessionId }));
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

}
