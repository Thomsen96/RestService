package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/merchants")
public class MerchantResource {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response create(String accountNumber) {
		try {
			return Response.status(Response.Status.OK)
					.entity(accountService.createCustomerCreationRequest(accountNumber, UUID.randomUUID().toString(), Role.MERCHANT)).build();			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	
		}
	}
    
	
	// POST : /customers/tokens -> return HashSet<Token>
    public static class MerchantPaymentDTO {
        public String MerchatId, TokenId, Amount, Description;
    }
	@POST
	@Path("/payments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(MerchantPaymentDTO data) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(String.format("Payment requested for mid: %s, token: %s, amount: %s, descr: %s", 
						data.MerchatId, data.TokenId, data.Amount, data.Description))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
	}
	
  @GET
  @Path("/reports/{merchantId}")
  public Response getReport(@PathParam("merchantId") String merchantId) {
			return Response
					.status(Response.Status.OK)
					.entity(String.format("Report requested for %s", merchantId))
					.build();
  }
	

}
