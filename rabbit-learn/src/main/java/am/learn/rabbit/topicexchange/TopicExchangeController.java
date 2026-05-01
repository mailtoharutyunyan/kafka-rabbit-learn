package am.learn.rabbit.topicexchange;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/topic")
@RequiredArgsConstructor
public class TopicExchangeController {

    private final TopicExchangeProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "order.created") String key,
                       @RequestParam(defaultValue = "order 42") String message) {
        producer.send(key, message);
        return "sent key=" + key + " (try: order.created, order.shipped, user.created, user.deleted)";
    }
}
