package am.learn.kafka.jsonserde;

import am.learn.kafka.common.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/kafka/jsonserde")
@RequiredArgsConstructor
public class JsonSerdeController {

    private final JsonSerdeProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "alice") String customer,
                       @RequestParam(defaultValue = "99.90") BigDecimal amount) {
        OrderEvent event = OrderEvent.builder()
                .id(UUID.randomUUID().toString())
                .customer(customer)
                .amount(amount)
                .createdAt(Instant.now())
                .build();
        producer.send(event);
        return "sent order " + event.getId();
    }
}
