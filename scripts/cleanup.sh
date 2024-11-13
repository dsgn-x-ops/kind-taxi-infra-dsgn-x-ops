#!/bin/bash
set -e

echo "Cleaning up resources..."

# Delete namespace
echo "Removing namespace..."
kubectl delete namespace taxi-system