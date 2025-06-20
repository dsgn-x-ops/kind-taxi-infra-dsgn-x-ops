# RabbitMQ Deployment – Kubernetes

**Objective:**

Deploy a **RabbitMQ** message broker in a Kubernetes cluster, with internal messaging and a management UI. This setup supports local development with Kind and provides secure access via Kubernetes Secrets.

---

## Files Created

| File | Path | Description |
|------|------|-------------|
| `rabbitmq-secret.yaml` | `k8s/secrets/` | Secret containing default RabbitMQ credentials. |
| `rabbitmq-svc.yaml` | `k8s/services/` | ClusterIP service exposing AMQP (5672) and UI (15672). |
| `rabbitmq.yaml` | `k8s/deployments/` | Deployment for the RabbitMQ broker. |

---

## Configuration Details

### Secret

```yaml
name: rabbitmq-secret
username: taxiuser
password: taxipass
```

Used to inject credentials into the RabbitMQ container via environment variables.

---

### Service

```yaml
name: rabbitmq
type: ClusterIP
ports:
  - 5672 (AMQP)
  - 15672 (Management UI)
```

Accessible internally as `rabbitmq.taxi-system.svc.cluster.local`.

---

### Deployment

- Image: `rabbitmq:3.12-management-alpine`
- Ports: `5672`, `15672`
- Credentials: injected via Secret
- Resources:
    - `requests`: 128Mi memory, 100m CPU
    - `limits`: 256Mi memory, 250m CPU
- Namespace: `taxi-system`

---

## How to Apply

```bash
kubectl apply -f k8s/secrets/rabbitmq-secret.yaml
kubectl apply -f k8s/services/rabbitmq-svc.yaml
kubectl apply -f k8s/deployments/rabbitmq.yaml
```

---

## How to Test

Forward the management port:

```bash
kubectl -n taxi-system port-forward svc/rabbitmq 15672:15672
```

Open in browser:

```
http://localhost:15672
```

Login with:

- **Username**: `taxiuser`
- **Password**: `taxipass`