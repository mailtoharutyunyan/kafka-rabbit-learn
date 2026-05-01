package am.learn.kafka.retrybackoff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryBackoffProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        log.info("[retrybackoff] -> {}", message);
        kafkaTemplate.send(RetryBackoffConfig.TOPIC, message);
    }
}
