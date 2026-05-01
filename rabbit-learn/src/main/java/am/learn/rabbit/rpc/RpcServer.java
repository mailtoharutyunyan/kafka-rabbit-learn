package am.learn.rabbit.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class RpcServer {

    @RabbitListener(queues = RpcConfig.QUEUE)
    public String handle(String request) {
        log.info("[rpc.server] <- request: {}", request);
        return request == null ? "" : request.toUpperCase(Locale.ROOT);
    }
}
