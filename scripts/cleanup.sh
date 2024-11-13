#!/bin/bash
set -e

echo "Cleaning up resources..."

# Delete monitoring stack
echo "Removing monitoring stack..."
helm uninstall prometheus -n taxi-system || true
helm uninstall loki -n taxi-system || true
helm uninstall promtail -n taxi-system || true
helm uninstall grafana -n taxi-system || true

# Delete namespace
echo "Removing namespace..."
kubectl delete namespace taxi-system