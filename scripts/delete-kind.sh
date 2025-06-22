#!/bin/bash
set -e
echo "Deleting Kind cluster..."
kind delete cluster
echo "Cluster successfully eliminated."