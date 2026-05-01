# kafka-rabbit-learn

Hands-on learning project for **Apache Kafka** and **RabbitMQ** with Spring Boot — from basics to production patterns.

**Author:** Arayik Harutyunyan

## Modules

| Module | Port | Tech |
|--------|------|------|
| [`kafka-learn`](./kafka-learn) | 8081 | Apache Kafka (spring-kafka + kafka-streams) |
| [`rabbit-learn`](./rabbit-learn) | 8082 | RabbitMQ (spring-amqp) |

## Quick Start

```bash
# 1. Start brokers
docker compose up -d

# 2. Run apps (in separate terminals)
./mvnw -pl kafka-learn spring-boot:run
./mvnw -pl rabbit-learn spring-boot:run

# 3. Trigger examples via Postman or curl
curl -X POST "http://localhost:8081/kafka/basics/send?message=hello"
curl -X POST "http://localhost:8082/rabbit/basics/send?message=hello"
```

## UIs

- **Kafka UI:** http://localhost:8080
- **RabbitMQ Management:** http://localhost:15672 (guest / guest)

## Documentation (Russian)

- [Kafka Guide](./docs/kafka-guide-ru.md)
- [RabbitMQ Guide](./docs/rabbitmq-guide-ru.md)
- [Kafka vs RabbitMQ — Comparison](./docs/comparison-kafka-vs-rabbitmq-ru.md)

## Postman Collection

Import `docs/kafka-rabbit-learn.postman_collection.json` into Postman to trigger all endpoints.

## Topics Covered

**Kafka:** basics, partitioning, consumer groups, manual ack, dead-letter topics, JSON serde, Kafka Streams, transactions, idempotency, retry with backoff, observability

**RabbitMQ:** basics, work queues, direct/topic/fanout/headers exchanges, RPC, dead-letter queues, manual ack, retry with backoff, publisher confirms, idempotency, observability

## Requirements

- Java 21+
- Docker & Docker Compose
