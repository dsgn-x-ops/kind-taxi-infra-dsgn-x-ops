@echo off
echo Cleaning up resources...

echo Removing namespace...
kubectl delete namespace taxi-system

echo Cleanup complete.
