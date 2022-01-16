package dtu.services;

import messaging.Event;
import messaging.implementations.MockMessageQueue;
import restService.Application.CustomerService;
import restService.Application.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ServiceStatusSteps {

  private MockMessageQueue messageQueue = new MockMessageQueue();
  private TokenService tokenService = new TokenService(messageQueue);
  private CustomerService customerService = new CustomerService(messageQueue);
  private CompletableFuture<String> statusMessage = new CompletableFuture<>();

  @When("the Token service is requested for its status")
  public void theTokenServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      String status = tokenService.getStatus();
      statusMessage.complete(status);
    }).start();
  }

  @When("the Customer service is requested for its status")
  public void theCustomerServiceIsRequestedForItsStatus() {
    new Thread(() -> {
      String status = customerService.getStatus();
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

  @When("the token service replies with the status message {string}")
  public void theTokenServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("TokenStatusResponse", new Object[] { statusMessage });
    tokenService.handleGetStatus(event);
  }

  @When("the Customer service replies with the status message {string}")
  public void theCustomerServiceRepliesWithTheStatusMessage(String statusMessage) {
    Event event = new Event("CustomerStatusResponse", new Object[] { statusMessage });
    customerService.handleGetStatus(event);
  }

  @Then("the status message is {string}")
  public void theStatusMessageIs(String statusMessage) {
    assertEquals(statusMessage, this.statusMessage.join());
  }

}
