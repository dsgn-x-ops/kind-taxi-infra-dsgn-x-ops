
# Architecture Overview

The Taxi Data System is a microservices-based application designed to simulate and process real-time taxi ride data. It follows a modular architecture where each service has a distinct role, enabling separation of concerns, ease of testing, and deployment in containerized environments.

---

## Components

### 1. API Service (Spring Boot)
- **Role**: Exposes REST endpoints to clients for creating, reading, and querying taxi ride data.
- **Data Flow**:
    - Reads from Redis cache where applicable.
    - Falls back to PostgreSQL when cache misses occur.
    - Can receive requests for filtered results (e.g., price range, request taxi by id).
- **Documentation**: OpenAPI (Swagger) for API contract testing.
- **Caching**: Integrated with Redis using a TTL strategy (30 minutes).

### 2. Data Generator (Python)
- **Role**: Simulates real-time taxi ride events.
- **Data Flow**:
    - Generates JSON payloads with geolocation, timestamp, and pricing information.
    - Publishes messages to the RabbitMQ queue (`taxi-ride-queue`) at regular intervals.
- **Use Case**: Useful for testing ingestion performance and load handling of downstream services.

### 3. Processor (Spring Boot)
- **Role**: Consumes messages from RabbitMQ and persists them to PostgreSQL.
- **Key Features**:
    - Message deserialization and validation.
    - Use of Resilience4j Circuit Breaker to avoid system overload during failures.
    - JPA-based persistence using `TaxiRideRepository`.

---

## Infrastructure

### Kubernetes (Kind)
All services are deployed to a local Kubernetes cluster using Kind. The manifests are organized into:
- **Deployments** (`k8s/deployments/`)
- **Services** (`k8s/services/`)
- **Volumes** (`k8s/persistent-volumes/`)

### PostgreSQL
- Relational database used for persistent storage of taxi rides.
- Each ride includes start and end locations, important landmarks, and metadata like timestamps and pricing.

### RabbitMQ
- Message broker for decoupling the producer (Generator) and consumer (Processor).
- Supports fault tolerance and scaling of processing logic.

### Redis
- Used as a distributed cache layer for frequently accessed read operations from the API.
- Configured with JSON serializers and error handlers for robustness.

---

## Logical Flow

```text
        [User Request]
             ↓
           [API]
          ↙     ↘
     [Redis]   [PostgreSQL]
             ↑
       [Processor] ← [RabbitMQ] ← [Data Generator]
```

---

## Observability and Testing

- Local debugging via port forwarding (e.g., `kubectl port-forward svc/api 8081:8080`).
- Logs from API, Generator, and Processor confirm message flow and data persistence.
- Swagger UI confirms request/response correctness.

---