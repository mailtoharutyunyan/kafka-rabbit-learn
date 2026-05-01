package am.learn.practical.multitenant;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MultiTenantConsumer {

    // Kafka: each tenant has its own topic
    @KafkaListener(topics = "learn.mt.tenant-a", groupId = "mt-kafka-a")
    public void kafkaTenantA(ConsumerRecord<String, String> r) {
        log.info("[mt-kafka] <- TENANT-A: {}", r.value());
    }

    @KafkaListener(topics = "learn.mt.tenant-b", groupId = "mt-kafka-b")
    public void kafkaTenantB(ConsumerRecord<String, String> r) {
        log.info("[mt-kafka] <- TENANT-B: {}", r.value());
    }

    // RabbitMQ: per-tenant queue via routing key pattern
    @RabbitListener(queues = MultiTenantConfig.RABBIT_QUEUE_TENANT_A)
    public void rabbitTenantA(String msg) {
        log.info("[mt-rabbit] <- TENANT-A queue: {}", msg);
    }

    @RabbitListener(queues = MultiTenantConfig.RABBIT_QUEUE_TENANT_B)
    public void rabbitTenantB(String msg) {
        log.info("[mt-rabbit] <- TENANT-B queue: {}", msg);
    }

    // Audit: receives ALL tenant events
    @RabbitListener(queues = MultiTenantConfig.RABBIT_QUEUE_ALL)
    public void rabbitAll(String msg) {
        log.info("[mt-rabbit] <- ALL-TENANTS audit: {}", msg);
    }
}
