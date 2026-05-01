package am.learn.kafka.retrybackoff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RetryBackoffConsumer {

    private final AtomicInteger attempt = new AtomicInteger(0);

    @KafkaListener(topics = RetryBackoffConfig.TOPIC, groupId = "retrybackoff-group",
            containerFactory = RetryBackoffConfig.FACTORY)
    public void listen(String message) {
        int n = attempt.incrementAndGet();
        log.info("[retrybackoff] <- attempt={} msg={}", n, message);
        if (message != null && message.startsWith("fail") && n < 4) {
            throw new IllegalStateException("transient failure, attempt " + n);
        }
        log.info("[retrybackoff] processed after {} attempt(s)", n);
        attempt.set(0);
    }
}
