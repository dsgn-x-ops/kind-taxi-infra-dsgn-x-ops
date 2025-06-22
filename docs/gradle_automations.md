# Infrastructure Automation – Gradle

This document summarizes the process of building a portable and automated infrastructure setup for the `taxi-data-system` project using **Gradle tasks**, **Docker**, **Kubernetes (Kind)**, and **cross-platform scripting**.

**Objective:**

- Create and delete a Kind cluster.
- Build and load Docker images for all services (API, Processor, Generator).
- Deploy manifests to the Kubernetes cluster.
- Forward ports for local access.
- View logs and cluster status.

## Gradle Tasks Implemented

- **setupKind**: Creates a Kind cluster and sets up secrets and Helm repos.
- **cleanupCluster**: Deletes the `taxi-system` namespace.
- **deleteKind**: Deletes the Kind cluster.
- **buildApiImage / buildProcessorImage / buildDataGenerator**: Builds Docker images for each service.
- **loadImagesIntoKind**: Loads built Docker images into the Kind cluster.
- **deployManifests**: Applies Kubernetes manifests from the `k8s/` folder.
- **viewPodStatus / viewLogs**: For debugging and monitoring.
- **startPortForward / stopPortForward**: Starts and stops port forwarding for the API service.

Grouped tasks like `fullDeploy` and `buildAllImages` combine these operations for convenience.

---

## Cross-Platform Challenges

### Problem

Scripts written in `.sh` format (Bash) failed to execute correctly on Windows when invoked via Gradle, despite working manually in Git Bash.

### Root Cause

Gradle uses its own internal shell environment when invoking `commandLine("bash", ...)`, which doesn't inherit the full system PATH. As a result, commands like `kind` and `kubectl` were not found, even though they were available in the user's terminal.

### Solution

It was created **paired scripts** for each operation:

- `setup-kind.sh` + `setup-kind.bat`
- `delete-kind.sh` + `delete-kind.bat`
- `start-portforward.sh` + `start-portforward.bat`
- `stop-portforward.sh` + `stop-portforward.bat`
- `cleanup.sh` + `cleanup.bat`

It was also introduced a Gradle helper function:

```kotlin
fun runScriptForOS(windowsScript: String, unixScript: String): List<String> {
    return if (System.getProperty("os.name").lowercase().contains("windows")) {
        listOf("cmd", "/c", windowsScript)
    } else {
        listOf("bash", unixScript)
    }
}
```

All relevant Gradle tasks now invoke the correct script for the detected OS.

---

## Outcome

By solving pathing issues and introducing portable scripting, the project now supports:

- Cross-platform development on Windows and Unix-like systems
- Reproducible infrastructure bootstrapping
- Simple Gradle-based DevOps automation

This groundwork also opens the door for future CI/CD integration.