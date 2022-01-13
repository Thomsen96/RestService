package dtu.services;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class ClientCreateTokenSteps {
 
	
	RestClient restClient = new RestClient();
 
	String endpoint;
	String response;
	
	@Given("the endpoint is {string}")
	public void theEndpointIs(String newEndpoint) {
		endpoint = newEndpoint;
	}

	@When("a REST GET is made")
	public void aRESTGETIsMade() {
		response = restClient.rest(endpoint);
	}

	@Then("the message {string} is received")
	public void theMessageIsReceived(String expectedResponse) {
	    assertEquals(expectedResponse, response);
	}
	
}