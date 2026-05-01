package am.learn.realworld.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = WebSocketKafkaConfig.TOPIC, groupId = "ws-push-group")
    public void listen(String message) {
        log.info("[ws] <- Kafka → pushing to WebSocket subscribers: {}", message);
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
