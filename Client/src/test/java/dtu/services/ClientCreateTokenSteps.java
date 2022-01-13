package dtu.services;
import dtu.services.Entities.Token;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class ClientCreateTokenSteps {
  String customerId = null;
  Response res;
  ArrayList<Token> tokenList = null;
  TokenClient tokenClient = new TokenClient();
  
  @After
  public void before_or_after_all() {
	try {
		tokenClient.deleteTokens(customerId);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  
  @Given("a customer with id {string}")
  public void aCustomerWithId(String customerId) {
	  this.customerId = customerId;
  }

  @When("the customer requests {int} tokens")
  public void theCustomerRequestsTokens(Integer numOfRequestedTokens) {
	  res = tokenClient.createTokens(numOfRequestedTokens, customerId);
	  tokenList = res.readEntity(new GenericType<ArrayList<Token>>() {});
  }

  @Then("the customer has {int} tokens")
  public void theCustomerHasTokens(Integer expectedNumOfTokens) {
	  assertEquals(expectedNumOfTokens, tokenList.size());
  }

  @Given("the customer already has {int} tokens")
  public void theCustomerAlreadyHasTokens(Integer numOfRequestedTokens) {
    theCustomerRequestsTokens(numOfRequestedTokens);
  }
}