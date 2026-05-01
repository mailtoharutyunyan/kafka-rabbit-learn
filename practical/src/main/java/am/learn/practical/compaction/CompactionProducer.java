package am.learn.practical.compaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void upsert(String userId, String profileJson) {
        log.info("[compaction] -> key={} value={}", userId, profileJson);
        kafkaTemplate.send(CompactionConfig.TOPIC, userId, profileJson);
    }

    public void delete(String userId) {
        log.info("[compaction] -> TOMBSTONE key={}", userId);
        kafkaTemplate.send(CompactionConfig.TOPIC, userId, null);
    }
}
