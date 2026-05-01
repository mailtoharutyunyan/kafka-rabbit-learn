# RabbitMQ — Полное руководство

## Что такое RabbitMQ?

RabbitMQ — это брокер сообщений, реализующий протокол **AMQP 0-9-1**. Написан на Erlang, обеспечивает надёжную доставку сообщений с гибкой маршрутизацией.

## Основные концепции

### Архитектура

```
┌──────────────────────────────────────────────────────────────────┐
│                         RABBITMQ BROKER                            │
│                                                                    │
│  Producer ──→ Exchange ──routing──→ Queue ──→ Consumer             │
│                  │                    ▲                             │
│                  │    Binding         │                             │
│                  └────(rules)─────────┘                            │
│                                                                    │
└──────────────────────────────────────────────────────────────────┘
```

### Exchange (Обменник)

Принимает сообщения от продюсера и маршрутизирует их в очереди по правилам (bindings).

**Типы Exchange:**

```
┌─────────────────────────────────────────────────────────┐
│ DIRECT                                                   │
│                                                          │
│ Producer ──[routing_key="error"]──→ Exchange             │
│                                       │                  │
│                     routing_key="error" ──→ error-queue  │
│                     routing_key="info"  ──→ info-queue   │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ TOPIC                                                    │
│                                                          │
│ Producer ──[routing_key="order.created"]──→ Exchange     │
│                                              │           │
│                     pattern "order.*"    ──→ orders-q    │
│                     pattern "*.created" ──→ created-q    │
│                     pattern "#"         ──→ all-events-q │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ FANOUT                                                   │
│                                                          │
│ Producer ──→ Exchange ──→ Queue A                        │
│                       ──→ Queue B                        │
│                       ──→ Queue C                        │
│     (broadcast: ВСЕ очереди получают ВСЕ сообщения)     │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ HEADERS                                                  │
│                                                          │
│ Producer ──[headers: {format:pdf, type:report}]──→ Ex   │
│                                                    │     │
│        match ALL {format:pdf, type:report} ──→ pdf-q    │
│        match ANY {format:png} OR {format:jpg} ──→ img-q │
└─────────────────────────────────────────────────────────┘
```

### Queue (Очередь)

Буфер для хранения сообщений. Сообщения ожидают обработки потребителем.

- **Durable** — переживает перезапуск брокера
- **Exclusive** — только одно соединение, удаляется при отключении
- **Auto-delete** — удаляется, когда последний потребитель отключается

### Binding (Привязка)

Правило маршрутизации: связывает Exchange с Queue через routing key или headers.

### Message (Сообщение)

```
┌─────────────────────────────────────┐
│ Properties:                          │
│   - content-type                     │
│   - message-id                       │
│   - correlation-id                   │
│   - reply-to                         │
│   - delivery-mode (persistent/transient) │
│   - headers (custom key-value)       │
├─────────────────────────────────────┤
│ Body (payload): bytes                │
└─────────────────────────────────────┘
```

## Подтверждения (Acknowledgments)

### Consumer Ack

```
Broker ──deliver──→ Consumer
                        │
              process message
                        │
                   ack/nack/reject
                        │
                        ▼
            ┌─── basicAck ─────→ удалить из очереди
            ├─── basicNack(requeue=true) ──→ вернуть в очередь
            ├─── basicNack(requeue=false) ──→ DLQ (если настроен)
            └─── basicReject ──→ отклонить (1 сообщение)
```

### Publisher Confirms

```
Producer ──→ Broker
                │
          (записал на диск)
                │
          confirm ACK ──→ Producer (уверен: доставлено)
     или  confirm NACK ──→ Producer (проблема!)
```

## Паттерны (примеры в этом проекте)

### 1. Простая очередь (`basics/`)
Default exchange + routing_key == имя очереди.

### 2. Work Queues (`workqueues/`)
```
             ┌──→ Consumer 1 (обрабатывает msg-1, msg-3)
Queue ──────┤
             └──→ Consumer 2 (обрабатывает msg-2, msg-4)
```
Round-robin распределение. prefetch=1 для fair dispatch.

### 3. Direct Exchange (`directexchange/`)
Routing key точно совпадает с binding key. Фильтрация по типу (info/error).

### 4. Topic Exchange (`topicexchange/`)
Wildcard маршрутизация: `*` = одно слово, `#` = ноль или более слов.

### 5. Fanout Exchange (`fanoutexchange/`)
Broadcast — каждое сообщение копируется во все привязанные очереди.

### 6. Headers Exchange (`headersexchange/`)
Маршрутизация по заголовкам сообщения (x-match: all/any).

