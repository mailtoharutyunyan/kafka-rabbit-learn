package am.learn.kafka.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "false") boolean rollback) {
        try {
            producer.sendAtomic("msg-1", "msg-2", rollback);
            return "committed";
        } catch (RuntimeException e) {
            return "aborted: " + e.getMessage();
        }
    }
}
