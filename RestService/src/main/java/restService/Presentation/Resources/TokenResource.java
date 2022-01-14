package restService.Presentation.Resources;

import messaging.MessageQueue;
import restService.Resources.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@Path("/tokens")
public class TokenResource {
    // mvn compile quarkus:dev
    // Should offer the following:
    // register
    // deregister
    // getTokens
    // getReport
    public MessageQueue mq;
    CustomerMessageService cms = new CustomerMessageService(mq);
    private CompletableFuture<String> sessionHandled;

    TokenService tsr = new TokenService(mq);

    @POST
    @Path("{customerId}/{numberOfTokens}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokens(@PathParam("customerId") String customerId, @PathParam("numberOfTokens") Integer numberOfTokens) {
        var token = tsr.getTokensMessageSerivce(customerId, numberOfTokens);
        return Response.status(Response.Status.CREATED).entity(String.format("Hello %s! Here are %2d tokens!", customerId, numberOfTokens)).build();
    }
}
