package am.learn.realworld.quorum;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quorum queues: replicated RAFT-based queues for high availability.
 * Data is replicated across multiple RabbitMQ nodes (here single-node demo).
 *
 * Benefits over classic mirrored queues:
 * - Safer: no split-brain scenarios
 * - Faster leader election
 * - Built-in message deduplication
 * - Poison message handling (delivery-limit)
 */
@Configuration
public class QuorumConfig {

    public static final String QUEUE = "learn.quorum";

    @Bean
    Queue quorumQueue() {
        return QueueBuilder.durable(QUEUE)
                .quorum()
                .deliveryLimit(5)
                .build();
    }
}
