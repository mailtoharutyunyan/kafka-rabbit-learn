package am.learn.kafka.streams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamsOutputConsumer {

    @KafkaListener(topics = StreamsConfig.OUTPUT_TOPIC, groupId = "streams-output-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("[streams] word={} count={}", record.key(), record.value());
    }
}
