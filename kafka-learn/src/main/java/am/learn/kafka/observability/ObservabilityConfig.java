package am.learn.kafka.observability;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ObservabilityConfig {

    public static final String TOPIC = "learn.observability";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    @Bean
    NewTopic observabilityTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }
}
