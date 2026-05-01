package am.learn.rabbit.fanoutexchange;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/fanout")
@RequiredArgsConstructor
public class FanoutExchangeController {

    private final FanoutExchangeProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "breaking news") String message) {
        producer.broadcast(message);
        return "broadcast: " + message + " (both queues A and B should receive it)";
    }
}
