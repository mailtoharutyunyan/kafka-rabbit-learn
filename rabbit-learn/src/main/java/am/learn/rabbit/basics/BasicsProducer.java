package am.learn.rabbit.basics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicsProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("[basics] -> {}", message);
        // Default exchange + routing-key == queue name
        rabbitTemplate.convertAndSend(BasicsConfig.QUEUE, message);
    }
}
