package am.learn.kafka.basics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class BasicsConfig {

    public static final String TOPIC = "learn.basics";

    @Bean
    NewTopic basicsTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }
}
