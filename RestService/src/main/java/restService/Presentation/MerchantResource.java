package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Application.PaymentService;
import restService.Application.ReportService;
import restService.Domain.PaymentDTO;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;

@Path("/merchants")
public class MerchantResource {

    AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
    PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());
    ReportService reportService = new ReportService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(String accountNumber) {
        try {
            var e = accountService.createCustomerCreationRequest(accountNumber, UUID.randomUUID().toString(), Role.MERCHANT);
            var id = e.getArgument(0, EventResponse.class).getArgument(0, String.class);
            return Response.status(Response.Status.OK)
                    .entity(id)
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/reports/{merchantId}")
    public Response getReport(@PathParam("merchantId") String merchantId) {
    	try {
    		EventResponse outcome = reportService.getMerchantReport(UUID.randomUUID().toString(), merchantId, ReportService.Role.MERCHANT);
    		
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
