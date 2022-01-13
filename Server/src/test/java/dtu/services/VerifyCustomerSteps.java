package dtu.services;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import messaging.Event;
import messaging.MessageQueue;
import restService.Presentation.Resources.CustomerMessageService;

public class VerifyCustomerSteps {

//  String customerId = null;
//  String token = null;
//
//  private MessageQueue messageQueue = mock(MessageQueue.class);
//  private TokenService tokenService = new TokenService(new LocalTokenRepository());
//  private CustomerMessageService service = new CustomerMessageService(messageQueue, tokenService);

@Given("A customer with id {string}")
public void aCustomerWithId(String customerId) {
//	this.customerId = customerId;
//	token = tokenService.createTokens(1, customerId).get(0).getUuid();
}

  @When("a request to verify the token is received")
  public void aRequestToVerifyTheTokenIsReceived() {
//    service.handleTokenVerificationRequested(new Event("TokenVerificationRequested",new Object[] {this.token}));
  }

  @Then("the token is verified")
  public void theTokenIsVerified() {
//  Boolean bool = true;
//  Event event = new Event("TokenVerificationResponse", new Object[] { bool });
//	verify(messageQueue).publish(event);
  }

}