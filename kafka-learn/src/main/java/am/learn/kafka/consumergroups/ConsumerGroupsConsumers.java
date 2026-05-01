package am.learn.kafka.consumergroups;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerGroupsConsumers {

    // Two instances in the SAME group - they share partitions (work is split).
    @KafkaListener(topics = ConsumerGroupsConfig.TOPIC, groupId = "shared-group", concurrency = "2")
    public void sharedGroup(ConsumerRecord<String, String> record) {
        log.info("[shared-group] partition={} value={}", record.partition(), record.value());
    }

    // DIFFERENT group - receives all messages independently (pub-sub style).
    @KafkaListener(topics = ConsumerGroupsConfig.TOPIC, groupId = "audit-group")
    public void auditGroup(ConsumerRecord<String, String> record) {
        log.info("[audit-group] partition={} value={}", record.partition(), record.value());
    }
}
