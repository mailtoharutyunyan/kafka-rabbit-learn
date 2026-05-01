package am.learn.rabbit.manualack;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ManualAckConsumer {

    @RabbitListener(queues = ManualAckConfig.QUEUE, containerFactory = ManualAckConfig.FACTORY)
    public void listen(Message message, Channel channel,
                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String body = new String(message.getBody());
        log.info("[manualack] <- tag={} body={}", tag, body);

        if ("nack-requeue".equalsIgnoreCase(body)) {
            log.warn("[manualack] nack+requeue");
            channel.basicNack(tag, false, true);
            return;
        }
        if ("reject".equalsIgnoreCase(body)) {
            log.warn("[manualack] reject (drop)");
            channel.basicReject(tag, false);
            return;
        }
        channel.basicAck(tag, false);
        log.info("[manualack] ack tag={}", tag);
    }
}
