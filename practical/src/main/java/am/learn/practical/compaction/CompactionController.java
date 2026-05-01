package am.learn.practical.compaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/practical/compaction")
@RequiredArgsConstructor
public class CompactionController {

    private final CompactionProducer producer;

    @PostMapping("/upsert")
    public String upsert(@RequestParam String userId, @RequestParam String profile) {
        producer.upsert(userId, profile);
        return "upserted key=" + userId;
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String userId) {
        producer.delete(userId);
        return "tombstone sent for key=" + userId + " (will be removed during compaction)";
    }

    @PostMapping("/demo")
    public String demo() {
        producer.upsert("user-1", "{\"name\":\"Alice\",\"age\":25}");
        producer.upsert("user-2", "{\"name\":\"Bob\",\"age\":30}");
        producer.upsert("user-1", "{\"name\":\"Alice\",\"age\":26}");
        producer.upsert("user-3", "{\"name\":\"Carol\",\"age\":28}");
        producer.upsert("user-2", "{\"name\":\"Bob\",\"age\":31}");
        producer.delete("user-3");
        return "sent: user-1 (x2), user-2 (x2), user-3 (upsert + tombstone). After compaction only latest per key remains.";
    }
}
