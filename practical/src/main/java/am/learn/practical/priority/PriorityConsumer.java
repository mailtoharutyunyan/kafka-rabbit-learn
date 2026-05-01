package am.learn.practical.priority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriorityConsumer {

    @RabbitListener(queues = PriorityConfig.QUEUE)
    public void listen(Message message) {
        String body = new String(message.getBody());
        Integer priority = message.getMessageProperties().getPriority();
        log.info("[priority] <- body='{}' priority={}", body, priority);
    }
}
