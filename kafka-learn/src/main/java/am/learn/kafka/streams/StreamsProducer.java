package am.learn.kafka.streams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamsProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final List<String> SAMPLES = List.of(
            "kafka streams is fun",
            "learning kafka and rabbit",
            "streams aggregate events",
            "topic partition offset");

    public void send(String line) {
        log.info("[streams] -> {}", line);
        kafkaTemplate.send(StreamsConfig.INPUT_TOPIC, line);
    }

    @Scheduled(fixedDelay = 5000L, initialDelay = 10_000L)
    public void emitSample() {
        String line = SAMPLES.get(ThreadLocalRandom.current().nextInt(SAMPLES.size()));
        send(line);
    }
}
