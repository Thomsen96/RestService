package dtu.services;

import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import restService.Domain.Token;
import restService.Presentation.Resources.TokenResource;
import restService.Resources.TokenService;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class VerifyTokenSteps {

    public MessageQueue mq;
    public TokenResource tr = new TokenResource();
    public TokenService ts = new TokenService(mq);

    String id;
    int requestToken;

    @Given("the customer with id {string} wants {int} tokens request tokens")
    public void theCustomerWithIdWantsTokensRequestTokens(String arg0, int arg1) {
        this.id = arg0;
        this.requestToken = arg1;
    }

    @When("the token service responds with a token")
    public void theTokenServiceRespondsWithAToken() {
        tr.getTokens(id, requestToken);
    }

    @Then("the token request is sent to the tokenService")
    public void theTokenRequestIsSentToTheTokenService() {
        String topic = "TokenCreationRequest";
        Event e = new Event(topic, new Object[]{id, requestToken, ""});
//        assertEquals(e, mq.getEvent());
    }




}
