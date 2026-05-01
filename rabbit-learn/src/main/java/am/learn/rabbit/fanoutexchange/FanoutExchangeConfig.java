package am.learn.rabbit.fanoutexchange;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutExchangeConfig {

    public static final String EXCHANGE = "learn.fanout";
    public static final String QUEUE_A = "learn.fanout.A";
    public static final String QUEUE_B = "learn.fanout.B";

    @Bean FanoutExchange fanoutExchange() { return new FanoutExchange(EXCHANGE); }
    @Bean Queue fanoutQueueA() { return QueueBuilder.durable(QUEUE_A).build(); }
    @Bean Queue fanoutQueueB() { return QueueBuilder.durable(QUEUE_B).build(); }

    @Bean Binding bindA(FanoutExchange fanoutExchange, Queue fanoutQueueA) {
        return BindingBuilder.bind(fanoutQueueA).to(fanoutExchange);
    }
    @Bean Binding bindB(FanoutExchange fanoutExchange, Queue fanoutQueueB) {
        return BindingBuilder.bind(fanoutQueueB).to(fanoutExchange);
    }
}
