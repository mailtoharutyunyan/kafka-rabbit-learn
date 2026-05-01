package am.learn.kafka.basics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicsProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        log.info("[basics] -> sending: {}", message);
        kafkaTemplate.send(BasicsConfig.TOPIC, message);
    }
}
