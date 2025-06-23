# Taxi Data System Infrastructure Challenge

## Project Overview

This project simulates a distributed taxi ride system using a microservices-based architecture. It includes:

- A **Spring Boot API** for managing taxi rides.
- A **Python-based data generator** that simulates real-time ride events.
- A **Spring Boot processor** that consumes and stores ride data.
- Full deployment on **Kubernetes (Kind)** with Redis, PostgreSQL, and RabbitMQ as infrastructure services.

This documentation cover the following:

- Architecture overview
- Setup instructions
- Design decisions
- Testing strategy
- Gradle migration details
- Known issues and future improvements

---

## Technology Stack

```
Backend:
- Java 21
- Spring Boot 3.2
- Python 3.11
- PostgreSQL 14
- RabbitMQ 3
- Redis

Infrastructure:
- Docker
- Kubernetes (Kind)
- kubectl
- Skaffold

Build Tools:
- Gradle 8.5+ (Maven to Gradle migration required)

Monitoring:
- Prometheus
- OpenTelemetry
- Jaeger

CI/CD:
- GitHub Actions workflow for:
  - Running tests
  - Building and publishing Docker images to DockerHub
```

## System Architecture (Summary)

The system consists of several interconnected components:

- **API Service** (Spring Boot): Exposes REST endpoints, uses Redis for caching and PostgreSQL for persistence.
- **Data Generator** (Python): Produces JSON-structured ride events and publishes them to RabbitMQ.
- **Processor Service** (Spring Boot): Listens to RabbitMQ, processes incoming rides, and stores them in the database.
- **Infrastructure**:
    - **PostgreSQL** for permanent data storage
    - **RabbitMQ** as the message broker
    - **Redis** for in-memory caching
    - **Kubernetes** to orchestrate and deploy all services