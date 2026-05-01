package am.learn.kafka.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BasicsConsumer {

    @KafkaListener(topics = BasicsConfig.TOPIC, groupId = "basics-group")
    public void listen(String message) {
        log.info("[basics] <- received: {}", message);
    }
}
