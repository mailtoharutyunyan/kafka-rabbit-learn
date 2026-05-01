package am.learn.rabbit.retrybackoff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryBackoffProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("[retrybackoff] -> {}", message);
        rabbitTemplate.convertAndSend(RetryBackoffConfig.QUEUE, message);
    }
}
