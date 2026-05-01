package am.learn.practical.delayed;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practical/delayed")
@RequiredArgsConstructor
public class DelayedController {

    private final DelayedProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "delayed-task") String message,
                       @RequestParam(defaultValue = "5000") long delayMs) {
        producer.sendDelayed(message, delayMs);
        return "sent: '" + message + "' will be processed after " + delayMs + "ms";
    }
}
