import java.util.Locale

plugins {
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    java
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    group = "com.taxidata"
    version = "1.0.0"

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

// === Infrastructure (Kind e K8s) ===

fun runScriptForOS(windowsScript: String, unixScript: String): List<String> {
    return if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")) {
        listOf("cmd", "/c", windowsScript)
    } else {
        listOf("bash", unixScript)
    }
}

tasks.register<Exec>("setupKind") {
    group = "infrastructure"
    description = "Creates Kind cluster"
    commandLine(runScriptForOS("scripts\\setup-kind.bat", "scripts/setup-kind.sh"))
}

tasks.register<Exec>("cleanupCluster") {
    group = "infrastructure"
    description = "Removes namespace taxi-system"
    commandLine(runScriptForOS("scripts\\cleanup.bat", "scripts/cleanup.sh"))
}

tasks.register<Exec>("deleteKind") {
    group = "infrastructure"
    description = "Eliminates Kind cluster"
    commandLine(runScriptForOS("scripts\\delete-kind.bat", "scripts/delete-kind.sh"))
}

// === Docker ===

tasks.register<Exec>("buildApiImage") {
    group = "docker"
    description = "Builds the Docker image from API service"
    commandLine("docker", "build", "-f", "src/api/Dockerfile", "-t", "taxi-api:v1.01", ".")
}

tasks.register<Exec>("buildProcessorImage") {
    group = "docker"
    description = "Builds the Docker image from Processor service"
    commandLine("docker", "build", "-f", "src/processor/Dockerfile", "-t", "taxi-processor:v1.01", ".")
}

tasks.register<Exec>("buildDataGenerator") {
    group = "docker"
    description = "Builds the Docker image from Generator service"
    commandLine("docker", "build", "-t", "taxi-generator:v1.01", "src/data-generator")
}

tasks.register<Exec>("loadImagesIntoKind") {
    group = "docker"
    description = "Loads all images into cluster Kind"
    dependsOn("buildApiImage", "buildProcessorImage")
    doLast {
        exec {
            commandLine("kind", "load", "docker-image", "taxi-api")
        }
        exec {
            commandLine("kind", "load", "docker-image", "taxi-processor")
        }
    }
}

// === Deploy Kubernetes ===

tasks.register<Exec>("deployManifests") {
    group = "deploy"
    description = "Applies the Kubernetes manifests"
    commandLine("kubectl", "apply", "-f", "k8s/")
}

tasks.register("fullDeploy") {
    group = "deploy"
    description = "Cluster + images + manifests"
    dependsOn("setupKind", "loadImagesIntoKind", "deployManifests")
}

// === Debug ===

tasks.register<Exec>("viewPodStatus") {
    group = "debug"
    description = "Get pods from taxi-system namespace"
    commandLine("kubectl", "get", "pods", "-n", "taxi-system")
}

tasks.register<Exec>("viewLogs") {
    group = "debug"
    description = "Shows logs from every pod from taxi-system namespace"
    commandLine("kubectl", "logs", "-l", "app=taxi-api", "-n", "taxi-system", "--tail=100", "--follow")
}

// === Port-forward ===

tasks.register<Exec>("startPortForward") {
    group = "services"
    description = "Starts API port-forward in background"
    commandLine(runScriptForOS("scripts\\start-portforward.bat", "scripts/start-portforward.sh"))
}

tasks.register<Exec>("stopPortForward") {
    group = "services"
    description = "Terminates API port-forward"
    commandLine(runScriptForOS("scripts\\stop-portforward.bat", "scripts/stop-portforward.sh"))
}