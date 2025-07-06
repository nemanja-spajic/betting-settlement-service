# 🏇 Betting Settlement Service

This service simulates sports betting event outcomes and bet settlement using **Kafka**, **Spring Boot**, and a mock **RocketMQ** implementation.

---

## ⚙️ Tech Stack

- Java 21
- Spring Boot 3
- Apache Kafka (via Docker)
- H2 in-memory database
- Kafdrop (Kafka Web UI)
- Lombok

---

## 🚀 Getting Started

### 1. Clone the project

```bash
git clone https://github.com/your-org/betting-settlement-service.git
cd betting-settlement-service
```

### 2. Start Kafka + Zookeeper + Kafdrop

```bash
cd docker
docker-compose up -d
```

This runs:
- Kafka on `localhost:9092`
- Zookeeper on `localhost:2181`
- Kafdrop UI on [http://localhost:9000](http://localhost:9000)

---

### 3. Run the Spring Boot app

You can run it from your IDE or via Maven:

```bash
./mvnw spring-boot:run
```

The app will:
- Create and preload test bets in an in-memory H2 database
- Listen to the `event-outcomes` Kafka topic
- Match incoming event outcomes to bets
- Log mock settlements via a RocketMQ logger

---

## 🧪 Test the Flow

### 1. Submit an event outcome via `curl`

```bash
curl -X POST http://localhost:8080/api/events/outcome \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "motogp-assen-2025",
    "eventName": "MotoGP Assen 2025",
    "eventWinnerId": "fabio-quartararo"
  }'
```

### 2. Watch the logs

You should see:

```
INFO  EventOutcomeProducer       : EVENT_PRODUCED             topic=event-outcomes, eventId=motogp-assen-2025
SQL   Hibernate                  : SELECT ...                 FROM bets WHERE event_id = ?
INFO  EventOutcomeHandlerService: BET_WON_SENDING_TO_ROCKETMQ betId=11111...
INFO  BetSettlementProducer     : ROCKETMQ_SETTLING_BET       json={"betId":"11111-...", ...}
INFO  EventOutcomeHandlerService: BET_LOST_SKIPPING           betId=22222-...
INFO  EventOutcomeConsumer      : EVENT_PROCESSED             topic=event-outcomes, eventId=motogp-assen-2025
```

---

### 3. Inspect messages in Kafdrop

Go to: [http://localhost:9000](http://localhost:9000)

- Click on topic `event-outcomes`
- View published messages and payloads
- Monitor partitions and offsets

---

## 🧼 Cleanup

```bash
cd docker
docker-compose down
```

---

## ✅ Example Test Bet Data (preloaded)

```sql
ID: auto-generated UUID
User: user-46
Event ID: motogp-assen-2025
Winner ID: fabio-quartararo
Amount: 100
```

You can modify or preload additional test data in `data.sql` or via `CommandLineRunner`.
