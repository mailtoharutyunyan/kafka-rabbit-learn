package am.learn.rabbit.dlq;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/dlq")
@RequiredArgsConstructor
public class DlqController {

    private final DlqProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "poison-1") String message) {
        producer.send(message);
        return "sent: " + message + " (prefix 'poison' to route to DLQ)";
    }
}
