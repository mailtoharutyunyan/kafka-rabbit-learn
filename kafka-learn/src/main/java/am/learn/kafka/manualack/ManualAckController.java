package am.learn.kafka.manualack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/manualack")
@RequiredArgsConstructor
public class ManualAckController {

    private final ManualAckProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "commit-me") String message) {
        producer.send(message);
        return "sent: " + message;
    }
}
