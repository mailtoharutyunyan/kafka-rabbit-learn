package am.learn.realworld.ratelimit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendBurst(int count) {
        log.info("[ratelimit] -> sending {} messages (burst)", count);
        for (int i = 0; i < count; i++) {
            kafkaTemplate.send(RateLimitConfig.TOPIC, "burst-msg-" + i);
        }
    }
}
