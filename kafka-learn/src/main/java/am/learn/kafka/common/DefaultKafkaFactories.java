package am.learn.kafka.common;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot's auto-configured {@code ConsumerFactory} backs off when
 * we declare our own (manualack, transactions, jsonserde). We provide
 * an explicit default one so the generic listener factories (dlt, retrybackoff,
 * plus the default container factory used by plain {@code @KafkaListener}) have
 * something to wire. A placeholder group.id is set so Spring's start-checks pass —
 * each listener overrides it via {@code groupId = "..."}.
 */
@Configuration
public class DefaultKafkaFactories {

    public static final String DEFAULT_CONSUMER_FACTORY = "defaultConsumerFactory";

    @Bean(name = DEFAULT_CONSUMER_FACTORY)
    ConsumerFactory<String, String> defaultConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "learn-default-group");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    @Bean(name = "kafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> defaultConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultConsumerFactory);
        return factory;
    }

    @Primary
    @Bean(name = "defaultProducerFactory")
    ProducerFactory<String, String> defaultProducerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Primary
    @Bean(name = "kafkaTemplate")
    KafkaTemplate<String, String> kafkaTemplate(
            @org.springframework.beans.factory.annotation.Qualifier("defaultProducerFactory")
            ProducerFactory<String, String> defaultProducerFactory) {
        return new KafkaTemplate<>(defaultProducerFactory);
    }
}
