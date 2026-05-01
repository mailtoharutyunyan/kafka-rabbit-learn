package am.learn.rabbit.idempotency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class IdempotencyConsumer {

    // In production, replace with Redis/DB-backed dedup store with TTL.
    private final Set<String> seen = ConcurrentHashMap.newKeySet();

    @RabbitListener(queues = IdempotencyConfig.QUEUE)
    public void listen(Message message) {
        String id = message.getMessageProperties().getMessageId();
        String body = new String(message.getBody());
        if (id == null) {
            log.warn("[idempotency] <- no message-id, processing anyway: {}", body);
            return;
        }
        if (!seen.add(id)) {
            log.warn("[idempotency] <- DUPLICATE id={} skipped", id);
            return;
        }
        log.info("[idempotency] <- NEW id={} body={}", id, body);
    }
}
