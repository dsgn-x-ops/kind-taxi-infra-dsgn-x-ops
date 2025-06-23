#!/bin/bash
set -e

echo "Setting up Kind cluster..."

# Create Kind cluster
kind create cluster --config kind-config.yaml

# Create namespace
kubectl create namespace taxi-system

# Create secrets
echo "Creating secrets..."
kubectl create secret generic db-credentials \
    --from-literal=username=taxi_user \
    --from-literal=password=taxi_pass \
    --namespace taxi-system

kubectl create secret generic rabbitmq-credentials \
    --from-literal=username=taxi_user \
    --from-literal=password=taxi_pass \
    --namespace taxi-system

# Add Helm repos
echo "Adding Helm repositories..."
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/prometheus --namespace monitoring --create-namespace
echo "Kind cluster setup complete!"