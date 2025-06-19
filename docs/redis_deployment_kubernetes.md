# Redis Deployment – Kubernetes

**Objective:**

Deploy a **Redis** instance in a Kubernetes cluster for in-memory caching or message brokering. This setup is designed for local development using Kind and supports integration with other services such as API and Processor.

---

## Files Created

| File | Path | Description |
|------|------|-------------|
| `redis-svc.yaml` | `k8s/services/` | ClusterIP service for Redis (`redis:6379`). |
| `redis.yaml` | `k8s/deployments/` | Deployment for Redis container with basic runtime args. |

---

## Configuration Details

### Service

```yaml
name: redis
type: ClusterIP
port: 6379
```

This creates a DNS-accessible service inside the cluster at `redis.taxi-system.svc.cluster.local`.

---

### Deployment

- Image: `redis:7.2-alpine`
- Port: `6379`
- Mode: no persistence (`--save "" --appendonly no`)
- Resources:
    - `requests`: 64Mi memory, 50m CPU
    - `limits`: 128Mi memory, 100m CPU
- Namespace: `taxi-system`

---

## How to Apply

```bash
kubectl apply -f k8s/services/redis-svc.yaml
kubectl apply -f k8s/deployments/redis.yaml
```

---

## How to Test

Forward the port locally:

```bash
kubectl -n taxi-system port-forward svc/redis 6379:6379
```

Connect using `redis-cli`:

```bash
redis-cli -h 127.0.0.1 -p 6379
```

Simple test:

```redis
SET foo bar
GET foo
```
