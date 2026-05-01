package am.learn.realworld.quorum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuorumProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("[quorum] -> {}", message);
        rabbitTemplate.convertAndSend(QuorumConfig.QUEUE, message);
    }
}
