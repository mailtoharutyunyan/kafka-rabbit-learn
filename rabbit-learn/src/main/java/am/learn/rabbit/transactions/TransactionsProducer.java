package am.learn.rabbit.transactions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsProducer {

    private final RabbitTemplate rabbitTemplate;

    public String sendWithConfirm(String message, boolean toMissingQueue) {
        String id = UUID.randomUUID().toString();
        CorrelationData correlation = new CorrelationData(id);
        String queue = toMissingQueue ? "no-such-queue" : TransactionsConfig.QUEUE;
        log.info("[tx] -> id={} queue={} message={}", id, queue, message);
        rabbitTemplate.convertAndSend("", queue, message, correlation);
        return id;
    }
}
