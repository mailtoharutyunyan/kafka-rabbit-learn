package am.learn.practical.multitenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiTenantProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    public void sendToKafka(String tenantId, String event) {
        String topic = MultiTenantConfig.KAFKA_TOPIC_PREFIX + tenantId;
        log.info("[multitenant-kafka] -> tenant={} topic={} event={}", tenantId, topic, event);
        kafkaTemplate.send(topic, tenantId, event);
    }

    public void sendToRabbit(String tenantId, String eventType, String payload) {
        String routingKey = tenantId + "." + eventType;
        log.info("[multitenant-rabbit] -> routing={} payload={}", routingKey, payload);
        rabbitTemplate.convertAndSend(MultiTenantConfig.RABBIT_EXCHANGE, routingKey, payload);
    }
}
