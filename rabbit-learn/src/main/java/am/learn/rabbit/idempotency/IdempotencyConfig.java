package am.learn.rabbit.idempotency;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdempotencyConfig {

    public static final String QUEUE = "learn.idempotency";

    @Bean
    Queue idempotencyQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}
