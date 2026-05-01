package am.learn.kafka.manualack;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManualAckConsumer {

    @KafkaListener(topics = ManualAckConfig.TOPIC, containerFactory = ManualAckConfig.FACTORY)
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("[manualack] <- value={} offset={}", record.value(), record.offset());
        if ("skip-ack".equalsIgnoreCase(record.value())) {
            log.warn("[manualack] NOT acking - message will be redelivered on restart");
            return;
        }
        ack.acknowledge();
        log.info("[manualack] offset committed: {}", record.offset());
    }
}
