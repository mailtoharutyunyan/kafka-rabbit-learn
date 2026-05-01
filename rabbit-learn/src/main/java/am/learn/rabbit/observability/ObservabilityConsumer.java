package am.learn.rabbit.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObservabilityConsumer {

    private final Counter receivedCounter;

    public ObservabilityConsumer(MeterRegistry registry) {
        this.receivedCounter = Counter.builder("learn.rabbit.messages.received")
                .tag("queue", ObservabilityConfig.QUEUE).register(registry);
    }

    @RabbitListener(queues = ObservabilityConfig.QUEUE)
    public void listen(Message message) {
        Object header = message.getMessageProperties().getHeaders().get(ObservabilityConfig.CORRELATION_HEADER);
        String correlationId = header == null ? "none" : header.toString();
        MDC.put("correlationId", correlationId);
        try {
            log.info("[observability] <- {}", new String(message.getBody()));
            receivedCounter.increment();
        } finally {
            MDC.remove("correlationId");
        }
    }
}
