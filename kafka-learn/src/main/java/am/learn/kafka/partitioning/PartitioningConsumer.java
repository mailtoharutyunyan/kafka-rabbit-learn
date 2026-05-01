package am.learn.kafka.partitioning;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PartitioningConsumer {

    @KafkaListener(topics = PartitioningConfig.TOPIC, groupId = "partitioning-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("[partitioning] <- partition={} key={} value={}",
                record.partition(), record.key(), record.value());
    }
}
