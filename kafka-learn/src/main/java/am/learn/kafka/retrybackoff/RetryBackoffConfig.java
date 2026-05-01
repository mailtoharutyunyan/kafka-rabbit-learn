package am.learn.kafka.retrybackoff;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class RetryBackoffConfig {

    public static final String TOPIC = "learn.retrybackoff";
    public static final String FACTORY = "retryBackoffListenerFactory";

    @Bean
    NewTopic retryTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }

    @Bean(name = FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> retryBackoffListenerFactory(
            @Qualifier(am.learn.kafka.common.DefaultKafkaFactories.DEFAULT_CONSUMER_FACTORY) ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        ExponentialBackOff backOff = new ExponentialBackOff(500L, 2.0);
        backOff.setMaxInterval(5_000L);
        backOff.setMaxElapsedTime(30_000L);
        factory.setCommonErrorHandler(new DefaultErrorHandler(backOff));
        return factory;
    }
}
