package am.learn.realworld.consumerlag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realworld/consumer-lag")
@RequiredArgsConstructor
public class ConsumerLagController {

    @GetMapping("/info")
    public String info() {
        return "Consumer lag is monitored every 5s. Check: GET /actuator/metrics/kafka.consumer.lag";
    }
}
