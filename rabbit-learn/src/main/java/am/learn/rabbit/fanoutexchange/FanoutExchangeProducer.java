package am.learn.rabbit.fanoutexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FanoutExchangeProducer {

    private final RabbitTemplate rabbitTemplate;

    public void broadcast(String message) {
        log.info("[fanout] -> broadcasting: {}", message);
        // routing key is ignored by fanout exchange
        rabbitTemplate.convertAndSend(FanoutExchangeConfig.EXCHANGE, "", message);
    }
}
