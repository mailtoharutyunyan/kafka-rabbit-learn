package am.learn.practical.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/practical/outbox")
@RequiredArgsConstructor
public class OutboxController {

    private final OutboxService service;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam(defaultValue = "alice") String customer,
                              @RequestParam(defaultValue = "99.90") String amount) {
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        service.createOrder(orderId, customer, amount);
        return "order " + orderId + " saved to DB + outbox (will be published to Kafka within 2s)";
    }
}
