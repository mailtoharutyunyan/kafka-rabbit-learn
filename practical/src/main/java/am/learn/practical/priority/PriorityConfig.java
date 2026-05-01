package am.learn.practical.priority;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Priority queue: messages with higher priority are delivered first.
 * RabbitMQ supports priorities 0-255 (typically use 0-10 for readability).
 */
@Configuration
public class PriorityConfig {

    public static final String QUEUE = "learn.priority";
    public static final int MAX_PRIORITY = 10;

    @Bean
    Queue priorityQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-max-priority", MAX_PRIORITY)
                .build();
    }
}
