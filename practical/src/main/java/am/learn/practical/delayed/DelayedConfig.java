package am.learn.practical.delayed;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Delayed messages using DLX + TTL trick.
 *
 * Flow: Producer → wait-queue (TTL, no consumers) → DLX → process-queue → Consumer
 *
 * The wait-queue holds messages until TTL expires, then dead-letters them to the process exchange.
 */
@Configuration
public class DelayedConfig {

    public static final String WAIT_EXCHANGE = "learn.delayed.wait";
    public static final String WAIT_QUEUE = "learn.delayed.wait";
    public static final String PROCESS_EXCHANGE = "learn.delayed.process";
    public static final String PROCESS_QUEUE = "learn.delayed.process";

    @Bean DirectExchange delayedWaitExchange() { return new DirectExchange(WAIT_EXCHANGE); }
    @Bean DirectExchange delayedProcessExchange() { return new DirectExchange(PROCESS_EXCHANGE); }

    @Bean
    Queue delayedWaitQueue() {
        return QueueBuilder.durable(WAIT_QUEUE)
                .withArgument("x-dead-letter-exchange", PROCESS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PROCESS_QUEUE)
                .build();
    }

    @Bean
    Queue delayedProcessQueue() {
        return QueueBuilder.durable(PROCESS_QUEUE).build();
    }

    @Bean
    Binding bindWait(DirectExchange delayedWaitExchange, Queue delayedWaitQueue) {
        return BindingBuilder.bind(delayedWaitQueue).to(delayedWaitExchange).with(WAIT_QUEUE);
    }

    @Bean
    Binding bindProcess(DirectExchange delayedProcessExchange, Queue delayedProcessQueue) {
        return BindingBuilder.bind(delayedProcessQueue).to(delayedProcessExchange).with(PROCESS_QUEUE);
    }
}
