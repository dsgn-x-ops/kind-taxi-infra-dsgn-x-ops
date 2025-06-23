# Setup Instructions

This document provides guidance on setting up and running the Taxi Data System project locally using both manual and automated (Gradle) methods.

---

## Prerequisites

Install the following tools:

- **Docker**
- **Kind**
- **kubectl**
- **Java 17+**
- **Gradle 8.5+** (or use `./gradlew`)
- **Python 3.11**
- **pip**
- **MkDocs** (optional for documentation)

---

## Manual Setup (for reference)

See previous steps on how to:
- Build Docker images
- Load them into Kind
- Apply manifests with `kubectl`
- Forward ports
- Run the generator manually

---

## Gradle Automation

The project provides several **Gradle tasks** to automate common operations.

### Initial Setup

```bash
./gradlew setupKind        # Create a fresh Kind cluster
./gradlew deleteKind       # Delete existing cluster
./gradlew fullDeploy       # Create Kind cluster and deploy everything
```

### Deployment Management

```bash
./gradlew deployInfra              # Deploy infrastructure manifests (DB, MQ, Redis)
./gradlew deployApps               # Deploy application services (API, Generator, Processor)
./gradlew deployManifests          # Deploys all manifests
```

### Resource Management

```bash
./gradlew createSecrets            # Create Kubernetes secrets
./gradlew deleteSecrets            # Remove Kubernetes secrets
./gradlew buildAllImages           # Build Docker images for all services
./gradlew loadImagesIntoKind       # Load Docker images into Kind
```

### Monitoring and Debugging

```bash
./gradlew viewPods                 # View pod statuses
./gradlew viewLogs                 # Tail logs for all main deployments
```

### Service Management

```bash
./gradlew startPortForward         # Forward all ports (API, Redis, Postgres, RabbitMQ)
./gradlew stopPortForwarding       # Stop all forwarded ports
```

### Cleanup

```bash
./gradlew cleanup                  # Full cleanup of K8s resources
```

---

## Notes

- All Gradle tasks are defined using the Kotlin DSL.
- You can see all available tasks with:
```bash
./gradlew tasks
```
- Ensure your `kind` cluster is active before running most tasks.
