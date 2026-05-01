package am.learn.kafka.partitioning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartitioningProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String key, String message) {
        log.info("[partitioning] -> key={} message={}", key, message);
        kafkaTemplate.send(PartitioningConfig.TOPIC, key, message);
    }
}
