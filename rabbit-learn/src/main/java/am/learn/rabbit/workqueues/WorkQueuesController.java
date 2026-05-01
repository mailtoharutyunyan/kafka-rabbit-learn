package am.learn.rabbit.workqueues;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/workqueues")
@RequiredArgsConstructor
public class WorkQueuesController {

    private final WorkQueuesProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "5") int count) {
        for (int i = 0; i < count; i++) {
            producer.send("task-" + i + "-" + "x".repeat(i + 1));
        }
        return "sent " + count + " tasks";
    }
}
