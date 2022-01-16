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

  public String getStatus() {
    messageQueue.addHandler("AccountStatusResponse", this::handleGetStatus);
    messageQueue.publish(new Event("AccountStatusRequest", new Object[] { "" }));
    return getStatus.join().getArgument(0, String.class);
  }

  public void handleGetStatus(Event event) {
    getStatus.complete(event);
  }

}
