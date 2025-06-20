# Generator Deployment – Kubernetes

**Objective:**

Deploy the `generator` service in a Kubernetes cluster. This service continuously generates simulated taxi ride data and publishes it to RabbitMQ. It acts as a background data producer for testing and simulation purposes.

---

## Files Created

| File | Path | Description |
|------|------|-------------|
| `generator.yaml` | `k8s/deployments/` | Deployment manifest for the generator service. |

---

## Configuration Details

### Deployment

- Image: `generator:latest` (must be built locally and loaded into Kind)
- Runs continuously (infinite loop with scheduled batches)
- Sends messages to RabbitMQ
- Environment variables:
    - `BATCH_SIZE=100`
    - `RABBITMQ_HOST=rabbitmq`
    - `RABBITMQ_USER` and `RABBITMQ_PASS` via secret `rabbitmq-secret`
- Resources:
    - `requests`: 64Mi memory, 50m CPU
    - `limits`: 128Mi memory, 100m CPU
- Namespace: `taxi-system`

---

## How to Apply

```bash
kubectl apply -f k8s/deployments/generator.yaml
```

---

## Docker Image Instructions

1. Build the image locally:

```bash
docker build -t generator:latest .
```

2. Load the image into Kind:

```bash
kind load docker-image generator:latest --name taxi-cluster
```

---

## How to Test

Monitor logs:

```bash
kubectl logs -f deployment/generator -n taxi-system
```

You should see log messages indicating successful batch generation and message publishing to RabbitMQ.