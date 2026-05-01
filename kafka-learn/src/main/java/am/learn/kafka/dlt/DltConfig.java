package am.learn.kafka.dlt;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class DltConfig {

    public static final String TOPIC = "learn.dlt";
    public static final String DLT_TOPIC = "learn.dlt.DLT";
    public static final String FACTORY = "dltListenerFactory";

    @Bean
    NewTopic dltMainTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic dltTopic() {
        return TopicBuilder.name(DLT_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean(name = FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> dltListenerFactory(
            @Qualifier(am.learn.kafka.common.DefaultKafkaFactories.DEFAULT_CONSUMER_FACTORY) ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(DLT_TOPIC, record.partition()));
        // Retry 2 times with 1-second delay, then publish to DLT.
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
