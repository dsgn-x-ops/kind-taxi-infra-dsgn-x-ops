# Technical Documentation – Integration & Testing of `taxi-data-system`

## Components Tested

### 1. Data Generator (Python)
- Verified message publishing to RabbitMQ.
- Logs from the `processor` confirmed message reception and persistence in PostgreSQL.

### 2. Processor (Spring Boot)
- Consumes messages from RabbitMQ and saves them in PostgreSQL.
- Logs validated with `✔ Ride saved` messages.

### 3. PostgreSQL
- Successfully tested persistence via the `processor`.
- Database populated with simulated ride data.

### 4. RabbitMQ
- Queue receives messages and delivers them to the `processor`.
- Validated via `processor` logs.

### 5. Redis (Cache)
- Verified integration with the API.
- Cache functional using `@Cacheable` methods and `/cache/status` endpoint.

### 6. API (Spring Boot)
- Swagger UI used to test endpoints (`/api/v1/rides`, `/api/v1/rides/{id}`).
- Fixed error with query `order by t.string` caused by malformed Swagger UI request (sort by nonexistent field).
- RedisTemplate properly injected after adjustments in `RedisConfig.java`.

## Gradle Automation Adjustments

### Docker Image Build
- Ensured `.jar` is properly generated (`build/libs/taxi-data-system.jar`).
- Confirmed the `Dockerfile` correctly copies the `.jar`.
- `.dockerignore` validated to exclude `build/` but adjusted if necessary to include `build/libs`.

### Issues Fixed
- Redis port misconfiguration (6380 instead of 6379): clarified that `6380` is for local port-forwarding.
- API crashing due to missing `RedisTemplate`: added the bean in `RedisConfig.java`.
- API error due to `order by t.string`: issue originated from Swagger UI, not the backend code.

### Port Forwarding Used for Debugging
- Redis: 6379 → 6380
- API: 8080 → 8081
- RabbitMQ UI: 15672
- PostgreSQL: 5432

## Final Validations
- API responsive and functional via Swagger.
- Simulated data generated and processed successfully.
- Redis cache verified operational.
- All services communicating properly in local Kind cluster.