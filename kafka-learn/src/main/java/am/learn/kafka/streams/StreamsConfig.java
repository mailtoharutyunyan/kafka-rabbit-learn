package am.learn.kafka.streams;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class StreamsConfig {

    public static final String INPUT_TOPIC = "learn.streams.input";
    public static final String OUTPUT_TOPIC = "learn.streams.output";

    @Bean
    NewTopic streamsInput() {
        return TopicBuilder.name(INPUT_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic streamsOutput() {
        return TopicBuilder.name(OUTPUT_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfig(
            @org.springframework.beans.factory.annotation.Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG, "learn-streams-app");
        props.put(org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    KStream<String, String> wordCountStream(StreamsBuilder builder) {
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
        KTable<String, Long> counts = source
                .flatMapValues(line -> Arrays.asList(line.toLowerCase(Locale.ROOT).split("\\W+")))
                .filter((k, word) -> word != null && !word.isBlank())
                .groupBy((k, word) -> word)
                .count(Materialized.as("word-counts"));
        counts.toStream()
                .mapValues(count -> Long.toString(count))
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
        return source;
    }
}
