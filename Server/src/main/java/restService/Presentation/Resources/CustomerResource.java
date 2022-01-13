package restService.Presentation.Resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource  {

	// mvn compile quarkus:dev
	
    // Should offer the following:
	//	/customer
    // 		/register
    // 		/deregister
    // 		/getTokens
    // 		/getReport
	
//	private static TokenService service = new TokenService(new LocalTokenRepository());

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello from customer root";
	}
	
	@GET
	@Path("{customerId}/{numberOfTokens}")
	@Produces(MediaType.APPLICATION_JSON)    
	public Response getTokens(@PathParam("customerId") String customerId, @PathParam("numberOfTokens") Integer numberOfTokens) {
//		return Response.status(Response.Status.CREATED).entity(service.createTokens(numberOfTokens,customerId)).build();
		return Response.status(Response.Status.CREATED)
				.entity(String.format("Hello %s! Here are %2d tokens!", customerId, numberOfTokens))
				.build();
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
