package am.learn.rabbit.dlq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DlqProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("[dlq] -> {}", message);
        rabbitTemplate.convertAndSend(DlqConfig.EXCHANGE, DlqConfig.ROUTING_KEY, message);
    }
}
