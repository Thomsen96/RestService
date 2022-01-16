package dtu.services;

import messaging.Event;
import messaging.implementations.MockMessageQueue;
import restService.Application.AccountService;
import restService.Application.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ServiceStatusSteps {

  private MockMessageQueue messageQueue = new MockMessageQueue();
  private TokenService tokenService = new TokenService(messageQueue);
  private AccountService AccountService = new AccountService(messageQueue);
  private CompletableFuture<String> statusMessage = new CompletableFuture<>();

  @When("the Token service is requested for its status")
  public void theTokenServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      String status = tokenService.getStatus();
      statusMessage.complete(status);
    }).start();
  }

  @When("the Account service is requested for its status")
  public void theAccountServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      String status = AccountService.getStatus();
      statusMessage.complete(status);
    }).start();
  }

  @Then("the event {string} have been sent")
  public void theEventHaveBeenSent(String eventTopic) {
    Event event = new Event(eventTopic);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(event, messageQueue.getEvent(eventTopic));
  }

  @When("the Token service replies with the status message {string}")
  public void theTokenServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("TokenStatusResponse", new Object[] { statusMessage });
    tokenService.handleGetStatus(event);
  }

  @When("the Account service replies with the status message {string}")
  public void theAccountServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("AccountStatusResponse", new Object[] { statusMessage });
    AccountService.handleGetStatus(event);
  }

  @Then("the status message is {string}")
  public void theStatusMessageIs(String statusMessage) {
    assertEquals(statusMessage, this.statusMessage.join());
  }

}
