package am.learn.kafka.streams;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/streams")
@RequiredArgsConstructor
public class StreamsController {

    private final StreamsProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "hello kafka streams") String line) {
        producer.send(line);
        return "sent: " + line;
    }
}
