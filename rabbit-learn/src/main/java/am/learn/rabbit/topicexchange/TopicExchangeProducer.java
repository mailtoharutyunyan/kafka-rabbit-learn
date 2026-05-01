package am.learn.rabbit.topicexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicExchangeProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String routingKey, String message) {
        log.info("[topic] -> key={} message={}", routingKey, message);
        rabbitTemplate.convertAndSend(TopicExchangeConfig.EXCHANGE, routingKey, message);
    }
}
