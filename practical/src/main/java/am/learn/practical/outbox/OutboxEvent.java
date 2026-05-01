package am.learn.practical.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Outbox table: stores events that must be published to Kafka.
 * A scheduled poller reads unsent events and publishes them.
 * This guarantees at-least-once delivery without distributed transactions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private String aggregateId;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Builder.Default
    private boolean published = false;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
