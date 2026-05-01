package am.learn.realworld.springcloudstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Spring Cloud Stream abstracts away Kafka/RabbitMQ behind a binder.
 * Same code works with either broker — just swap the dependency.
 *
 * This module doesn't add the cloud-stream dependency to avoid conflicting
 * with our direct spring-kafka/spring-amqp usage. Instead, this controller
 * documents the pattern and shows the config needed.
 *
 * To use Spring Cloud Stream in YOUR project:
 *
 * 1. Add dependency: spring-cloud-stream-binder-kafka (or -rabbit)
 * 2. Define functional beans: Supplier, Consumer, Function
 * 3. Spring auto-wires them to topics/queues via application.yml
 *
 * The same Consumer<String> works with both Kafka and RabbitMQ —
 * just change the binder in config.
 */
@Slf4j
@RestController
@RequestMapping("/realworld/spring-cloud-stream")
public class SpringCloudStreamController {

    @GetMapping("/kafka-config-example")
    public Map<String, Object> kafkaExample() {
        return Map.of(
                "description", "Spring Cloud Stream with Kafka binder",
                "dependency", "org.springframework.cloud:spring-cloud-stream-binder-kafka",
                "java_code", """
                    @Bean
                    public Function<String, String> uppercase() {
                        return value -> value.toUpperCase();
                    }

                    @Bean
                    public Consumer<String> logger() {
                        return message -> log.info("received: {}", message);
                    }

                    @Bean
                    public Supplier<String> producer() {
                        return () -> "hello-" + Instant.now();
                    }
                    """,
                "application_yml", Map.of(
                        "spring.cloud.stream.bindings.uppercase-in-0.destination", "input-topic",
                        "spring.cloud.stream.bindings.uppercase-out-0.destination", "output-topic",
                        "spring.cloud.stream.bindings.logger-in-0.destination", "log-topic",
                        "spring.cloud.stream.bindings.producer-out-0.destination", "produced-topic",
                        "spring.cloud.stream.kafka.binder.brokers", "localhost:9092"
                )
        );
    }

    @GetMapping("/rabbit-config-example")
    public Map<String, Object> rabbitExample() {
        return Map.of(
                "description", "Same code with RabbitMQ binder — just swap dependency",
                "dependency", "org.springframework.cloud:spring-cloud-stream-binder-rabbit",
                "note", "The Java code stays EXACTLY the same! Only the binder + config changes.",
                "application_yml", Map.of(
                        "spring.cloud.stream.bindings.uppercase-in-0.destination", "input-exchange",
                        "spring.cloud.stream.bindings.uppercase-out-0.destination", "output-exchange",
                        "spring.cloud.stream.bindings.logger-in-0.destination", "log-exchange",
                        "spring.rabbitmq.host", "localhost",
                        "spring.rabbitmq.port", 5672
                ),
                "key_benefit", "Write once, run with any broker (Kafka, Rabbit, Kinesis, Solace, etc.)"
        );
    }
}
