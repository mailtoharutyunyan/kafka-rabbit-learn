package am.learn.kafka.jsonserde;

import am.learn.kafka.common.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonSerdeConsumer {

    @KafkaListener(topics = JsonSerdeConfig.TOPIC, groupId = "json-group",
            containerFactory = JsonSerdeConfig.FACTORY)
    public void listen(OrderEvent event) {
        log.info("[jsonserde] <- {}", event);
    }
}
