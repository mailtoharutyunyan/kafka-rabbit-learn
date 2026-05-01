package am.learn.kafka.dlt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DltConsumer {

    @KafkaListener(topics = DltConfig.TOPIC, groupId = "dlt-group",
            containerFactory = DltConfig.FACTORY)
    public void listen(String message) {
        log.info("[dlt] <- received: {}", message);
        if (message != null && message.startsWith("poison")) {
            throw new IllegalStateException("poison pill: " + message);
        }
        log.info("[dlt] processed ok");
    }

    @KafkaListener(topics = DltConfig.DLT_TOPIC, groupId = "dlt-dead-group")
    public void deadLetter(String message) {
        log.warn("[dlt.DLT] >>> dead-letter: {}", message);
    }
}
