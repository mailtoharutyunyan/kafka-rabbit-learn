package am.learn.rabbit.retrybackoff;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/retrybackoff")
@RequiredArgsConstructor
public class RetryBackoffController {

    private final RetryBackoffProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "fail-then-succeed") String message) {
        producer.send(message);
        return "sent: " + message + " (prefix 'fail' to trigger retries)";
    }
}
