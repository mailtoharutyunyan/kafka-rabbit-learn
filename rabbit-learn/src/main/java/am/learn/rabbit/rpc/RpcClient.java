package am.learn.rabbit.rpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RpcClient {

    private final RabbitTemplate rabbitTemplate;

    public String call(String request) {
        log.info("[rpc] -> request: {}", request);
        Object response = rabbitTemplate.convertSendAndReceive(RpcConfig.QUEUE, request);
        log.info("[rpc] <- response: {}", response);
        return response == null ? "<timeout>" : response.toString();
    }
}
