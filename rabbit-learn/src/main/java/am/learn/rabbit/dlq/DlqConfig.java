package am.learn.rabbit.dlq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DlqConfig {

    public static final String EXCHANGE = "learn.dlq.main";
    public static final String QUEUE = "learn.dlq.main";
    public static final String ROUTING_KEY = "main";

    public static final String DLX = "learn.dlq.dlx";
    public static final String DLQ = "learn.dlq.dead";

    @Bean DirectExchange mainExchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    Queue mainQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    Binding mainBinding(DirectExchange mainExchange, Queue mainQueue) {
        return BindingBuilder.bind(mainQueue).to(mainExchange).with(ROUTING_KEY);
    }

    @Bean DirectExchange deadExchange() { return new DirectExchange(DLX); }
    @Bean Queue deadQueue() { return QueueBuilder.durable(DLQ).build(); }

    @Bean
    Binding deadBinding(DirectExchange deadExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DLQ);
    }
}
