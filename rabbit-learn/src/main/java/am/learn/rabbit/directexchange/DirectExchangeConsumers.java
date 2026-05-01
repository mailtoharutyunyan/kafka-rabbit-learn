package am.learn.rabbit.directexchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectExchangeConsumers {

    @RabbitListener(queues = DirectExchangeConfig.QUEUE_INFO)
    public void info(String message) {
        log.info("[direct.info] <- {}", message);
    }

    @RabbitListener(queues = DirectExchangeConfig.QUEUE_ERROR)
    public void error(String message) {
        log.error("[direct.error] <- {}", message);
    }
}
