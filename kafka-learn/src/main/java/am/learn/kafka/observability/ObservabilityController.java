package am.learn.kafka.observability;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/observability")
@RequiredArgsConstructor
public class ObservabilityController {

    private final ObservabilityProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "trace me") String message) {
        producer.send(message);
        return "sent: " + message + " (check metrics at /actuator/metrics/learn.kafka.messages.sent)";
    }
}
