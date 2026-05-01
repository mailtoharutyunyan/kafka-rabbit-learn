package am.learn.realworld.saga;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Saga (Choreography): distributed transaction across services via events.
 *
 * Flow: OrderService → "order.created" → PaymentService → "payment.completed"/"payment.failed"
 *       → OrderService listens and updates status (completed/compensated)
 *
 * No central orchestrator — each service reacts to events and emits its own.
 */
@Configuration
public class SagaConfig {

    public static final String ORDER_EVENTS = "learn.saga.order-events";
    public static final String PAYMENT_EVENTS = "learn.saga.payment-events";

    @Bean NewTopic sagaOrderEvents() { return TopicBuilder.name(ORDER_EVENTS).partitions(1).replicas(1).build(); }
    @Bean NewTopic sagaPaymentEvents() { return TopicBuilder.name(PAYMENT_EVENTS).partitions(1).replicas(1).build(); }
}
