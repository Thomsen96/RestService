package dtu.services;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;
import restService.Application.TokenService;
import restService.Domain.Token;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class VerifyTokenSteps {

    public MockMessageQueue messageQueue = new MockMessageQueue();
    public TokenService tokenService = new TokenService(messageQueue);
    public CompletableFuture<Event> tokenResponse = new CompletableFuture<>();

    String customerId;
    int numberOfTokensRequested;
    String sessionId = UUID.randomUUID().toString();

    @Given("the customer with id {string} wants {int} tokens request tokens")
    public void theCustomerWithIdWantsTokensRequestTokens(String customerId, int numberOfTokensRequested) {
        this.customerId = customerId;
        this.numberOfTokensRequested = numberOfTokensRequested;
    }

    @When("the token service responds with a token")
    public void theTokenServiceRespondsWithAToken() {
        new Thread(() -> {
            Event e = tokenService.getTokensMessageService(sessionId, customerId, numberOfTokensRequested);
            tokenResponse.complete(e);
        }).start();
    }

    @Then("the token request is sent to the tokenService")
    public void theTokenRequestIsSentToTheTokenService() throws InterruptedException {
        Thread.sleep(100);

        Event event = new Event("TokenCreationRequest", new EventResponse(sessionId, true, null, customerId, numberOfTokensRequested));
        assertEquals(event, messageQueue.getEvent("TokenCreationRequest"));
    }


    @When("token service sends a response on the queue")
    public void tokenServiceSendsAResponseOnTheQueue() {
        tokenService.handleResponse(new Event("TokenCreationResponse." + sessionId, new EventResponse(sessionId, true, null, new Token( customerId, "TOKEN STRING", true))));
    }

    @Then("the service can return the token to the user")
    public void theServiceCanReturnTheTokenToTheUser() {
        Token t = new Token(customerId, "TOKEN STRING", true);
        assertEquals(t, tokenResponse.join().getArgument(0, EventResponse.class).getArgument(0, Token.class));
    }
}
