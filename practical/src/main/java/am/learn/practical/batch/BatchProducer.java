package am.learn.practical.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendBatch(int count) {
        log.info("[batch] -> sending {} messages", count);
        for (int i = 0; i < count; i++) {
            kafkaTemplate.send(BatchConfig.TOPIC, "key-" + (i % 5), "event-" + i);
        }
    }
}
