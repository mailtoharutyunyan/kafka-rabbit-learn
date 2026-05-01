package am.learn.kafka.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ObservabilityConsumer {

    private final Counter receivedCounter;

    public ObservabilityConsumer(MeterRegistry registry) {
        this.receivedCounter = Counter.builder("learn.kafka.messages.received")
                .tag("topic", ObservabilityConfig.TOPIC).register(registry);
    }

    @KafkaListener(topics = ObservabilityConfig.TOPIC, groupId = "observability-group")
    public void listen(ConsumerRecord<String, String> record) {
        Header header = record.headers().lastHeader(ObservabilityConfig.CORRELATION_HEADER);
        String correlationId = header == null ? "none" : new String(header.value(), StandardCharsets.UTF_8);
        MDC.put("correlationId", correlationId);
        try {
            log.info("[observability] <- {}", record.value());
            receivedCounter.increment();
        } finally {
            MDC.remove("correlationId");
        }
    }
}
