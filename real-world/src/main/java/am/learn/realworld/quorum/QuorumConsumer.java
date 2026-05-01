package am.learn.realworld.quorum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuorumConsumer {

    @RabbitListener(queues = QuorumConfig.QUEUE)
    public void listen(String message) {
        log.info("[quorum] <- received from quorum queue: {}", message);
        if (message != null && message.startsWith("fail")) {
            throw new IllegalStateException("simulated failure - delivery count increments");
        }
        log.info("[quorum] processed ok: {}", message);
    }
}
