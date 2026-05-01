package am.learn.rabbit.basics;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/basics")
@RequiredArgsConstructor
public class BasicsController {

    private final BasicsProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "hello rabbit") String message) {
        producer.send(message);
        return "sent: " + message;
    }
}
