package restService.Presentation;

import messaging.Event;
import messaging.implementations.RabbitMqQueue;

import javax.ws.rs.Path;
import java.util.concurrent.CompletableFuture;

@Path("/payments")
public class PaymentResource {

    public static class PaymentDTO {
        public String token;
        public String merchant;
        public String amount;
    }

    RabbitMqQueue queue = new RabbitMqQueue("localhost");
    CompletableFuture<Boolean> success = new CompletableFuture<>();

    public Boolean pay(PaymentDTO dto) {
        queue.publish(new Event("PaymentRequest", new Object[] { dto }));
        return success.join();
    }

}
