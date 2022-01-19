package restService.Presentation;

import restService.Application.PaymentService;
import restService.Infrastructure.MessageQueueFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/payments")
public class PaymentsResource {

    private final PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());

    @GET
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentServiceStatus() throws Exception {
        return Response.status(Response.Status.OK).entity(paymentService.getStatus(UUID.randomUUID().toString())).build();
    }



}
