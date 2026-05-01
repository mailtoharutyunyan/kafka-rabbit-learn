package am.learn.rabbit.retrybackoff;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryBackoffConfig {

    public static final String QUEUE = "learn.retrybackoff";
    public static final String FACTORY = "retryBackoffContainerFactory";

    @Bean
    Queue retryQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean(name = FACTORY)
    SimpleRabbitListenerContainerFactory retryBackoffContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(4)
                .backOffOptions(500L, 2.0, 5_000L) // initial, multiplier, max
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return factory;
    }
}
