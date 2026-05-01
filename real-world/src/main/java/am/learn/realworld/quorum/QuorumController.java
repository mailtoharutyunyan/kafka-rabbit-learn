package am.learn.realworld.quorum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realworld/quorum")
@RequiredArgsConstructor
public class QuorumController {

    private final QuorumProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "reliable-msg") String message) {
        producer.send(message);
        return "sent to quorum queue: " + message + " (RAFT-replicated, delivery-limit=5)";
    }
}
