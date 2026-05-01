package am.learn.rabbit.manualack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/manualack")
@RequiredArgsConstructor
public class ManualAckController {

    private final ManualAckProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "ack-me") String message) {
        producer.send(message);
        return "sent: " + message + " (try: ack-me, nack-requeue, reject)";
    }
}
