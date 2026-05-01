package am.learn.rabbit.dlq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DlqConsumer {

    @RabbitListener(queues = DlqConfig.QUEUE)
    public void main(String message) {
        log.info("[dlq] <- {}", message);
        if (message != null && message.startsWith("poison")) {
            // nack + don't requeue => dead-letter-exchange routes to DLQ
            throw new AmqpRejectAndDontRequeueException("poison pill: " + message);
        }
        log.info("[dlq] processed ok");
    }

    @RabbitListener(queues = DlqConfig.DLQ)
    public void dead(String message) {
        log.warn("[dlq.dead] >>> {}", message);
    }
}
