package am.learn.kafka.idempotency;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class IdempotencyConfig {

    public static final String TOPIC = "learn.idempotency";

    @Bean
    NewTopic idempotencyTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }
}
