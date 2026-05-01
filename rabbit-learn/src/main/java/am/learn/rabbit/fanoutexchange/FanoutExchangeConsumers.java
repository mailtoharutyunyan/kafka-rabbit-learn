package am.learn.rabbit.fanoutexchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FanoutExchangeConsumers {

    @RabbitListener(queues = FanoutExchangeConfig.QUEUE_A)
    public void a(String message) { log.info("[fanout.A] <- {}", message); }

    @RabbitListener(queues = FanoutExchangeConfig.QUEUE_B)
    public void b(String message) { log.info("[fanout.B] <- {}", message); }
}
