package am.learn.rabbit.topicexchange;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    public static final String EXCHANGE = "learn.topic";
    public static final String QUEUE_ORDERS = "learn.topic.orders";    // order.*
    public static final String QUEUE_USER_CREATED = "learn.topic.userCreated"; // user.created

    @Bean TopicExchange topicExchange() { return new TopicExchange(EXCHANGE); }
    @Bean Queue ordersQueue() { return QueueBuilder.durable(QUEUE_ORDERS).build(); }
    @Bean Queue userCreatedQueue() { return QueueBuilder.durable(QUEUE_USER_CREATED).build(); }

    @Bean
    Binding bindOrders(TopicExchange topicExchange, Queue ordersQueue) {
        return BindingBuilder.bind(ordersQueue).to(topicExchange).with("order.*");
    }

    @Bean
    Binding bindUserCreated(TopicExchange topicExchange, Queue userCreatedQueue) {
        return BindingBuilder.bind(userCreatedQueue).to(topicExchange).with("user.created");
    }
}
