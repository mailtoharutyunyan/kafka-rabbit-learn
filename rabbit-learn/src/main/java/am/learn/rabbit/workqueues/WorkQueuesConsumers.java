package am.learn.rabbit.workqueues;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkQueuesConsumers {

    // Two competing consumers on the same queue - each message goes to ONE of them (round-robin).
    @RabbitListener(queues = WorkQueuesConfig.QUEUE, concurrency = "2")
    public void worker(String task) {
        log.info("[worker-{}] processing: {}", Thread.currentThread().getName(), task);
        try {
            Thread.sleep(task.length() * 100L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[worker-{}] done: {}", Thread.currentThread().getName(), task);
    }
}
