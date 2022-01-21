package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Application.PaymentService;
import restService.Application.ReportService;
import restService.Domain.DTO;
import restService.Domain.ReportDTO;
import restService.Infrastructure.MessageQueueFactory;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;

@Path("/merchants")
public class MerchantResource {

    AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
    PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());
    ReportService reportService = new ReportService(new MessageQueueFactory().getMessageQueue());


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(DTO.CreateAccount account) {
        System.out.println("Create merchant");
        try {
            var e = accountService.accountCreationRequest(UUID.randomUUID().toString(), account.accountId, Role.MERCHANT);
            var eventResponse = e.getArgument(0, EventResponse.class);
            
            if(eventResponse.isSuccess()) {
            	var id = eventResponse.getArgument(0, String.class);
            	return Response.status(Response.Status.CREATED)
            			.entity(new DTO.CreateAccountResponse(id))
            			.build();
            } else {
            	return Response.status(Response.Status.BAD_REQUEST)
            			.entity(eventResponse.getErrorMessage())
            			.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }
    }


    @POST
    @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(DTO.CreatePayment dto) {
        System.out.println("Create payment with " + dto.toString());
        try {
            EventResponse outcome = paymentService.createPaymentRequest(UUID.randomUUID().toString(), dto);

            if (outcome.isSuccess()) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }

    }

    @GET
    @Path("/reports/{merchantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport(@PathParam("merchantId") String merchantId) {
    	try {
    		EventResponse outcome = reportService.getReport(UUID.randomUUID().toString(), merchantId, ReportService.Role.MERCHANT);
    		
            if (outcome.isSuccess()) {
            	ReportDTO.Merchant report = outcome.getArgument(0, ReportDTO.Merchant.class);
				System.out.println("IAM HERE: " + report);
				return Response.status(Response.Status.OK).entity(report).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
    	} catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
    	}	
    }

}
