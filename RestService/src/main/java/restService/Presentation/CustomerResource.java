package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Application.TokenService;
import restService.Domain.Token;
import restService.Infrastructure.MessageQueueFactory;

import java.util.HashSet;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;


@Path("/customers")
public class CustomerResource  {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
    private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());
	
    @GET
    @Path("/status")
    public Response get() {
			return Response.status(Response.Status.OK).entity(accountService.getStatus(UUID.randomUUID().toString())).build();
    }
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(String accountNumber) {
		try {
			return Response.status(Response.Status.OK)
					.entity(accountService.createCustomerCreationRequest(accountNumber, UUID.randomUUID().toString(), Role.CUSTOMER)).build();			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	
		}
	}
    
	
	// POST : /customers/tokens -> return HashSet<Token>
    public static class CustomerTokensDTO {
        public String customerId, numberOfTokens;
    }
	@POST
	@Path("/tokens")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(CustomerTokensDTO data) {
		
		HashSet<Token> tokens = new HashSet<Token>();
		tokens.add(new Token(data.customerId, data.numberOfTokens, true));
		
		int numberOfTokens;
		try {
			numberOfTokens = Integer.parseInt(data.numberOfTokens);			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		
		try {
			return Response.status(Response.Status.OK)
					.entity(tokenService.getTokensMessageService(UUID.randomUUID().toString(), data.customerId, numberOfTokens).getArgument(0, EventResponse.class).getArgument(0, Token[].class)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
//		__TokenCreationRequest__
//			CustomerId
//			NumberOfTokens
//			SessionId
	}
	
	
	
//	try {
//	service.createCustomer(CreationRequest.getAccountNumber());
//} catch (BankServiceException_Exception e) {
//	e.printStackTrace();
//	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//}

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
