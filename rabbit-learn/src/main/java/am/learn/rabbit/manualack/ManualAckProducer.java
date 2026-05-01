package am.learn.rabbit.manualack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManualAckProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("[manualack] -> {}", message);
        rabbitTemplate.convertAndSend(ManualAckConfig.QUEUE, message);
    }
}
