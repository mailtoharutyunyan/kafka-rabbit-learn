package am.learn.realworld.consumerlag;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Monitors Kafka consumer lag using AdminClient.
 * Publishes lag as Micrometer gauge → queryable via /actuator/metrics/kafka.consumer.lag
 */
@Slf4j
@Component
public class ConsumerLagMonitor {

    private final String bootstrapServers;
    private final MeterRegistry meterRegistry;
    private final AtomicLong totalLag = new AtomicLong(0);
    private AdminClient adminClient;

    private static final String MONITORED_GROUP = "saga-payment-group";

    public ConsumerLagMonitor(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                              MeterRegistry meterRegistry) {
        this.bootstrapServers = bootstrapServers;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    void init() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        adminClient = AdminClient.create(props);

        Gauge.builder("kafka.consumer.lag", totalLag, AtomicLong::get)
                .tag("group", MONITORED_GROUP)
                .description("Total consumer lag for group")
                .register(meterRegistry);
    }

    @PreDestroy
    void destroy() {
        if (adminClient != null) adminClient.close();
    }

    @Scheduled(fixedDelay = 5000L)
    public void checkLag() {
        try {
            ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets(MONITORED_GROUP);
            Map<TopicPartition, OffsetAndMetadata> offsets = result.partitionsToOffsetAndMetadata().get();

            Map<TopicPartition, org.apache.kafka.clients.admin.ListOffsetsResult.ListOffsetsResultInfo> endOffsets =
                    adminClient.listOffsets(
                            offsets.keySet().stream().collect(
                                    java.util.stream.Collectors.toMap(
                                            tp -> tp,
                                            tp -> org.apache.kafka.clients.admin.OffsetSpec.latest()
                                    )
                            )
                    ).all().get();

            long lag = 0;
            for (TopicPartition tp : offsets.keySet()) {
                long committed = offsets.get(tp).offset();
                long end = endOffsets.get(tp).offset();
                lag += Math.max(0, end - committed);
            }

            totalLag.set(lag);
            if (lag > 0) {
                log.info("[consumer-lag] group={} totalLag={}", MONITORED_GROUP, lag);
            }
            if (lag > 100) {
                log.warn("[consumer-lag] HIGH LAG ALERT! group={} lag={}", MONITORED_GROUP, lag);
            }
        } catch (Exception e) {
            log.debug("[consumer-lag] could not fetch lag: {}", e.getMessage());
        }
    }
}
