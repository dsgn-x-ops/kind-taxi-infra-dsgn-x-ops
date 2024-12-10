# Taxi Data System Infrastructure Challenge

## Problem Statement

Work for a Transportation Department analyzing taxi ride patterns in NYC. A data generator produces real-time taxi ride information including pick-up/drop-off locations, timestamps, and pricing. Your task is to build the complete infrastructure and processing system to handle this data efficiently.

## Technology Stack

### Required

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
```

### Optional (For Bonus)

```
Monitoring:
- Prometheus
- OpenTelemetry
- Jaeger

CI/CD:
- GitHub Actions workflow for:
  - Running tests
  - Building and publishing Docker images to DockerHub
```

## Important Note About Build System Migration

As part of this challenge, you are required to:
1. Convert all existing Maven configurations to Gradle
2. Set up new services using Gradle
3. Implement all build automation using Gradle
4. Ensure the build system migration maintains all existing functionality
5. Document your migration approach and any issues encountered

The provided Maven configurations must be converted to use:
- Kotlin DSL (`build.gradle.kts`)
- Multi-project Gradle setup
- Appropriate Gradle plugins and configurations
- Equivalent dependency management

## System Architecture

### Infrastructure Components

1. PostgreSQL Database
2. RabbitMQ
3. Redis

## Provided Components

### 1. API Service (Requires Maven to Gradle Migration)

A Spring Boot REST API that includes:

```
src/api/
├── Dockerfile
├── build.gradle.kts     # Convert from pom.xml
└── src/
    └── main/
        ├── java/com/taxidata/api/
        │   ├── config/
        │   │   ├── JacksonConfig.java
        │   │   ├── RedisConfig.java    # IMPLEMENT
        │   │   └── SwaggerConfig.java
        │   ├── controller/
        │   ├── model/
        │   ├── repository/
        │   └── service/
        └── resources/
```

Features:

- CRUD operations for taxi rides
- Price range filtering with pagination
- Redis caching (configuration needed)
- OpenAPI documentation
- Complete test suite

### 2. Data Generator (Complete)

A Python service generating taxi data:

```
src/data-generator/
├── Dockerfile
├── generator.py
├── requirements.txt
└── tests/
    └── test_generator.py
```

Sample Data Structure:

```json
{
    "start": {
        "latitude": 40.7128,
        "longitude": -74.0060,
        "place": "New York City"
    },
    "end": {
        "latitude": 40.7580,
        "longitude": -73.9855,
        "place": "Times Square"
    },
    "important_places": [
        {
            "latitude": 40.7527,
            "longitude": -73.9772,
            "place": "Grand Central Terminal"
        }
    ],
    "start_date": "2024-01-01T10:00:00",
    "end_date": "2024-01-01T10:30:00",
    "price": 25.50,
    "distance_km": 3.4
}
```

## Required Implementations

### 1. Redis Configuration

Implement complete Redis configuration:

```
src/api/src/main/java/com/taxidata/api/config/RedisConfig.java
```

Requirements:

- Connection pooling
- Serialization configuration
- Cache management
- Error handling
- Health checks

### 2. Kubernetes Infrastructure

Implement all K8s configurations:

```
k8s/
├── deployments/
│   ├── api.yaml           # IMPLEMENT
│   ├── generator.yaml     # IMPLEMENT
│   ├── postgres.yaml      # IMPLEMENT
│   ├── processor.yaml     # IMPLEMENT
│   ├── rabbitmq.yaml      # IMPLEMENT
│   └── redis.yaml         # IMPLEMENT
├── persistent-volumes/
│   ├── postgres-pv.yaml   # IMPLEMENT
│   └── postgres-pvc.yaml  # IMPLEMENT
└── services/
    ├── api-svc.yaml       # IMPLEMENT
    ├── postgres-svc.yaml  # IMPLEMENT
    ├── rabbitmq-svc.yaml  # IMPLEMENT
    └── redis-svc.yaml     # IMPLEMENT
```

### 3. New Processor Service

Implement the complete processor using Gradle:

```
src/processor/
└── src/main/java/com/taxidata/processor/
    ├── config/
    │   ├── CircuitBreakerConfig.java  # IMPLEMENT
    │   └── RabbitConfig.java          # IMPLEMENT
    ├── model/
    │   ├── Location.java              # IMPLEMENT
    │   └── TaxiRide.java              # IMPLEMENT
    ├── repository/
    │   └── TaxiRiderRepository.java   # IMPLEMENT
    └── service/
        └── TaxiRideProcessor.java     # IMPLEMENT
```

### 4. Build & Deployment Automation

#### Required Port Configuration

```
- API: 8081 (forwarded from 8080)
- Redis: 6380 (forwarded from 6379)
- PostgreSQL: 5432
- RabbitMQ: 5672 (AMQP), 15672 (Management)
```

#### Required Gradle Tasks

Create Gradle tasks for the following operations:

Initial Setup:
- Create new Kind cluster
- Delete existing Kind cluster
- Create cluster and perform full deploy

Deployment Management:
- Deploy infrastructure components
- Deploy application components
- Rebuild and redeploy applications

Resource Management:
- Create required Kubernetes secrets
- Remove all secrets
- Build all Docker images
- Load images into Kind cluster

Monitoring and Debugging:
- View pod status
- View application logs

Service Management:
- Setup all port forwards
- Stop all port forwards

Cleanup:
- Complete system cleanup
- Clean and redeploy

### 5. CI/CD Implementation (Bonus)

Implement a GitHub Actions workflow that:
- Runs the test suite
- Builds and publishes Docker images to DockerHub

## Project Structure

Implement a Gradle multi-project structure:

```
.
├── build.gradle.kts
├── settings.gradle.kts
├── buildSrc/              # Optional for shared build logic
├── src/
│   ├── api/
│   │   └── build.gradle.kts
│   └── processor/
│       └── build.gradle.kts
└── k8s/
```

## Evaluation Criteria

### Build System Migration and Setup (25%)

- Successful Maven to Gradle conversion
- Proper multi-project structure
- Build script organization
- Dependency management
- Plugin configuration

### Infrastructure Design (25%)

- Kubernetes configuration quality
- Resource management
- Service configuration
- Security implementation

### Code Quality (25%)

- Clean code principles
- Error handling
- Testing coverage
- Documentation
- Best practices

### System Integration (25%)

- Component interaction
- Build automation
- Deployment process
- Operational excellence
- Task automation

## Submission Requirements

### 1. Code Repository

- Complete source code
- Kubernetes configurations
- Gradle build scripts
- Documentation
- GitHub Actions workflow (if implementing bonus)

### 2. Documentation

Required sections:
- Architecture overview
- Setup instructions
- Design decisions
- Testing procedures
- Maven to Gradle migration process
- Known limitations or issues
- Future improvements

### 3. Working Deployment

Requirements:
- Successful Gradle task execution for deployment
- All services running
- Port forwarding working
- Basic operations functional
- Successful test execution

## Notes

- Focus on automation and reliability
- Document failure handling
- Consider resource constraints
- Follow Kind best practices
- Implement proper health checks
- Ensure proper Gradle configuration for multi-project setup
- Document all build system migration decisions

