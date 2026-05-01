package am.learn.practical.outbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OutboxConsumer {

    @KafkaListener(topics = OutboxConfig.TOPIC, groupId = "outbox-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("[outbox] <- event from Kafka: key={} value={}", record.key(), record.value());
    }
}
