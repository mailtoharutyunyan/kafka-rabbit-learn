package am.learn.rabbit.basics;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicsConfig {

    public static final String QUEUE = "learn.basics";

    @Bean
    Queue basicsQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}
