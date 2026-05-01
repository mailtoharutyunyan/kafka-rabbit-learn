package am.learn.realworld.kafkaconnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Kafka Connect runs as a SEPARATE process/container (not inside Spring Boot).
 * This controller serves as documentation — returns connector config examples.
 *
 * To run Kafka Connect, add to docker-compose:
 *   connect:
 *     image: confluentinc/cp-kafka-connect:7.7.1
 *     ports: ["8083:8083"]
 *     environment:
 *       CONNECT_BOOTSTRAP_SERVERS: kafka:9092
 *       CONNECT_REST_PORT: 8083
 *       ...
 *
 * Then POST connector configs to http://localhost:8083/connectors
 */
@Slf4j
@RestController
@RequestMapping("/realworld/kafka-connect")
public class KafkaConnectController {

    @GetMapping("/jdbc-source-example")
    public Map<String, Object> jdbcSourceExample() {
        return Map.of(
                "name", "jdbc-source-orders",
                "description", "Polls DB orders table every 5s → publishes to Kafka topic",
                "config", Map.of(
                        "connector.class", "io.confluent.connect.jdbc.JdbcSourceConnector",
                        "connection.url", "jdbc:postgresql://localhost:5432/mydb",
                        "connection.user", "postgres",
                        "connection.password", "secret",
                        "table.whitelist", "orders",
                        "mode", "incrementing",
                        "incrementing.column.name", "id",
                        "topic.prefix", "connect.jdbc.",
                        "poll.interval.ms", "5000"
                ),
                "result_topic", "connect.jdbc.orders"
        );
    }

    @GetMapping("/elasticsearch-sink-example")
    public Map<String, Object> elasticSinkExample() {
        return Map.of(
                "name", "elastic-sink-orders",
                "description", "Reads from Kafka topic → indexes into Elasticsearch",
                "config", Map.of(
                        "connector.class", "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
                        "topics", "connect.jdbc.orders",
                        "connection.url", "http://elasticsearch:9200",
                        "type.name", "_doc",
                        "key.ignore", "true",
                        "schema.ignore", "true"
                )
        );
    }

    @GetMapping("/file-source-example")
    public Map<String, Object> fileSourceExample() {
        return Map.of(
                "name", "file-source-demo",
                "description", "Reads lines from a file → publishes each line to Kafka topic",
                "config", Map.of(
                        "connector.class", "org.apache.kafka.connect.file.FileStreamSourceConnector",
                        "topic", "connect.file.lines",
                        "file", "/tmp/test-input.txt"
                ),
                "how_to_use", "POST this config to http://localhost:8083/connectors with Connect running"
        );
    }
}
