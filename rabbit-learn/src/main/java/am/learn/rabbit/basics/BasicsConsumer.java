package am.learn.rabbit.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BasicsConsumer {

    @RabbitListener(queues = BasicsConfig.QUEUE)
    public void listen(String message) {
        log.info("[basics] <- {}", message);
    }
}
