package am.learn.kafka.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class ObservabilityProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Counter sentCounter;

    public ObservabilityProducer(KafkaTemplate<String, String> kafkaTemplate, MeterRegistry registry) {
        this.kafkaTemplate = kafkaTemplate;
        this.sentCounter = Counter.builder("learn.kafka.messages.sent")
                .tag("topic", ObservabilityConfig.TOPIC).register(registry);
    }

    public void send(String message) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        try {
            log.info("[observability] -> {}", message);
            ProducerRecord<String, String> record = new ProducerRecord<>(ObservabilityConfig.TOPIC, message);
            record.headers().add(ObservabilityConfig.CORRELATION_HEADER,
                    correlationId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(record);
            sentCounter.increment();
        } finally {
            MDC.remove("correlationId");
        }
    }
}
