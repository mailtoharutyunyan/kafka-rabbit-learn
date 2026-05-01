package am.learn.practical.compaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompactionConsumer {

    @KafkaListener(topics = CompactionConfig.TOPIC, groupId = "compaction-group")
    public void listen(ConsumerRecord<String, String> record) {
        if (record.value() == null) {
            log.info("[compaction] <- DELETED key={}", record.key());
        } else {
            log.info("[compaction] <- key={} value={}", record.key(), record.value());
        }
    }
}
