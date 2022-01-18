package restService.Presentation;

import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.RabbitMqQueue;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Path("/payments")
public class PaymentResource {

    public static class PaymentDTO {
        public String token;
        public String merchant;
        public String amount;
        public String description;
    }

    RabbitMqQueue queue = new RabbitMqQueue("localhost");
    // CompletableFuture<Event> success = new CompletableFuture<>();

    ConcurrentHashMap<String, CompletableFuture<Event>> map = new ConcurrentHashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(PaymentDTO dto) {
        String sid = UUID.randomUUID().toString();
        map.put(sid, new CompletableFuture<>());
        queue.publish(new Event("PaymentRequest", new Object[] {new EventResponse(sid, true, null, dto)}));
        queue.addHandler("PaymentResponse" + "." + sid, this::handlePaymentResponse);
        map.get(sid).join();
        try {
            EventResponse e = map.get(sid).get().getArgument(0, EventResponse.class);

            if (e.isSuccess()) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorMessage()).build();
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    public void handlePaymentResponse(Event event) {
        var sid = event.getType().split("\\.")[1];
        var future = map.get(sid);
        future.complete(event);
    }

    CompletableFuture<String> status = new CompletableFuture<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response status() throws ExecutionException, InterruptedException {
        queue.publish(new Event("PaymentStatusRequest"));
        queue.addHandler("PaymentStatusResponse.*", this::handlePaymentStatusResponse);
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    status.complete("No reply from a Payment service");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        status.join();
        return Response.status(Response.Status.OK).entity(status.get()).build();
    }

    public void handlePaymentStatusResponse(Event event) {
        status.complete(event.getArgument(0, String.class));
    }

}
