package am.learn.kafka.partitioning;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/partitioning")
@RequiredArgsConstructor
public class PartitioningController {

    private final PartitioningProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam String key, @RequestParam String message) {
        producer.send(key, message);
        return "sent key=" + key;
    }
}
