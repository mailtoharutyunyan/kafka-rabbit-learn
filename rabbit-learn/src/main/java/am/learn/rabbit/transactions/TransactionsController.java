package am.learn.rabbit.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "confirm-me") String message,
                       @RequestParam(defaultValue = "false") boolean toMissingQueue) {
        String id = producer.sendWithConfirm(message, toMissingQueue);
        return "sent id=" + id + " (watch logs for ACK/NACK/RETURNED)";
    }
}
