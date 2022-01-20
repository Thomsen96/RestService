package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.ReportService;
import restService.Application.AccountService.Role;
import restService.Application.TokenService;
import restService.Domain.AccountDTO;
import restService.Domain.CustomerTokensDTO;
import restService.Domain.ErrorDTO;
import restService.Domain.Payment;
import restService.Domain.Token;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import com.google.gson.GsonBuilder;

import messaging.Event;
import messaging.EventResponse;

@Path("/customers")
public class CustomerResource {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
	TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());
	ReportService reportService = new ReportService(new MessageQueueFactory().getMessageQueue());

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(AccountDTO accountDTO) {
		try {
			EventResponse outcome = accountService.createCustomerCreationRequest(UUID.randomUUID().toString(), accountDTO.accountNumber, Role.CUSTOMER).getArgument(0, EventResponse.class); 
			
			if(outcome.isSuccess()) {
				return Response.status(Response.Status.CREATED)
						.entity(new AccountDTO(outcome.getArgument(0, String.class)))
						.build();
			} else {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(outcome.getErrorMessage())).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(e.getStackTrace().toString())).build();
		}
	}

	// POST : /customers/tokens -> return HashSet<Token>


	@POST
	@Path("/tokens")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(CustomerTokensDTO data) {
		System.out.println(data);
		try {
			String id = data.getCustomerId();
			int num = data.getNumberOfTokens();
			EventResponse outcome = tokenService
			.getTokensMessageService(UUID.randomUUID().toString(), data.customerId, data.numberOfTokens)
					//.getTokensMessageService(UUID.randomUUID().toString(), id, num)
					.getArgument(0, EventResponse.class);
			if(outcome.isSuccess()) {
				return Response.status(Response.Status.OK)
				.entity(outcome.getArgument(0, String[].class))
//						.entity(new GsonBuilder().setPrettyPrinting().create().toJson(outcome.getArgument(0, String[].class)))
						.build();				
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
		}
	}

	@GET
	@Path("/reports/{customerId}")
	public Response getReport(@PathParam("customerId") String customerId) {
    	try {
    		EventResponse outcome = reportService.getReport(UUID.randomUUID().toString(), customerId, ReportService.Role.CUSTOMER);
    		
            if (outcome.isSuccess()) {
            	Payment[] payments = outcome.getArgument(0, Payment[].class);
                return Response.status(Response.Status.OK).entity(payments).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
    	} catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
    	}	
	}

}
