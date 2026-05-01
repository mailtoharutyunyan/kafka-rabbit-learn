package am.learn.rabbit.observability;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    public static final String QUEUE = "learn.observability";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    @Bean
    Queue observabilityQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}
