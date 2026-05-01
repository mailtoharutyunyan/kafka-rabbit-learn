package am.learn.rabbit.workqueues;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkQueuesConfig {

    public static final String QUEUE = "learn.workqueues";

    @Bean
    Queue workQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}
