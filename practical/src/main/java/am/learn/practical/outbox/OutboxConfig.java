package am.learn.practical.outbox;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class OutboxConfig {

    public static final String TOPIC = "learn.outbox.events";

    @Bean
    NewTopic outboxTopic() {
        return TopicBuilder.name(TOPIC).partitions(3).replicas(1).build();
    }
}
