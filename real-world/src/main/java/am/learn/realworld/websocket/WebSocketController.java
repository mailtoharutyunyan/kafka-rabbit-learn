package am.learn.realworld.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/realworld/websocket")
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "New order received!") String message) {
        producer.sendNotification(message);
        return "published to Kafka → will be pushed to WebSocket subscribers at /topic/notifications";
    }
}
