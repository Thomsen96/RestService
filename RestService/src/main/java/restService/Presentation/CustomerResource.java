package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.ReportService;
import restService.Application.AccountService.Role;
import restService.Application.TokenService;
import restService.Domain.Token;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;

@Path("/customers")
public class CustomerResource {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
	private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());
	ReportService reportService = new ReportService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(String accountNumber) {
		try {
			return Response.status(Response.Status.OK)
					.entity(accountService.createCustomerCreationRequest(accountNumber, UUID.randomUUID().toString(),Role.CUSTOMER).getArgument(0, EventResponse.class))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	// POST : /customers/tokens -> return HashSet<Token>
	public static class CustomerTokensDTO {
		public String customerId;
		public Integer numberOfTokens;
	}

	@POST
	@Path("/tokens")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(CustomerTokensDTO data) {
		try {
			return Response.status(Response.Status.OK)
					.entity(tokenService
							.getTokensMessageService(UUID.randomUUID().toString(), data.customerId, data.numberOfTokens)
							.getArgument(0, EventResponse.class).getArgument(0, Token[].class))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/reports/{customerId}")
	public Response getReport(@PathParam("customerId") String customerId) {
    	try {
    		EventResponse outcome = reportService.getMerchantReport(UUID.randomUUID().toString(), customerId, ReportService.Role.CUSTOMER);
    		
            if (outcome.isSuccess()) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
    	} catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}	
	}

}
