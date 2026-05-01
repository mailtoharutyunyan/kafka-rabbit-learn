package am.learn.kafka.consumergroups;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ConsumerGroupsConfig {

    public static final String TOPIC = "learn.consumergroups";

    @Bean
    NewTopic consumerGroupsTopic() {
        return TopicBuilder.name(TOPIC).partitions(3).replicas(1).build();
    }
}
