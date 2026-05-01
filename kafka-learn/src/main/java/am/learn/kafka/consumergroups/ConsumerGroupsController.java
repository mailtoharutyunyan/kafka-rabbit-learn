package am.learn.kafka.consumergroups;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/consumergroups")
@RequiredArgsConstructor
public class ConsumerGroupsController {

    private final ConsumerGroupsProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "5") int count) {
        for (int i = 0; i < count; i++) {
            producer.send("msg-" + i);
        }
        return "sent " + count + " messages";
    }
}
