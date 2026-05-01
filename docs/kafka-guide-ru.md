# Apache Kafka — Полное руководство

## Что такое Kafka?

Apache Kafka — это распределённая платформа потоковой обработки данных. Изначально разработана в LinkedIn, сейчас — проект Apache Software Foundation. Kafka работает как **брокер сообщений** с гарантией порядка, отказоустойчивостью и горизонтальным масштабированием.

## Основные концепции

### Архитектура

```
┌─────────────────────────────────────────────────────────────────┐
│                        KAFKA CLUSTER                             │
│                                                                  │
│  ┌──────────┐   ┌──────────┐   ┌──────────┐                    │
│  │ Broker 0 │   │ Broker 1 │   │ Broker 2 │                    │
│  └──────────┘   └──────────┘   └──────────┘                    │
│        │              │              │                            │
│        ▼              ▼              ▼                            │
│  ┌──────────────────────────────────────┐                       │
│  │           Topic: orders              │                       │
│  │  ┌────────┐ ┌────────┐ ┌────────┐   │                       │
│  │  │ Part-0 │ │ Part-1 │ │ Part-2 │   │                       │
│  │  │ [0,1,2]│ │ [0,1]  │ │ [0,1,2,3]│ │                       │
│  │  └────────┘ └────────┘ └────────┘   │                       │
│  └──────────────────────────────────────┘                       │
└─────────────────────────────────────────────────────────────────┘
         ▲                                        │
         │                                        ▼
   ┌──────────┐                            ┌──────────────┐
   │ Producer │                            │Consumer Group│
   │          │                            │  C1  C2  C3  │
   └──────────┘                            └──────────────┘
```

### Topic (Топик)

Именованный канал для публикации сообщений. Аналог «таблицы» в БД или «папки» в файловой системе.

- Разделён на **партиции** (partitions) для параллелизма
- Сообщения хранятся на диске с настраиваемым TTL (retention)
- Сообщения **append-only** — не удаляются после прочтения

### Partition (Партиция)

Упорядоченная, неизменяемая последовательность записей.

- Каждая запись имеет уникальный **offset** (порядковый номер)
- Порядок гарантирован **внутри** одной партиции
- Ключ сообщения определяет, в какую партицию оно попадёт
- Одинаковый ключ → одна и та же партиция → гарантия порядка для этого ключа

### Producer (Производитель)

Публикует сообщения в топик.

```
Producer → [key: "user-123"] → Partitioner → Partition 2
```

**Основные настройки:**
- `acks=all` — ждать подтверждения от всех реплик (надёжно)
- `acks=1` — ждать только лидера (быстрее)
- `enable.idempotence=true` — exactly-once семантика на стороне продюсера
- `batch.size`, `linger.ms` — батчинг для throughput

### Consumer (Потребитель)

Читает сообщения из топика.

- Отслеживает свою позицию через **offset**
- Может перемотать offset назад для повторной обработки
- Управляет commit (подтверждением обработки) — авто или ручной

### Consumer Group (Группа потребителей)

```
Topic: orders (3 partitions)
Consumer Group: order-service

  Partition 0 ──→ Consumer A
  Partition 1 ──→ Consumer B
  Partition 2 ──→ Consumer C

  (каждое сообщение обрабатывается ОДНИМ потребителем в группе)
```

- Партиции распределяются между потребителями в группе
- Если потребителей больше, чем партиций — лишние простаивают
- Разные группы получают ВСЕ сообщения независимо (pub-sub)

### Offset и Commit

```
Partition: [msg0][msg1][msg2][msg3][msg4][msg5]
                              ▲              ▲
                              │              │
                    committed offset    latest offset
                    (обработано)        (доступно)
```

**Авто-commit**: Spring Kafka коммитит периодически (удобно, но при сбое — повторная обработка)

**Ручной commit**: вы контролируете, когда сообщение считается обработанным

## Паттерны (примеры в этом проекте)

### 1. Простой producer/consumer (`basics/`)
Базовая отправка и получение строковых сообщений.

### 2. Ключевое партиционирование (`partitioning/`)
Сообщения с одним ключом всегда попадают в одну партицию → гарантия порядка для данного ключа.

### 3. Consumer Groups (`consumergroups/`)
- `shared-group`: 2 потребителя делят работу (work queue)
- `audit-group`: отдельная группа получает ВСЁ (audit log)

