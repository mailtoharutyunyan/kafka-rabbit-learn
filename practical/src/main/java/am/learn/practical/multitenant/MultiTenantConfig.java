package am.learn.practical.multitenant;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Multi-tenant messaging: isolate tenant data using:
 * - Kafka: per-tenant topics (learn.mt.{tenantId}) + tenant header
 * - RabbitMQ: topic exchange with routing key = tenantId.eventType
 */
@Configuration
public class MultiTenantConfig {

    public static final String KAFKA_TOPIC_PREFIX = "learn.mt.";
    public static final String RABBIT_EXCHANGE = "learn.multitenant";
    public static final String RABBIT_QUEUE_TENANT_A = "learn.mt.tenant-a";
    public static final String RABBIT_QUEUE_TENANT_B = "learn.mt.tenant-b";
    public static final String RABBIT_QUEUE_ALL = "learn.mt.all";

    @Bean NewTopic mtTenantA() { return TopicBuilder.name(KAFKA_TOPIC_PREFIX + "tenant-a").partitions(1).replicas(1).build(); }
    @Bean NewTopic mtTenantB() { return TopicBuilder.name(KAFKA_TOPIC_PREFIX + "tenant-b").partitions(1).replicas(1).build(); }

    @Bean TopicExchange mtExchange() { return new TopicExchange(RABBIT_EXCHANGE); }
    @Bean Queue mtQueueA() { return QueueBuilder.durable(RABBIT_QUEUE_TENANT_A).build(); }
    @Bean Queue mtQueueB() { return QueueBuilder.durable(RABBIT_QUEUE_TENANT_B).build(); }
    @Bean Queue mtQueueAll() { return QueueBuilder.durable(RABBIT_QUEUE_ALL).build(); }

    @Bean Binding mtBindA(TopicExchange mtExchange, Queue mtQueueA) {
        return BindingBuilder.bind(mtQueueA).to(mtExchange).with("tenant-a.#");
    }
    @Bean Binding mtBindB(TopicExchange mtExchange, Queue mtQueueB) {
        return BindingBuilder.bind(mtQueueB).to(mtExchange).with("tenant-b.#");
    }
    @Bean Binding mtBindAll(TopicExchange mtExchange, Queue mtQueueAll) {
        return BindingBuilder.bind(mtQueueAll).to(mtExchange).with("#");
    }
}
