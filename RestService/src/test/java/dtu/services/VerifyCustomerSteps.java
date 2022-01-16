package dtu.services;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

//import messaging.implementations.MockMessageQueue;
//import restService.Application.CustomerService;

public class VerifyCustomerSteps {

	//private MockMessageQueue messageQueue = new MockMessageQueue();
	//private CustomerService service = new CustomerService(messageQueue);
	
	@Given("the endpoint is {string}")
	public void theEndpointIs(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("a REST GET i posted")
	public void aRESTGETIPosted() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the message {string} is received")
	public void theMessageIsReceived(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

    // String customerId = null;
	// String token = null;

	// @Given("A customer with id {string}")
	// public void aCustomerWithId(String customerId) {
	// 	this.customerId = customerId;
	// 	token = tokenService.createTokens(1, customerId).get(0).getUuid();
	// }

	// @When("a request to verify the token is received")
	// public void aRequestToVerifyTheTokenIsReceived() {
	// 	service.handleTokenVerificationRequested(new
	// 	Event("TokenVerificationRequested",new Object[] {this.token}));
	// }

	// @Then("the token is verified")
	// public void theTokenIsVerified() {
	// 	Boolean bool = true;
	// 	Event event = new Event("TokenVerificationResponse", new Object[] { bool });
	// 	verify(messageQueue).publish(event);
	// }

}