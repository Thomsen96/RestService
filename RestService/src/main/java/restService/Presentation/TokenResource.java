package restService.Presentation;

import restService.Application.TokenService;
import restService.Infrastructure.MessageQueueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tokens")
public class TokenResource {

    private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());

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
