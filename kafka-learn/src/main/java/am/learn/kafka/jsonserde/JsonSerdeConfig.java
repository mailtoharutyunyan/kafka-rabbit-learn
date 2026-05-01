package am.learn.kafka.jsonserde;

import am.learn.kafka.common.OrderEvent;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JsonSerdeConfig {

    public static final String TOPIC = "learn.jsonserde";
    public static final String FACTORY = "jsonListenerFactory";
    public static final String TEMPLATE = "jsonKafkaTemplate";

    @Bean
    NewTopic jsonTopic() {
        return TopicBuilder.name(TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    ProducerFactory<String, OrderEvent> jsonProducerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name = TEMPLATE)
    KafkaTemplate<String, OrderEvent> jsonKafkaTemplate(
            @Qualifier("jsonProducerFactory") ProducerFactory<String, OrderEvent> jsonProducerFactory) {
        return new KafkaTemplate<>(jsonProducerFactory);
    }

    @Bean
    ConsumerFactory<String, OrderEvent> jsonConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<>(OrderEvent.class);
        deserializer.addTrustedPackages("am.learn.kafka.common");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean(name = FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, OrderEvent> jsonListenerFactory(
            @Qualifier("jsonConsumerFactory") ConsumerFactory<String, OrderEvent> jsonConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(jsonConsumerFactory);
        return factory;
    }
}
