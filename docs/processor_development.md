# Processor Development – Technical Documentation

**Objective:**

Implement the `processor` service for the `taxi-data-system` project. This service consumes taxi ride data from a RabbitMQ queue (`taxi-ride-queue`), deserializes the messages into domain entities, and persists them into a PostgreSQL database. It also uses a circuit breaker to ensure resilience during database failures.

---

## Project Structure

```
src/processor/
├── build.gradle.kts
├── Dockerfile
└── src/
    └── main/
        ├── java/com/taxidata/processor/
        │   ├── config/
        │   │   ├── CircuitBreakerConfig.java
        │   │   └── RabbitConfig.java
        │   ├── model/
        │   │   ├── Location.java
        │   │   └── TaxiRide.java
        │   ├── repository/
        │   │   └── TaxiRideRepository.java
        │   └── service/
        │       └── TaxiRideProcessor.java
        └── resources/
            └── application.yml
```

---

## Key Components

### `TaxiRide.java` and `Location.java`

- JPA entities representing a taxi ride and embedded location data.
- Includes `@Embedded` fields for start/end locations and a `@ElementCollection` for important places.

### `TaxiRideRepository.java`

- Interface extending `JpaRepository<TaxiRide, Long>`.
- Used for persisting `TaxiRide` objects into PostgreSQL.

### `TaxiRideProcessor.java`

- Service class annotated with `@Service` and `@RabbitListener`.
- Receives messages from the `taxi-ride-queue`.
- Uses Resilience4j's `CircuitBreaker` to wrap the `repository.save(...)` call.
- Instantiated with `@RequiredArgsConstructor` and circuit breaker injected via `@PostConstruct`.

### `CircuitBreakerConfig.java`

- Defines a Resilience4j circuit breaker configuration bean with:
    - 50% failure threshold
    - 10-second wait time in open state
    - 10-call sliding window

### `RabbitConfig.java`

- Declares the queue if needed.
- Configures a Jackson message converter.
- Defines the `SimpleRabbitListenerContainerFactory` used by `@RabbitListener`.

---

## Configuration – `application.yml`

```yaml
spring:
  application:
    name: taxi-processor

  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/taxidb}
    username: ${SPRING_DATASOURCE_USERNAME:taxi_user}
    password: ${SPRING_DATASOURCE_PASSWORD:taxi_pass}
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false

  rabbitmq:
    host: ${RABBITMQ_HOST:rabbitmq}
    port: 5672
    username: ${RABBITMQ_USER}
    password: ${RABBITMQ_PASS}

rabbitmq:
  queue: taxi-ride-queue
```

> Secrets are injected via Kubernetes secrets defined in `secret.yaml` and referenced in `deployment.yaml`. No sensitive values are hardcoded in the `application.yml`.