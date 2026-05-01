package am.learn.rabbit.rpc;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {

    public static final String QUEUE = "learn.rpc.requests";

    @Bean
    Queue rpcQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}
