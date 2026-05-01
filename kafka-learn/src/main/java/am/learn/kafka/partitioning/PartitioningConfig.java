package am.learn.kafka.partitioning;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PartitioningConfig {

    public static final String TOPIC = "learn.partitioning";

    @Bean
    NewTopic partitioningTopic() {
        return TopicBuilder.name(TOPIC).partitions(3).replicas(1).build();
    }
}
