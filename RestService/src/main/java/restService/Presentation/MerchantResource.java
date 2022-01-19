package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Application.PaymentService;
import restService.Domain.PaymentDTO;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.RabbitMqQueue;


@Path("/merchants")
public class MerchantResource {

	AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
    PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());
	
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
	
	
    @POST
    @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(PaymentDTO dto) {
       try {
    	   EventResponse outcome = paymentService.createPayment(UUID.randomUUID().toString(), dto); 
    	   
    	   if (outcome.isSuccess()) {
               return Response.status(Response.Status.OK).build();
           } else {
               return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
           }
    	   
       } catch (Exception e) {
    	   e.printStackTrace();
           return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
       }
        
    }
	
    
    RabbitMqQueue queue = new RabbitMqQueue("localhost");
    ConcurrentHashMap<String, CompletableFuture<Event>> map = new ConcurrentHashMap<>();
    CompletableFuture<String> status = new CompletableFuture<>();
    @GET
    @Path("/payments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status() throws ExecutionException, InterruptedException {
        String sid = UUID.randomUUID().toString();
        queue.publish(new Event("PaymentStatusRequest", new EventResponse(sid, true, null)));
        queue.addHandler("PaymentStatusResponse." + sid, this::handlePaymentStatusResponse);
        (new Thread(() -> {
            try {
                Thread.sleep(5000);
                status.complete("No reply from a Payment service");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })).start();
        status.join();
        return Response.status(Response.Status.OK).entity(status.get()).build();
    }

    public void handlePaymentStatusResponse(Event event) {
        status.complete(event.getArgument(0, EventResponse.class).getArgument(0, String.class));
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
