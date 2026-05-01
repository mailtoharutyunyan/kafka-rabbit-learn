package am.learn.rabbit.directexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectExchangeProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String routingKey, String message) {
        log.info("[direct] -> key={} message={}", routingKey, message);
        rabbitTemplate.convertAndSend(DirectExchangeConfig.EXCHANGE, routingKey, message);
    }
}
