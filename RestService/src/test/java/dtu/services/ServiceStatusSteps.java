package dtu.services;

import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;
import restService.Application.AccountService;
import restService.Application.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ServiceStatusSteps {

  private MockMessageQueue messageQueue = new MockMessageQueue();
  private TokenService tokenService = new TokenService(messageQueue);
  private AccountService AccountService = new AccountService(messageQueue);
  private CompletableFuture<String> statusMessage = new CompletableFuture<>();
  String sessionId;

  @When("the Token service is requested for its status")
  public void theTokenServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      sessionId = UUID.randomUUID().toString();
      String status = tokenService.getStatus(sessionId);
      statusMessage.complete(status);
    }).start();
  }

  @When("the Account service is requested for its status")
  public void theAccountServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      sessionId = UUID.randomUUID().toString();
      String status = AccountService.getStatus(sessionId);
      statusMessage.complete(status);
    }).start();
  }

  @Then("the event {string} have been sent")
  public void theEventHaveBeenSent(String eventTopic) {

    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Event expectedEvent = new Event(eventTopic, sessionId );
    Event actualEvent = messageQueue.getEvent(eventTopic);
    
    assertEquals(expectedEvent, actualEvent );
  }

  @When("the Token service replies with the status message {string}")
  public void theTokenServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("TokenStatusResponse." + sessionId, new EventResponse(sessionId, true, null, statusMessage ));
    tokenService.handleResponse(event);
  }

  @When("the Account service replies with the status message {string}")
  public void theAccountServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("AccountStatusResponse." + sessionId, new EventResponse(sessionId, true, null, statusMessage ));
    AccountService.handleGetStatus(event);
  }

  @Then("the status message is {string}")
  public void theStatusMessageIs(String statusMessage) {
    assertEquals(statusMessage, this.statusMessage.join());
  }

}
