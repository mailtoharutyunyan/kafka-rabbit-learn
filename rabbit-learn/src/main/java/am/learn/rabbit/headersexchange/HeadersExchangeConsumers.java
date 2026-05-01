package am.learn.rabbit.headersexchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeadersExchangeConsumers {

    @RabbitListener(queues = HeadersExchangeConfig.QUEUE_PDF)
    public void pdf(Message message) {
        log.info("[headers.pdf] <- body={} headers={}",
                new String(message.getBody()), message.getMessageProperties().getHeaders());
    }

    @RabbitListener(queues = HeadersExchangeConfig.QUEUE_IMAGE)
    public void image(Message message) {
        log.info("[headers.image] <- body={} headers={}",
                new String(message.getBody()), message.getMessageProperties().getHeaders());
    }
}
