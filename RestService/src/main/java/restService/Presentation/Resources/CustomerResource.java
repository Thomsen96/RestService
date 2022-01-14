package restService.Presentation.Resources;

import messaging.Event;
import messaging.MessageQueue;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Path("/customers")
public class CustomerResource  {
	// mvn compile quarkus:dev
    // Should offer the following:
    // register
    // deregister
    // getTokens
    // getReport
	private MessageQueue mq;
	CustomerMessageService cms = new CustomerMessageService(mq);
	private CompletableFuture<String> sessionHandled = new CompletableFuture<>();
//	private static TokenService service = new TokenService(new LocalTokenRepository());

	private String addFuture(String sessionId){
		new Thread(() -> {
			this.sessionHandled.complete(sessionId);
		}).start();
		return sessionHandled.join();
	}
	
	@GET
	@Path("{customerId}/{numberOfTokens}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTokens(@PathParam("customerId") String customerId, @PathParam("numberOfTokens") Integer numberOfTokens) {
		String sessionID = UUID.randomUUID().toString();
		Event e = new Event("CustomerVerified", new Object[]{customerId, sessionID});
		addFuture(sessionID);
		mq.publish(e);
		return Response.status(Response.Status.CREATED)
				.entity(String.format("Hello %s! Here are %2d tokens!", customerId, numberOfTokens))
				.build();
		//return Response.status(Response.Status.CREATED).entity(String.format("Hello %s! Here are %2d tokens!", customerId, numberOfTokens)).build();
	}



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
