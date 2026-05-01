package am.learn.practical.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic saves domain event to the outbox table in the SAME transaction
 * as the business data change. No distributed transaction needed.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository repository;

    @Transactional
    public OutboxEvent createOrder(String orderId, String customer, String amount) {
        // In a real app, you'd also persist the Order entity here in the SAME transaction.
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("Order")
                .aggregateId(orderId)
                .eventType("OrderCreated")
                .payload("{\"orderId\":\"" + orderId + "\",\"customer\":\"" + customer + "\",\"amount\":" + amount + "}")
                .build();
        repository.save(event);
        log.info("[outbox] saved event to outbox table: id={} type={}", event.getId(), event.getEventType());
        return event;
    }
}
