package restService.Infrastructure;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

public class MessageQueueFactory {
  
  static RabbitMqQueue messageQueue = null;

	public MessageQueue getMessageQueue() {
		// The singleton pattern.
		// Ensure that there is at most
		// one instance of a PaymentService
		if (messageQueue != null) {
			return messageQueue;
		}
		
		// Hookup the classes to send and receive
		// messages via RabbitMq, i.e. RabbitMqSender and
		// RabbitMqListener. 
		// This should be done in the factory to avoid 
		// the PaymentService knowing about them. This
		// is called dependency injection.
		// At the end, we can use the PaymentService in tests
		// without sending actual messages to RabbitMq.
		RabbitMqQueue messageQueue = new RabbitMqQueue("localhost");
		return messageQueue;
  }
}
