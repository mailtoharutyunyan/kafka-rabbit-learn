package am.learn.kafka.dlt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/dlt")
@RequiredArgsConstructor
public class DltController {

    private final DltProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "poison-1") String message) {
        producer.send(message);
        return "sent: " + message + " (prefix with 'poison' to trigger DLT)";
    }
}
