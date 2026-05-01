package am.learn.practical.priority;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practical/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "task") String message,
                       @RequestParam(defaultValue = "5") int priority) {
        producer.send(message, priority);
        return "sent: '" + message + "' priority=" + priority;
    }

    @PostMapping("/demo")
    public String demo() {
        producer.send("low-priority-task", 1);
        producer.send("medium-priority-task", 5);
        producer.send("high-priority-task", 10);
        producer.send("another-low", 2);
        producer.send("critical-task", 10);
        return "sent 5 messages with different priorities — watch consumer order in logs";
    }
}
