package dtu.services;

import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.MockMessageQueue;
import restService.Domain.Token;
import restService.Presentation.Resources.TokenResource;
import restService.Resources.TokenService;

import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class VerifyTokenSteps {

    public MockMessageQueue mq = new MockMessageQueue();
    public TokenService ts = new TokenService(mq);
    public Event responseEvent;
    public CompletableFuture<Event> compFut = new CompletableFuture<>();

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
            responseEvent = ts.getTokensMessageSerivce(id, requestToken);
            compFut.complete(responseEvent);
        }).start();
    }

    @Then("the token request is sent to the tokenService")
    public void theTokenRequestIsSentToTheTokenService() throws InterruptedException {
        String topic = "TokenCreationRequest";
        Thread.sleep(100);
        String sessionID = mq.getEvent(topic).getArgument(2, String.class);
        Event e = new Event(topic, new Object[]{id, requestToken, sessionID});
        assertEquals(e, mq.getEvent(topic));
    }


    @When("token service sends a response on the queue")
    public void tokenServiceSendsAResponseOnTheQueue() {
        // i need a sessionID to complete this test
        String sessionID = "";
        ts.handleGetTokens(new Event("TokenCreationResponse#" + sessionID, new Object[] {new Token("id", "tid", true)}));
    }

    @Then("the service can return the token to the user")
    public void theServiceCanReturnTheTokenToTheUser() {
        Token t = new Token("id", "tid", true);
        assertEquals(compFut.join().getArgument(0, Token.class), t);
    }
}
