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
public class CustomerResource {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
	private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());

	@GET
	@Path("/status")
	public Response get() {
		return Response.status(Response.Status.OK).entity(accountService.getStatus(UUID.randomUUID().toString()))
				.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(String accountNumber) {
		try {
			return Response.status(Response.Status.OK)
					.entity(accountService.createCustomerCreationRequest(accountNumber, UUID.randomUUID().toString(),
							Role.CUSTOMER))
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

		HashSet<Token> tokens = new HashSet<Token>();
		tokens.add(new Token(data.customerId, data.numberOfTokens.toString(), true));

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
		return Response
				.status(Response.Status.OK)
				.entity(String.format("Report requested for %s", customerId))
				.build();
	}

	@GET
	@Path("/tokens/status")
	public Response getTokenStatus() {
		return Response.status(Response.Status.OK).entity(tokenService.getStatus(UUID.randomUUID().toString())).build();
	}

}
