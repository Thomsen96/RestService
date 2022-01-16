package restService.Application;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class CustomerService {

  private MessageQueue messageQueue;

  private CompletableFuture<Event> getStatus = new CompletableFuture<>();

  public CustomerService(MessageQueue messageQueue) {
    this.messageQueue = messageQueue;
  }

  public String getStatus() {
    messageQueue.addHandler("CustomerStatusResponse", this::handleGetStatus);
    messageQueue.publish(new Event("CustomerStatusRequest", new Object[] { "" }));
    return getStatus.join().getArgument(0, String.class);
  }

  public void handleGetStatus(Event event) {
    getStatus.complete(event);
  }

}