### 7. RPC (`rpc/`)
```
Client ──[request]──→ rpc-queue ──→ Server
Client ←─[response]─ reply-to-queue ←── Server
```
Синхронный запрос-ответ через RabbitMQ.

### 8. Dead Letter Queue (`dlq/`)
```
Message → Queue (reject/nack) → Dead Letter Exchange → DLQ
```
Неудачные сообщения автоматически перемещаются в отдельную очередь.

### 9. Manual Ack (`manualack/`)
- `basicAck` — подтвердить обработку
- `basicNack(requeue=true)` — вернуть в очередь (будет обработано снова)
- `basicReject(requeue=false)` — отбросить / направить в DLQ

### 10. Retry с Backoff (`retrybackoff/`)
Spring Retry interceptor: 4 попытки с экспоненциальным backoff (500ms → 1s → 2s → 4s).

### 11. Publisher Confirms (`transactions/`)
Подтверждение от брокера, что сообщение принято. + `mandatory=true` для возврата unroutable сообщений.

### 12. Идемпотентный Consumer (`idempotency/`)
Дедупликация по `messageId` в properties.

### 13. Observability (`observability/`)
Micrometer-метрики + propagation correlationId через headers.

## Гарантии доставки

| Гарантия | Как достичь |
|----------|-------------|
| At-most-once | auto-ack (не ждать обработки) |
| At-least-once | manual ack после обработки + persistent messages + durable queue |
| Exactly-once | Идемпотентный consumer + publisher confirms |

## Когда использовать RabbitMQ

- Сложная маршрутизация (exchange types, bindings)
- RPC / request-reply паттерн
- Приоритетные очереди
- TTL и отложенные сообщения (dead-letter + TTL)
- Низкая latency при умеренном throughput
- Протокол AMQP как стандарт (совместимость)
- Микросервисная интеграция с гибкой маршрутизацией

## Spring AMQP — ключевые классы

| Класс | Назначение |
|-------|-----------|
| `RabbitTemplate` | Отправка сообщений |
| `@RabbitListener` | Получение сообщений |
| `SimpleRabbitListenerContainerFactory` | Конфигурация контейнера |
| `Jackson2JsonMessageConverter` | JSON сериализация |
| `RetryInterceptorBuilder` | Retry с backoff |
| `CorrelationData` | Publisher confirms |
| `AmqpRejectAndDontRequeueException` | Направить в DLQ |

## Продвинутые паттерны (модуль `practical/`)

### Delayed Messages (отложенные сообщения)

```
Producer ──→ Wait Queue (TTL=5000ms, no consumers)
                   │
              TTL expires
                   │
                   ▼
        Dead Letter Exchange ──→ Process Queue ──→ Consumer
```

Трюк: очередь без подписчиков + `x-message-ttl` (или per-message expiration) + DLX.
Когда TTL истекает, сообщение перемещается в DLX → рабочую очередь.

### Priority Queues (приоритетные очереди)

```
Queue (x-max-priority=10):

  message (priority=10) ─┐
  message (priority=1)  ─┼──→ Consumer видит: priority=10, 5, 5, 2, 1
  message (priority=5)  ─┤
  message (priority=2)  ─┤
  message (priority=5)  ─┘
```

Очередь с аргументом `x-max-priority`. Сообщения с высоким приоритетом доставляются первыми.

### Multi-Tenant (мультитенантность)

```
Topic Exchange "learn.multitenant"

  routing "tenant-a.order.created" → Queue tenant-a (pattern: "tenant-a.#")
  routing "tenant-b.order.created" → Queue tenant-b (pattern: "tenant-b.#")
  routing "tenant-a.payment"       → Queue tenant-a + Queue ALL
  routing "*.*"                    → Queue ALL (audit: pattern "#")
```

Каждый арендатор получает только свои события. Аудит-очередь видит всё.

## Сценарии из реального мира (модуль `real-world/`)

### Quorum Queues (кворумные очереди)

```
Quorum Queue (RAFT-replicated):
  - x-queue-type: quorum
  - delivery-limit: 5 (после 5 неудачных доставок — отброс)
  - Реплицируется между узлами → HA без split-brain
```

Замена устаревших mirrored queues. Безопаснее и предсказуемее при сбоях.

### Rate Limiting (ограничение скорости)

Потребитель с искусственной задержкой между сообщениями:
- 20 сообщений отправлены мгновенно
- Consumer обрабатывает max 3/сек (задержка 333ms между сообщениями)

В продакшне используйте Guava RateLimiter или Resilience4j.

## Management UI

http://localhost:15672 (guest/guest)

- Мониторинг очередей, exchanges, bindings
- Просмотр сообщений в очередях
- Управление пользователями и vhosts
- Графики throughput, connections, channels
