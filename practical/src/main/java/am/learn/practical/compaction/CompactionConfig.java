package am.learn.practical.compaction;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Compacted topic: Kafka keeps only the LATEST value per key.
 * Old records with the same key are garbage-collected during log compaction.
 *
 * Use cases: user profiles, config, state snapshots (KTable-style).
 * Send key=null to "tombstone" (delete) a key.
 */
@Configuration
public class CompactionConfig {

    public static final String TOPIC = "learn.compacted.profiles";

    @Bean
    NewTopic compactedTopic() {
        return TopicBuilder.name(TOPIC)
                .partitions(3)
                .replicas(1)
                .config("cleanup.policy", "compact")
                .config("min.cleanable.dirty.ratio", "0.01")
                .config("segment.ms", "100")
                .build();
    }
}
