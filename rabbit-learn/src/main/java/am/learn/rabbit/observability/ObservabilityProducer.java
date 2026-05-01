package am.learn.rabbit.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ObservabilityProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Counter sentCounter;

    public ObservabilityProducer(RabbitTemplate rabbitTemplate, MeterRegistry registry) {
        this.rabbitTemplate = rabbitTemplate;
        this.sentCounter = Counter.builder("learn.rabbit.messages.sent")
                .tag("queue", ObservabilityConfig.QUEUE).register(registry);
    }

    public void send(String message) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        try {
            log.info("[observability] -> {}", message);
            rabbitTemplate.send("", ObservabilityConfig.QUEUE,
                    MessageBuilder.withBody(message.getBytes())
                            .setHeader(ObservabilityConfig.CORRELATION_HEADER, correlationId)
                            .build());
            sentCounter.increment();
        } finally {
            MDC.remove("correlationId");
        }
    }
}
