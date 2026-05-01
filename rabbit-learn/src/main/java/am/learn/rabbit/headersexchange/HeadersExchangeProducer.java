package am.learn.rabbit.headersexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeadersExchangeProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(Map<String, Object> headers, String body) {
        log.info("[headers] -> headers={} body={}", headers, body);
        org.springframework.amqp.core.MessageBuilder mb = MessageBuilder.withBody(body.getBytes());
        headers.forEach(mb::setHeader);
        rabbitTemplate.send(HeadersExchangeConfig.EXCHANGE, "", mb.build());
    }
}
