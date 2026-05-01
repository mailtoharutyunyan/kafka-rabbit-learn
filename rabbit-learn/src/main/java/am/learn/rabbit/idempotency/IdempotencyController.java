package am.learn.rabbit.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/idempotency")
@RequiredArgsConstructor
public class IdempotencyController {

    private final IdempotencyProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam String id, @RequestParam(defaultValue = "payload") String payload) {
        producer.send(id, payload);
        return "sent id=" + id + " (repeat same id to see dedup)";
    }
}
