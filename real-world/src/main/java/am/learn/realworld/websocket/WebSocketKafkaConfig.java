package am.learn.realworld.websocket;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class WebSocketKafkaConfig {

    public static final String TOPIC = "learn.ws.notifications";

    @Bean
    NewTopic wsNotificationsTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }
}
