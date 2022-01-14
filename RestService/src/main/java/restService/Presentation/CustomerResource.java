package restService.Presentation;

import messaging.MessageQueue;
import restService.Infrastructure.CustomerMessageFactory;
import restService.Infrastructure.CustomerMessageService;

import javax.ws.rs.*;

import java.util.concurrent.CompletableFuture;

@Path("/customers")
public class CustomerResource  {
	// mvn compile quarkus:dev
    // Should offer the following:
    // register
    // deregister
    // getTokens
    // getReport
	CustomerMessageService cms = new CustomerMessageFactory().getService();
	private CompletableFuture<String> sessionHandled;

	//GET TOKENS IS FOUND IN TOKENRESOURCE OR TOKENSERVICE

//	@POST
//	@Path("{customerId}/{numberOfTokens}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response create(@PathParam("customerId") String customerId, @PathParam("numberOfTokens") Integer numberOfTokens) {
//		return Response.status(Response.Status.CREATED).entity(service.createTokens(numberOfTokens,customerId)).build();
//	}
//
//	@GET
//	@Path("{customerId}")
//	public Response get(@PathParam("customerId") String customerId) {
//		return Response.status(Response.Status.OK).entity(service.getTokens(customerId)).build();
//	}
//
//	@DELETE
//	@Path("{customerId}")
//	public Response delete(@PathParam("customerId") String customerId) {
//		return Response.status(Response.Status.OK).entity(service.deleteTokensForCustomer(customerId)).build();
//	}

	

}