### 4. Manual Ack (`manualack/`)
Явный контроль коммита offset. Можно не коммитить — сообщение будет перечитано при перезапуске.

### 5. Dead Letter Topic (`dlt/`)
```
Message → Consumer (fails) → Retry 1 → Retry 2 → DLT topic
```
Неудачные сообщения после N попыток перемещаются в отдельный топик для анализа.

### 6. JSON Serde (`jsonserde/`)
Типизированная сериализация/десериализация POJO через Jackson.

### 7. Kafka Streams (`streams/`)
```
Input topic → [flatMap → groupBy → count] → Output topic
```
Стриминговая обработка в реальном времени внутри Kafka (без внешних систем).

### 8. Транзакции (`transactions/`)
Атомарная отправка нескольких сообщений. Consumer с `isolation.level=read_committed` видит только зафиксированные.

### 9. Идемпотентный Consumer (`idempotency/`)
Дедупликация на стороне получателя по `message-id` в заголовке.

### 10. Retry + Backoff (`retrybackoff/`)
Экспоненциальный backoff при ошибках: 500ms → 1s → 2s → 4s → 5s (max).

### 11. Observability (`observability/`)
Micrometer-метрики `messages.sent`/`messages.received` + propagation `correlationId` через заголовки.

## Гарантии доставки

| Гарантия | Как достичь |
|----------|-------------|
| At-most-once | `acks=0`, авто-commit |
| At-least-once | `acks=all`, ручной commit после обработки |
| Exactly-once | Транзакции + `enable.idempotence=true` + `read_committed` |

## Когда использовать Kafka

- Высокий throughput (миллионы сообщений/сек)
- Event sourcing / event-driven архитектура
- Стриминговая обработка (Kafka Streams, ksqlDB)
- Log aggregation
- Change Data Capture (CDC)
- Микросервисная интеграция с сохранением порядка

## Spring Kafka — ключевые классы

| Класс | Назначение |
|-------|-----------|
| `KafkaTemplate` | Отправка сообщений |
| `@KafkaListener` | Получение сообщений |
| `ConcurrentKafkaListenerContainerFactory` | Конфигурация контейнера потребителя |
| `DefaultErrorHandler` | Обработка ошибок + DLT |
| `KafkaTransactionManager` | Транзакции |
| `StreamsBuilder` | Kafka Streams DSL |

## Продвинутые паттерны (модуль `practical/`)

### Compacted Topics (уплотнение)

```
Topic (cleanup.policy=compact):

До компакции:  [user-1:v1] [user-2:v1] [user-1:v2] [user-2:v2] [user-1:v3]
После:         [user-2:v2] [user-1:v3]
                    ↑ только последние значения на ключ
```

Kafka хранит только ПОСЛЕДНЕЕ значение для каждого ключа. Используется для:
- Справочников (user profiles, configs)
- KTable / state store
- Отправка `null` = tombstone (удаление ключа)

### Batch Consuming (пакетная обработка)

```
Kafka Broker:  [msg1][msg2][msg3]...[msg50]
                         │
               maxPollRecords=50
                         │
                         ▼
Consumer:  List<ConsumerRecord> (до 50 за poll)
```

Повышает throughput за счёт обработки пакетами вместо одного сообщения за раз.

### Transactional Outbox (паттерн "Исходящий ящик")

```
┌─────────────────────────────────┐
│ @Transactional                  │
│                                 │
│  1. Save Order to DB            │
│  2. Save OutboxEvent to DB      │ ← одна транзакция
│                                 │
└─────────────────────────────────┘
         │
    Poller (каждые 2 сек)
         │
         ▼
   Publish to Kafka → Mark as published
```

Гарантирует: если бизнес-данные сохранены — событие БУДЕТ опубликовано. Без 2PC.

### Multi-Tenant (мультитенантность)

```
Kafka:   learn.mt.tenant-a  →  Consumer A
         learn.mt.tenant-b  →  Consumer B

Rabbit:  Exchange → routing "tenant-a.#" → Queue A
                  → routing "tenant-b.#" → Queue B
                  → routing "#"           → Audit (ВСЕ)
```

Изоляция данных арендаторов через отдельные топики/очереди.
