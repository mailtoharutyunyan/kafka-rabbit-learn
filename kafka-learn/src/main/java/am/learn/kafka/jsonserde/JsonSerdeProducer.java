package am.learn.kafka.jsonserde;

import am.learn.kafka.common.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonSerdeProducer {

    private final KafkaTemplate<String, OrderEvent> template;

    public JsonSerdeProducer(@Qualifier(JsonSerdeConfig.TEMPLATE) KafkaTemplate<String, OrderEvent> template) {
        this.template = template;
    }

    public void send(OrderEvent event) {
        log.info("[jsonserde] -> {}", event);
        template.send(JsonSerdeConfig.TOPIC, event.getId(), event);
    }
}
