
# LedgerForge — Event-Sourced Payments Ledger (CQRS, Outbox, Kafka, Redis Locks)

- **Java 21 + Spring Boot 3**, Gradle multi-module
- **CQRS**: `command-service` (write) and `query-service` (read)
- **Event sourcing** with **Kafka** and an **Outbox pattern** (exactly-once at-least-once hybrid)
- **Redis-based distributed locks** for idempotent account mutations
- **Postgres** for read model, **Docker Compose** for infra
- **API Gateway** facade + **OpenAPI** (Swagger UI at `/swagger`)

### Why it stands out
- Clear **hexagonal-ish** boundaries (common/events, command, query, gateway)
- Demonstrates **operational acumen**: Kafka, Postgres, Redis, Docker, load testing
- Shows knowledge of **consistency models**, idempotency, and **eventual consistency**
- Recruiter-friendly: clean README, runnable stack, realistic patterns

## Quickstart

```bash
# 1) Infra
docker compose up -d

# 2) Build & run services (in separate terminals)
./gradlew :command-service:bootRun
./gradlew :query-service:bootRun
./gradlew :api-gateway:bootRun
```

Visit Swagger: http://localhost:8080/swagger

### Smoke test
```bash
# credit & debit via gateway
curl -X POST localhost:8080/api/accounts/alice/credit -H 'Content-Type: application/json' -d '{"amountCents": 200}'
curl -X POST localhost:8080/api/accounts/alice/debit  -H 'Content-Type: application/json' -d '{"amountCents": 50}'
curl localhost:8080/api/accounts/alice
```

### Load test (optional)
```bash
k6 run loadtest/k6-credit-debit.js
```

## Architecture

- **Command Service**: validates and enqueues domain events into an **Outbox** table; a scheduler publishes to Kafka.
- **Query Service**: consumes Kafka events, updates **read model** in Postgres.
- **API Gateway**: simple facade that aggregates REST calls and exposes OpenAPI.

This split showcases **CQRS** and **eventual consistency** with **read/write separation**. The **Outbox** ensures reliability across DB + Kafka without XA.

## Extending Ideas
- Add **SAGA** orchestration for multi-account transfers.
- Use **Testcontainers** to spin Kafka/Postgres for integration tests.
- Expose **gRPC** for low-latency internal comms.
- Add **OpenTelemetry** + Jaeger for traces; Prometheus/Grafana for metrics.
- Implement **idempotency keys** and duplicate detection at gateway.
- Add **Multi-tenancy** with row-level security.

> Educational demo; not financial advice.
