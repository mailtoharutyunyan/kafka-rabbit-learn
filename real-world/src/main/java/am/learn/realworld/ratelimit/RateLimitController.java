package am.learn.realworld.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realworld/ratelimit")
@RequiredArgsConstructor
public class RateLimitController {

    private final RateLimitProducer producer;

    @PostMapping("/burst")
    public String burst(@RequestParam(defaultValue = "20") int count) {
        producer.sendBurst(count);
        return "sent " + count + " messages instantly — consumer processes max 3/sec (watch logs for throttling)";
    }
}
