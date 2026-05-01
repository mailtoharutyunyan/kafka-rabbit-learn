<div align="center">

# kafka-rabbit-learn

### A Complete Guide to Message Brokers with Spring Boot

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-7.7-black?logo=apachekafka)](https://kafka.apache.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-orange?logo=rabbitmq)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://docs.docker.com/compose/)

**30+ hands-on examples** — from "Hello World" to production-grade patterns like Saga, Outbox, and Kafka Streams.

*Author: **Arayik Harutyunyan***

---

</div>

## Architecture Overview

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                            docker compose up -d                                │
│                                                                               │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌──────────────┐  │
│  │   Kafka     │    │  Kafka UI   │    │  RabbitMQ   │    │ RabbitMQ UI  │  │
│  │  :9092      │    │  :8080      │    │  :5672      │    │   :15672     │  │
│  └─────────────┘    └─────────────┘    └─────────────┘    └──────────────┘  │
└───────────────────────────────────────────────────────────────────────────────┘
         ▲                                        ▲
         │                                        │
┌────────┴────────────────────────────────────────┴─────────────────────────────┐
│                          Spring Boot Applications                              │
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐ │
│  │ kafka-learn  │  │ rabbit-learn │  │  practical   │  │   real-world     │ │
│  │   :8081      │  │   :8082      │  │   :8083      │  │    :8084         │ │
│  │              │  │              │  │              │  │                  │ │
│  │ 11 examples  │  │ 13 examples  │  │ 6 patterns   │  │ 7 scenarios      │ │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────────┘ │
└───────────────────────────────────────────────────────────────────────────────┘
```

## Modules

| Module | Port | Description |
|--------|------|-------------|
| [`kafka-learn`](./kafka-learn) | 8081 | Core Kafka patterns: producer/consumer, partitioning, consumer groups, manual ack, DLT, JSON serde, Kafka Streams, transactions, idempotency, retry, observability |
| [`rabbit-learn`](./rabbit-learn) | 8082 | Core RabbitMQ patterns: queues, direct/topic/fanout/headers exchanges, RPC, DLQ, manual ack, retry, publisher confirms, idempotency, observability |
| [`practical`](./practical) | 8083 | Production patterns: delayed messages, priority queues, compacted topics, batch consuming, transactional outbox, multi-tenant |
| [`real-world`](./real-world) | 8084 | Real-world scenarios: WebSocket push, Saga choreography, consumer lag alerting, rate limiting, quorum queues, Kafka Connect, Spring Cloud Stream |

## Quick Start

```bash
# 1. Start infrastructure
docker compose up -d

# 2. Run any module
./mvnw -pl kafka-learn spring-boot:run      # port 8081
./mvnw -pl rabbit-learn spring-boot:run     # port 8082
./mvnw -pl practical spring-boot:run        # port 8083
./mvnw -pl real-world spring-boot:run       # port 8084

# 3. Hit an endpoint
curl -X POST "http://localhost:8081/kafka/basics/send?message=hello"
```

## Management UIs

| UI | URL | Credentials |
|----|-----|-------------|
| Kafka UI | http://localhost:8080 | — |
| RabbitMQ Management | http://localhost:15672 | guest / guest |
| WebSocket Demo | http://localhost:8084/websocket-test.html | — |
| H2 Console (outbox) | http://localhost:8083/h2-console | sa / (empty) |

## Learning Path

```
Level 1: Basics                    Level 2: Patterns                Level 3: Production
─────────────────                  ──────────────────               ───────────────────
kafka-learn/basics/                kafka-learn/dlt/                 practical/outbox/
rabbit-learn/basics/               kafka-learn/streams/             practical/compaction/
kafka-learn/partitioning/          rabbit-learn/rpc/                real-world/saga/
rabbit-learn/directexchange/       rabbit-learn/dlq/                real-world/websocket/
kafka-learn/consumergroups/        kafka-learn/transactions/        real-world/consumerlag/
rabbit-learn/workqueues/           practical/delayed/               real-world/ratelimit/
kafka-learn/jsonserde/             practical/priority/              real-world/quorum/
rabbit-learn/fanoutexchange/       practical/batch/                 practical/multitenant/
```

## Topics Covered

<details>
<summary><b>Kafka (11 examples)</b></summary>

| # | Topic | What you learn |
|---|-------|---------------|
| 1 | `basics` | Simple producer → topic → consumer |
| 2 | `partitioning` | Key-based routing, ordering guarantees |
| 3 | `consumergroups` | Work splitting vs pub-sub |
| 4 | `manualack` | Offset commit control |
| 5 | `dlt` | Dead Letter Topic after N retries |
| 6 | `jsonserde` | JSON POJO serialization with trusted packages |
| 7 | `streams` | Kafka Streams word-count (stateful processing) |
| 8 | `transactions` | Atomic multi-send, read_committed isolation |
| 9 | `idempotency` | Consumer-side dedup via message-id |
| 10 | `retrybackoff` | Exponential backoff error handler |
| 11 | `observability` | Micrometer counters + MDC correlation propagation |

</details>

<details>
<summary><b>RabbitMQ (13 examples)</b></summary>

| # | Topic | What you learn |
|---|-------|---------------|
| 1 | `basics` | Default exchange, simple queue |
| 2 | `workqueues` | Competing consumers, round-robin |
| 3 | `directexchange` | Routing key exact match |
| 4 | `topicexchange` | Wildcard patterns (`*`, `#`) |
| 5 | `fanoutexchange` | Broadcast to all bound queues |
| 6 | `headersexchange` | Header-based routing (all/any) |
| 7 | `rpc` | Synchronous request/reply |
| 8 | `dlq` | Dead Letter Exchange + Queue |
| 9 | `manualack` | basicAck / basicNack / basicReject |
| 10 | `retrybackoff` | Spring Retry with exponential backoff |
| 11 | `transactions` | Publisher confirms + mandatory returns |
| 12 | `idempotency` | Dedup via messageId |
| 13 | `observability` | Micrometer + correlation headers |

</details>

<details>
<summary><b>Practical — Production Patterns (6 examples)</b></summary>

| # | Topic | What you learn |
|---|-------|---------------|
| 1 | `delayed` | Delayed messages via DLX + TTL trick |
| 2 | `priority` | Priority queues (x-max-priority) |
| 3 | `compaction` | Log compaction — keep latest per key |
| 4 | `batch` | Batch consuming (List<Record>) for throughput |
| 5 | `outbox` | Transactional outbox → guaranteed Kafka delivery |
| 6 | `multitenant` | Per-tenant isolation (topics + routing) |

</details>

<details>
<summary><b>Real-World Scenarios (7 examples)</b></summary>

| # | Topic | What you learn |
|---|-------|---------------|
| 1 | `websocket` | Kafka → WebSocket real-time push to browser |
| 2 | `saga` | Choreography-based distributed transaction |
| 3 | `consumerlag` | AdminClient lag monitoring + Micrometer gauge |
| 4 | `ratelimit` | Throttle consumer processing speed |
| 5 | `quorum` | RAFT-replicated queues, delivery-limit |
| 6 | `kafkaconnect` | Connector configs (JDBC source, ES sink) |
| 7 | `springcloudstream` | Broker-agnostic abstraction layer |

</details>

## Documentation

| Document | Language | Description |
|----------|----------|-------------|
| [kafka-guide-ru.md](./docs/kafka-guide-ru.md) | Russian | Complete Kafka theory + patterns |
| [rabbitmq-guide-ru.md](./docs/rabbitmq-guide-ru.md) | Russian | Complete RabbitMQ theory + patterns |
| [comparison-kafka-vs-rabbitmq-ru.md](./docs/comparison-kafka-vs-rabbitmq-ru.md) | Russian | When to use which + hybrid architecture |

## Postman Collection

Import [`docs/kafka-rabbit-learn.postman_collection.json`](./docs/kafka-rabbit-learn.postman_collection.json) into Postman — all 37 endpoints organized by module with descriptions.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.3.5 |
| Spring Kafka | 3.2.x |
| Spring AMQP | 3.2.x |
| Kafka (Confluent) | 7.7.1 (KRaft mode) |
| RabbitMQ | 3.13 |
| Lombok | managed |
| Micrometer + Prometheus | managed |
| H2 (outbox demo) | managed |
| WebSocket (STOMP) | managed |

## Requirements

- **Java 21+**
- **Docker & Docker Compose**
- **~2GB RAM** for all containers

---

<div align="center">

Made with Spring Boot, Kafka, and RabbitMQ

</div>
