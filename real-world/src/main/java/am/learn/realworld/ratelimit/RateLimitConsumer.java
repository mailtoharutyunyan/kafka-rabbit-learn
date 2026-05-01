package am.learn.realworld.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate-limited consumer: sleeps between messages to limit processing rate.
 * In production, use a Semaphore, Guava RateLimiter, or Resilience4j.
 */
@Slf4j
@Component
public class RateLimitConsumer {

    private static final int MAX_PER_SECOND = 3;
    private static final long DELAY_MS = 1000L / MAX_PER_SECOND;
    private final AtomicInteger processed = new AtomicInteger(0);

    @KafkaListener(topics = RateLimitConfig.TOPIC, containerFactory = RateLimitConfig.FACTORY)
    public void listen(String message) throws InterruptedException {
        int n = processed.incrementAndGet();
        log.info("[ratelimit] <- #{} processing: {} (max {}/sec, delay={}ms)", n, message, MAX_PER_SECOND, DELAY_MS);
        Thread.sleep(DELAY_MS);
    }
}
