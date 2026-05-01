package am.learn.rabbit.transactions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
public class TransactionsConfig {

    public static final String QUEUE = "learn.transactions";

    private final RabbitTemplate rabbitTemplate;

    public TransactionsConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    Queue txQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @PostConstruct
    void configureConfirmsAndReturns() {
        rabbitTemplate.setConfirmCallback((correlation, ack, cause) -> {
            if (ack) {
                log.info("[tx] confirm ACK id={}", correlation == null ? null : correlation.getId());
            } else {
                log.error("[tx] confirm NACK id={} cause={}",
                        correlation == null ? null : correlation.getId(), cause);
            }
        });
        rabbitTemplate.setReturnsCallback(returned ->
                log.warn("[tx] RETURNED message={} reply={}",
                        new String(returned.getMessage().getBody()), returned.getReplyText()));
        rabbitTemplate.setMandatory(true);
    }

    public CorrelationData newCorrelation(String id) {
        return new CorrelationData(id);
    }
}
