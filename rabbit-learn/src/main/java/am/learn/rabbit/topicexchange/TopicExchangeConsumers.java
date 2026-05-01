package am.learn.rabbit.topicexchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicExchangeConsumers {

    @RabbitListener(queues = TopicExchangeConfig.QUEUE_ORDERS)
    public void orders(String message) {
        log.info("[topic.orders] <- {}", message);
    }

    @RabbitListener(queues = TopicExchangeConfig.QUEUE_USER_CREATED)
    public void userCreated(String message) {
        log.info("[topic.userCreated] <- {}", message);
    }
}
