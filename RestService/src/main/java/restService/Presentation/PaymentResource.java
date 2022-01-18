package restService.Presentation;

import messaging.Event;
import messaging.implementations.RabbitMqQueue;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    //CompletableFuture<Event> success = new CompletableFuture<>();

    ConcurrentHashMap<String, CompletableFuture<Event>> map = new ConcurrentHashMap<>();


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(PaymentDTO dto) {
        String sid = UUID.randomUUID().toString();
        map.put(sid, new CompletableFuture<>());
        queue.publish(new Event("PaymentRequest", new Object[] { dto, sid }));
        queue.addHandler("PaymentResponse" + "." + sid, this::handlePaymentResponse);
        map.get(sid).join();
        try {
            Event e = map.get(sid).get();
            if (e.getArgument(0, boolean.class)) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getArgument(1, String.class)).build();
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


}
