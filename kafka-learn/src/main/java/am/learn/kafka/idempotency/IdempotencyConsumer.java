package am.learn.kafka.idempotency;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class IdempotencyConsumer {

    // In production, replace with Redis/DB-backed dedup store with TTL.
    private final Set<String> seen = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = IdempotencyConfig.TOPIC, groupId = "idempotency-group")
    public void listen(ConsumerRecord<String, String> record) {
        Header header = record.headers().lastHeader(KafkaHeaders.CORRELATION_ID);
        String id = header == null ? null : new String(header.value(), StandardCharsets.UTF_8);
        if (id == null) {
            log.warn("[idempotency] <- no message id header, processing anyway");
            return;
        }
        if (!seen.add(id)) {
            log.warn("[idempotency] <- DUPLICATE id={} skipped", id);
            return;
        }
        log.info("[idempotency] <- NEW id={} payload={}", id, record.value());
    }
}
