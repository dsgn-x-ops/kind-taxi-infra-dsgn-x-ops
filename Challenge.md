# Taxi Data System Infrastructure Challenge

## Problem Statement

Work for a Transportation Department analyzing taxi ride patterns in NYC. A data generator produces real-time taxi ride information including pick-up/drop-off locations, timestamps, and pricing. Your task is to build the complete infrastructure and processing system to handle this data efficiently.

## Technology Stack

### Required

```
Backend:
- Java 17
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
- Maven
- Make
```

### Optional (For Bonus)

```
Monitoring:
- Prometheus
- OpenTelemetry
- Jaeger
```

## System Architecture

### Infrastructure Components

1. PostgreSQL Database
2. RabbitMQ
3. Redis

## Provided Components

### 1. API Service (Partially Complete)

A Spring Boot REST API that includes:

```
src/api/
├── Dockerfile
├── pom.xml
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

### 3. Processor Service

Implement the complete processor:

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


#### Required Makefile Targets

Initial Setup:

```makefile
all                     - Create cluster and perform full deploy
cluster-create          - Create new Kind cluster
cluster-delete          - Delete existing Kind cluster
```

Deployment Management:

```makefile
deploy-all             - Deploy complete system
deploy-infrastructure  - Deploy infrastructure components
deploy-apps           - Deploy application components
redeploy-apps         - Rebuild and redeploy applications
```

Resource Management:

```makefile
create-secrets        - Create required Kubernetes secrets
delete-secrets        - Remove all secrets
build-images         - Build all Docker images
load-images          - Load images into Kind cluster
```

Monitoring and Debugging:

```makefile
check-pods           - View pod status
check-logs           - View application logs
```

Service Management:

```makefile
port-forward-all     - Setup all port forwards
stop-port-forward    - Stop all port forwards
```

Cleanup:

```makefile
clean-all           - Complete system cleanup
clean-deploy        - Clean and redeploy
```

## Evaluation Criteria

### Infrastructure Design (35%)

- Kubernetes configuration quality
- Resource management
- Service configuration
- Security implementation

### Code Quality (35%)

- Clean code principles
- Error handling
- Testing coverage
- Documentation
- Best practices

### System Integration (30%)

- Component interaction
- Build automation
- Deployment process
- Operational excellence


## Submission Requirements

### 1. Code Repository

- Complete source code
- Kubernetes configurations
- Build scripts
- Documentation

### 2. Documentation

- Architecture overview
- Setup instructions
- Design decisions
- Testing procedures

### 3. Working Deployment

- Successful `make all` execution
- All services running
- Port forwarding working
- Basic operations functional

## Notes

- Focus on automation and reliability
- Document failure handling
- Consider resource constraints
- Follow Kind best practices
- Implement proper health checks
