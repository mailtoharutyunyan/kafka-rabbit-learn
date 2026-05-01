package am.learn.practical.delayed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DelayedConsumer {

    @RabbitListener(queues = DelayedConfig.PROCESS_QUEUE)
    public void listen(String message) {
        log.info("[delayed] <- processed after delay: {}", message);
    }
}
