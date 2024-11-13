# Taxi Data System

## Overview
This project implements a scalable system for processing taxi ride data using Kubernetes. The system includes data generation, processing, and API components.

## Prerequisites
- Docker
- Kind (Kubernetes in Docker)
- kubectl
- Skaffold
- Make

## Getting Started

### Installation Steps

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd taxi-data-system
   ```

2. Start from scratch (creates cluster and deploys everything):
   ```bash
   make clean-all && make all
   ```

### Available Make Commands

#### Cluster Management
```bash
# Create new cluster
make cluster-create

# Delete cluster
make cluster-delete

# Deploy everything
make deploy-all
```

#### Application Management
```bash
# Build Docker images
make build-images

# Load images into Kind cluster
make load-images

# Redeploy applications
make redeploy-apps

# Create required secrets
make create-secrets

# Delete secrets
make delete-secrets
```

#### System Status
```bash
# Check pod status
make check-pods

# View application logs
make check-logs

# Check infrastructure status
make debug-infra

# Describe problematic pods
make describe-pods
```

#### Cleanup
```bash
# Clean everything
make clean-all

# Clean and redeploy
make clean-deploy
```

## Accessing Services

### Application API
```bash
# Port forward the API service
kubectl port-forward svc/taxi-api 8080:8080 -n taxi-system

# Access API at:
http://localhost:8080
```

## Project Structure
```
.
├── k8s/                    # Kubernetes manifests
│   ├── configmaps/        # Configuration files
│   ├── deployments/       # Application deployments
│   ├── namespace.yaml     # Namespace definitions
│   ├── persistent-volumes/# Storage configurations
│   └── services/         # Service definitions
├── src/                   # Application source code
│   ├── api/              # REST API service
│   ├── data-generator/   # Data generation service
│   └── processor/        # Data processing service
├── Makefile              # Build and deployment automation
├── skaffold.yaml         # Skaffold configuration
└── README.md             # Project documentation
```

## Development

### Local Development
1. Make code changes in the respective service directories
2. Build and reload services:
   ```bash
   make build-images
   make load-images
   make redeploy-apps
   ```

### Testing
Each service contains its own test suite:
- API: `src/api/src/test`
- Processor: `src/processor/src/test`
- Generator: `src/data-generator/tests`

## Troubleshooting

### Common Issues
1. Pods not starting:
   ```bash
   make describe-pods
   make check-logs
   ```

2. Application issues:
   ```bash
   make check-logs
   ```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License
[Add your license information here]