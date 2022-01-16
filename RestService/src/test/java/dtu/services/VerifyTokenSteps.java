package dtu.services;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.implementations.MockMessageQueue;
import restService.Application.TokenService;
import restService.Domain.Token;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class VerifyTokenSteps {

    public MockMessageQueue messageQueue = new MockMessageQueue();
    public TokenService tokenService = new TokenService(messageQueue);
    public CompletableFuture<Event> tokenResponse = new CompletableFuture<>();

    String id;
    int requestToken;

    @Given("the customer with id {string} wants {int} tokens request tokens")
    public void theCustomerWithIdWantsTokensRequestTokens(String id, int reqTokens) {
        this.id = id;
        this.requestToken = reqTokens;
    }

    @When("the token service responds with a token")
    public void theTokenServiceRespondsWithAToken() {
        new Thread(() -> {
            Event e = tokenService.getTokensMessageSerivce(id, requestToken);
            tokenResponse.complete(e);
        }).start();
    }

    @Then("the token request is sent to the tokenService")
    public void theTokenRequestIsSentToTheTokenService() throws InterruptedException {
        String topic = "TokenCreationRequest";
        Thread.sleep(100);
        String sessionID = messageQueue.getEvent(topic).getArgument(2, String.class);
        Event e = new Event(topic, new Object[]{id, requestToken, sessionID});
        assertEquals(e, messageQueue.getEvent(topic));
    }


    @When("token service sends a response on the queue")
    public void tokenServiceSendsAResponseOnTheQueue() {
        // i need a sessionID to complete this test
        String sessionID = "";
        tokenService.handleGetTokens(new Event("TokenCreationResponse#" + sessionID, new Object[] {new Token("id", "tid", true)}));
    }

    @Then("the service can return the token to the user")
    public void theServiceCanReturnTheTokenToTheUser() {
        Token t = new Token("id", "tid", true);
        assertEquals(tokenResponse.join().getArgument(0, Token.class), t);
    }
}
