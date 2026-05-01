package am.learn.realworld.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message) {
        log.info("[ws] -> publishing to Kafka: {}", message);
        kafkaTemplate.send(WebSocketKafkaConfig.TOPIC, message);
    }
}
