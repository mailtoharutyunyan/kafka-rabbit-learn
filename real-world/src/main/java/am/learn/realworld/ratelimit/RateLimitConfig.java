package am.learn.realworld.ratelimit;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Rate-limited consumer: processes at most N messages per second.
 * Uses a token-bucket approach with Thread.sleep to throttle.
 */
@Configuration
public class RateLimitConfig {

    public static final String TOPIC = "learn.ratelimit";
    public static final String FACTORY = "rateLimitFactory";

    @Bean
    NewTopic rateLimitTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    ConsumerFactory<String, String> rateLimitConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ratelimit-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    @Bean(name = FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> rateLimitFactory(
            ConsumerFactory<String, String> rateLimitConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rateLimitConsumerFactory);
        return factory;
    }
}
