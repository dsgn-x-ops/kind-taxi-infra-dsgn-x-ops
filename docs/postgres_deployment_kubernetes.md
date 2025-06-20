# PostgreSQL Deployment – Kubernetes

**Objective:**

Deploy a **PostgreSQL** instance in a Kubernetes cluster with persistent storage and internal access via service discovery. This setup allows other microservices (e.g., API, Processor) to connect to the database for data persistence.

---

## Files Created

| File | Path | Description |
|------|------|-------------|
| `postgres-pv.yaml` | `k8s/persistent-volumes/` | PersistentVolume (PV) using `hostPath` to store database data. |
| `postgres-pvc.yaml` | `k8s/persistent-volumes/` | PersistentVolumeClaim (PVC) to bind the volume to the container. |
| `postgres-svc.yaml` | `k8s/services/` | Internal ClusterIP service for PostgreSQL (`postgres:5432`). |
| `postgres.yaml` | `k8s/deployments/` | Deployment manifest for the PostgreSQL container. |
| `postgres-secret.yaml` | `k8s/secrets/` | Kubernetes secret storing database credentials. |

---

## Configuration Details

### Namespace

All resources are deployed under the `taxi-system` namespace.

```bash
kubectl apply -f k8s/namespace.yaml
```

---

### Persistent Volume (PV)

```yaml
kind: PersistentVolume
path: /mnt/data/postgres
storage: 1Gi
accessMode: ReadWriteOnce
```

> `hostPath` is used for local development with Kind. Not suitable for production.

---

### Secret

```yaml
name: postgres-secret
user: taxiuser
password: taxi123
```

> Never store plain-text passwords in production environments.

---

### Service

```yaml
name: postgres
type: ClusterIP
port: 5432
```

Enables internal DNS-based access to the database at `postgres.taxi-system.svc.cluster.local`.

---

### Deployment

- Image: `postgres:15`
- Mounted volume at `/var/lib/postgresql/data`
- Environment variables:
    - `POSTGRES_DB=taxidata`
    - `POSTGRES_USER` and `POSTGRES_PASSWORD` from secret

---

## How to Apply

```bash
kubectl apply -f k8s/secrets/postgres-secret.yaml
kubectl apply -f k8s/persistent-volumes/postgres-pv.yaml
kubectl apply -f k8s/persistent-volumes/postgres-pvc.yaml
kubectl apply -f k8s/services/postgres-svc.yaml
kubectl apply -f k8s/deployments/postgres.yaml
```

---

## How to Test

Forward the port locally:

```bash
kubectl -n taxi-system port-forward svc/postgres 5432:5432
```

Connect using `psql`, DBeaver, or other tools:

```
Host: localhost
Port: 5432
User: taxiuser
Password: taxi123
Database: taxidata
```