package am.learn.practical.delayed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DelayedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendDelayed(String message, long delayMs) {
        log.info("[delayed] -> message='{}' delay={}ms", message, delayMs);
        rabbitTemplate.send(DelayedConfig.WAIT_EXCHANGE, DelayedConfig.WAIT_QUEUE,
                MessageBuilder.withBody(message.getBytes())
                        .setExpiration(String.valueOf(delayMs))
                        .build());
    }
}
