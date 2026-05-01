package am.learn.rabbit.transactions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionsConsumer {

    @RabbitListener(queues = TransactionsConfig.QUEUE)
    public void listen(String message) {
        log.info("[tx] <- {}", message);
    }
}
