package am.learn.practical.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BatchConsumer {

    @KafkaListener(topics = BatchConfig.TOPIC, containerFactory = BatchConfig.FACTORY)
    public void listen(List<ConsumerRecord<String, String>> records) {
        log.info("[batch] <- received BATCH of {} records", records.size());
        records.forEach(r ->
                log.debug("[batch]   partition={} key={} value={}", r.partition(), r.key(), r.value()));
        log.info("[batch] processed batch: first={} last={}",
                records.getFirst().value(), records.getLast().value());
    }
}
