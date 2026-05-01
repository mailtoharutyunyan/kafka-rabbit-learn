package am.learn.rabbit.retrybackoff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RetryBackoffConsumer {

    private final AtomicInteger attempt = new AtomicInteger(0);

    @RabbitListener(queues = RetryBackoffConfig.QUEUE, containerFactory = RetryBackoffConfig.FACTORY)
    public void listen(String message) {
        int n = attempt.incrementAndGet();
        log.info("[retrybackoff] <- attempt={} msg={}", n, message);
        if (message != null && message.startsWith("fail") && n < 3) {
            throw new IllegalStateException("transient failure, attempt " + n);
        }
        log.info("[retrybackoff] processed after {} attempt(s)", n);
        attempt.set(0);
    }
}
