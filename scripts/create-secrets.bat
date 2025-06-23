@echo off

echo Creating secrets...
kubectl create secret generic db-credentials ^
  --from-literal=username=taxi_user ^
  --from-literal=password=taxi_pass ^
  --namespace taxi-system

kubectl create secret generic rabbitmq-credentials ^
  --from-literal=username=taxi_user ^
  --from-literal=password=taxi_pass ^
  --namespace taxi-system