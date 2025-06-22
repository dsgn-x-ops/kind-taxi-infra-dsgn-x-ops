@echo off
echo Setting up Kind cluster...

kind create cluster --config kind-config.yaml

kubectl create namespace taxi-system

echo Creating secrets...
kubectl create secret generic db-credentials ^
  --from-literal=username=taxi_user ^
  --from-literal=password=taxi_pass ^
  --namespace taxi-system

kubectl create secret generic rabbitmq-credentials ^
  --from-literal=username=taxi_user ^
  --from-literal=password=taxi_pass ^
  --namespace taxi-system

echo Adding Helm repositories...
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

echo Kind setup complete!
