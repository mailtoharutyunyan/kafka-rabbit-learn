package am.learn.rabbit.directexchange;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/direct")
@RequiredArgsConstructor
public class DirectExchangeController {

    private final DirectExchangeProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "info") String key,
                       @RequestParam(defaultValue = "hello") String message) {
        producer.send(key, message);
        return "sent key=" + key;
    }
}
