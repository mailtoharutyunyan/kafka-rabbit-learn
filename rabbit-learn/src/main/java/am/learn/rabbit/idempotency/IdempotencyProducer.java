package am.learn.rabbit.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String messageId, String payload) {
        log.info("[idempotency] -> id={} payload={}", messageId, payload);
        rabbitTemplate.send("", IdempotencyConfig.QUEUE,
                MessageBuilder.withBody(payload.getBytes())
                        .setMessageId(messageId)
                        .build());
    }
}
