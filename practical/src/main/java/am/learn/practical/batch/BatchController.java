package am.learn.practical.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practical/batch")
@RequiredArgsConstructor
public class BatchController {

    private final BatchProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "100") int count) {
        producer.sendBatch(count);
        return "sent " + count + " messages — consumer will receive them in batches (max 50 per poll)";
    }
}
