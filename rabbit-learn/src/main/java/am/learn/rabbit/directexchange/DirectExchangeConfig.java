package am.learn.rabbit.directexchange;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    public static final String EXCHANGE = "learn.direct";
    public static final String QUEUE_INFO = "learn.direct.info";
    public static final String QUEUE_ERROR = "learn.direct.error";
    public static final String KEY_INFO = "info";
    public static final String KEY_ERROR = "error";

    @Bean DirectExchange directExchange() { return new DirectExchange(EXCHANGE); }
    @Bean Queue directInfoQueue() { return QueueBuilder.durable(QUEUE_INFO).build(); }
    @Bean Queue directErrorQueue() { return QueueBuilder.durable(QUEUE_ERROR).build(); }

    @Bean
    Binding bindInfo(DirectExchange directExchange, Queue directInfoQueue) {
        return BindingBuilder.bind(directInfoQueue).to(directExchange).with(KEY_INFO);
    }

    @Bean
    Binding bindError(DirectExchange directExchange, Queue directErrorQueue) {
        return BindingBuilder.bind(directErrorQueue).to(directExchange).with(KEY_ERROR);
    }
}
