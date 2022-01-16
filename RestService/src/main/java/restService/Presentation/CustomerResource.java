package restService.Presentation;

import restService.Application.CustomerService;
import restService.Infrastructure.MessageQueueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


@Path("/customers")
public class CustomerResource  {

	CustomerService customerService = new CustomerService(new MessageQueueFactory().getMessageQueue());

    @GET
    @Path("/status")
    public Response get() {
        return Response.status(Response.Status.OK).entity(customerService.getStatus()).build();
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
