package am.learn.rabbit.workqueues;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkQueuesProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String task) {
        log.info("[workqueues] -> {}", task);
        rabbitTemplate.convertAndSend(WorkQueuesConfig.QUEUE, task);
    }
}
