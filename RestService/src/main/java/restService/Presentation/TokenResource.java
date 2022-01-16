package restService.Presentation;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import restService.Application.TokenService;
import restService.Infrastructure.TokenMessageFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tokens")
public class TokenResource {
    // mvn compile quarkus:dev
    // Should offer the following:
    // register
    // deregister
    // getTokens
    // getReport
    private MessageQueue mq = new RabbitMqQueue("localhost");
    private TokenService tokenService = new TokenService(mq);
    //CustomerMessageService cms = new CustomerMessageService(mq);

    //TokenService tsr = new TokenService(mq);

    @POST
    @Path("{customerId}/{numberOfTokens}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokens(@PathParam("customerId") String customerId, @PathParam("numberOfTokens") Integer numberOfTokens) {
        return Response.status(Response.Status.CREATED).entity(String.format("Hello %s! Here are %2d tokens!", customerId, numberOfTokens)).build();
    }

    @GET
    @Path("/status")
    public Response get() {
        return Response.status(Response.Status.OK).entity(tokenService.getStatus()).build();
    }
}
