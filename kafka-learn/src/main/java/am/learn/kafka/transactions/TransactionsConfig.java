package am.learn.kafka.transactions;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionsConfig {

    public static final String TOPIC = "learn.transactions";
    public static final String TEMPLATE = "txKafkaTemplate";
    public static final String FACTORY = "txListenerFactory";
    public static final String TX_MANAGER = "kafkaTxManager";

    @Bean
    NewTopic txTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    ProducerFactory<String, String> txProducerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        factory.setTransactionIdPrefix("tx-learn-");
        return factory;
    }

    @Bean(name = TEMPLATE)
    KafkaTemplate<String, String> txKafkaTemplate(
            @Qualifier("txProducerFactory") ProducerFactory<String, String> txProducerFactory) {
        return new KafkaTemplate<>(txProducerFactory);
    }

    @Bean(name = TX_MANAGER)
    KafkaTransactionManager<String, String> kafkaTxManager(
            @Qualifier("txProducerFactory") ProducerFactory<String, String> txProducerFactory) {
        return new KafkaTransactionManager<>(txProducerFactory);
    }

    @Bean
    ConsumerFactory<String, String> txConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tx-group");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    @Bean(name = FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> txListenerFactory(
            @Qualifier("txConsumerFactory") ConsumerFactory<String, String> txConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(txConsumerFactory);
        return factory;
    }
}
