package am.learn.rabbit.observability;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/observability")
@RequiredArgsConstructor
public class ObservabilityController {

    private final ObservabilityProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "trace me") String message) {
        producer.send(message);
        return "sent: " + message + " (see /actuator/metrics/learn.rabbit.messages.sent)";
    }
}
