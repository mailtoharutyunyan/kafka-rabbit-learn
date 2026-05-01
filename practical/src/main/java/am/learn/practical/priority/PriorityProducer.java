package am.learn.practical.priority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriorityProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message, int priority) {
        log.info("[priority] -> message='{}' priority={}", message, priority);
        rabbitTemplate.send("", PriorityConfig.QUEUE,
                MessageBuilder.withBody(message.getBytes())
                        .setPriority(priority)
                        .build());
    }
}
