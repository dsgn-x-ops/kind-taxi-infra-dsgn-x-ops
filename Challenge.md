# Taxi Data System Challenge

## Overview

You are provided with a partially implemented taxi data processing system. Your challenge is to:

1. Complete the Kubernetes infrastructure
2. Implement the Redis configuration
3. Implement the data processor service
4. Create build and deployment automation
5. Ensure all components work together seamlessly

## Provided Components

### 1. API Service (Partially Complete)

A Spring Boot REST API with:

- JacksonConfig.java
- SwaggerConfig.java
- Complete CRUD operations
- Models and DTOs
- Repository layer
- Service layer
- Unit tests

Directory structure:

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

### 2. Data Generator (Complete)

A Python service that:

- Generates taxi ride data
- Includes test suite
- Has RabbitMQ integration

## Your Tasks

### 1. Implement Redis Configuration

Complete the Redis configuration for the API:

```java
// RedisConfig.java needs implementation
@Configuration
@EnableCaching
public class RedisConfig {
    // Implement:
    // - Connection configuration
    // - Serialization setup
    // - Cache management
    // - Health checks
}
```

### 2. Complete Kubernetes Infrastructure

Implement the following missing configurations:

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

### 3. Implement Processor Service

Complete the processor service implementation:

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
    │   └── TaxiRiderRepository.java    # IMPLEMENT
    └── service/
        └── TaxiRideProcessor.java      # IMPLEMENT
```

### 4. Create Build & Deployment Automation

Implement the following:

```bash
# Makefile        # IMPLEMENT
# Common operations needed:
- cluster-create
- cluster-delete
- build-images
- load-images
- deploy-all
- check-pods
- clean-all

# skaffold.yaml   # IMPLEMENT
# Configuration for:
- Build automation
- Image pushing
- Deployment management
- Development workflow
```

## Technical Requirements

### Redis Configuration

- Connection pooling
- Proper serialization
- Cache configuration
- Error handling
- Health checks

### Infrastructure

- Resource limits and requests
- Health checks
- Persistent storage for PostgreSQL
- Service discovery
- Environment configurations

### Processor Service

- Circuit breaker pattern
- RabbitMQ message consumption
- PostgreSQL data persistence
- Error handling
- Unit tests

### Build Automation

- Development workflow
- Image building
- Deployment management
- Debug capabilities

## Detailed Implementation Requirements

### 1. Redis Configuration Example Structure

```java
@Configuration
@EnableCaching
public class RedisConfig implements CachingConfigurer {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    // Implement:
    // 1. Connection factory
    // 2. Redis template
    // 3. Cache manager
    // 4. Error handler
}
```

### 2. Makefile Structure

```makefile
# Required targets:
cluster-create:
    # Implement cluster creation

deploy-all:
    # Implement full deployment

# Add other necessary targets
```

### 3. Skaffold Configuration

```yaml
# skaffold.yaml
apiVersion: skaffold/v2beta29
kind: Config
build:
  # Implement build configuration
deploy:
  # Implement deploy configuration
```

## Getting Started

1. Start with Redis configuration:

   ```bash
   # Implement RedisConfig.java
   # Test cache operations
   ```

2. Create build automation:

   ```bash
   # Create Makefile
   # Setup skaffold.yaml
   ```

3. Implement infrastructure:

   ```bash
   # Work through each K8s component
   # Test deployments individually
   ```

4. Implement processor:

   ```bash
   # Develop processor components
   # Test integration
   ```

## Evaluation Criteria

### Configuration Implementation (25%)

- Redis configuration quality
- Build automation effectiveness
- Kubernetes configuration quality
- Resource management

### Processor Implementation (25%)

- Code quality
- Error handling
- Testing coverage
- Integration with other services

### Infrastructure (25%)

- Kubernetes setup
- Service configuration
- Security implementation
- Resource management

### Automation & Documentation (25%)

- Build process
- Deployment automation
- Setup instructions
- Testing procedures

## Deliverables

1. Complete Redis configuration
2. Complete Kubernetes configurations
3. Implemented processor service
4. Makefile and Skaffold configuration
5. Updated documentation
6. Test coverage

## Timeline

- Expected completion: 3-4 days
- Focus on quality over speed

## Notes

- Start with Redis and build automation
- Test each component individually
- Document your decisions
- Consider error scenarios
