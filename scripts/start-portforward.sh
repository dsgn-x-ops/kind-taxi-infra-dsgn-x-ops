#!/bin/bash
set -e

echo "Port-forward in background..."

kubectl port-forward svc/taxi-api 8080:8080 -n taxi-system > portforward.log 2>&1 &
echo $! > portforward.pid

echo "Port-forward active (PID $(cat portforward.pid))"