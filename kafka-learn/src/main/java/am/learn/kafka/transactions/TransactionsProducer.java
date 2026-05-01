package am.learn.kafka.transactions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransactionsProducer {

    private final KafkaTemplate<String, String> template;

    public TransactionsProducer(@Qualifier(TransactionsConfig.TEMPLATE) KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @Transactional(TransactionsConfig.TX_MANAGER)
    public void sendAtomic(String first, String second, boolean rollback) {
        log.info("[tx] -> sending atomic pair: {} / {} rollback={}", first, second, rollback);
        template.send(TransactionsConfig.TOPIC, first);
        template.send(TransactionsConfig.TOPIC, second);
        if (rollback) {
            throw new IllegalStateException("simulated failure - transaction will abort");
        }
    }
}
