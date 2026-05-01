package am.learn.practical.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Polls the outbox table every 2 seconds and publishes unsent events to Kafka.
 * Marks them as published in the same transaction.
 *
 * In production, consider: Debezium CDC, or a Kafka Connect JDBC source connector.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000L)
    @Transactional
    public void poll() {
        List<OutboxEvent> events = repository.findTop100ByPublishedFalseOrderByIdAsc();
        if (events.isEmpty()) return;

        log.info("[outbox-poller] publishing {} events", events.size());
        for (OutboxEvent event : events) {
            kafkaTemplate.send(OutboxConfig.TOPIC, event.getAggregateId(), event.getPayload());
            event.setPublished(true);
        }
        repository.saveAll(events);
        log.info("[outbox-poller] published and marked {} events", events.size());
    }
}
