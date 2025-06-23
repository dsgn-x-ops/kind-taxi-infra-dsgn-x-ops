@echo off

echo Creating secrets...
kubectl delete secret db-credentials -n taxi-system

kubectl delete secret rabbitmq-credentials -n taxi-system