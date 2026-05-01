package am.learn.kafka.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String messageId, String payload) {
        log.info("[idempotency] -> id={} payload={}", messageId, payload);
        ProducerRecord<String, String> record = new ProducerRecord<>(IdempotencyConfig.TOPIC, payload);
        record.headers().add(KafkaHeaders.CORRELATION_ID, messageId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
    }
}
