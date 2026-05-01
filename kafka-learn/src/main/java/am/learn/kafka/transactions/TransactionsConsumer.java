package am.learn.kafka.transactions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionsConsumer {

    // read_committed - only sees messages from committed transactions.
    @KafkaListener(topics = TransactionsConfig.TOPIC, groupId = "tx-group",
            containerFactory = TransactionsConfig.FACTORY)
    public void listen(String message) {
        log.info("[tx] <- committed: {}", message);
    }
}
