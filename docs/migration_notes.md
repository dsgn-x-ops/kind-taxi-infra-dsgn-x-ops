# Migration Notes – Maven to Gradle Kotlin DSL

## 1. Migration Overview

- Migrated from Maven (`pom.xml`) to Gradle Kotlin DSL (`build.gradle.kts`) using Gradle 8.5
- Restructured project into a **multi-module setup**:
    - `src/api` – Spring Boot REST API
    - `src/processor` – Spring Boot message processor
- Root-level files added:
    - `settings.gradle.kts`
    - `build.gradle.kts` (root and per module)

---

## 2. Issues Encountered

- **Java and Gradle not detected inside WSL**  
  → Installed OpenJDK 17 and Gradle using `apt` and `sdkman`

- **Missing wrapper scripts**  
  → Generated via `gradle wrapper`

- **Gradle could not detect a valid build**  
  → Discovered that `settings.gradle.kts` and `build.gradle.kts` were placed incorrectly inside `/src` instead of project root

- **Build failed due to missing domain classes in `processor`**  
  → Code referenced `TaxiRide`, `Location`, etc., which had not yet been implemented

---

## 3. Solutions Adopted

- Installed Java and Gradle properly inside WSL
- Generated wrapper files for consistent Gradle builds across environments
- Moved Gradle configuration files to the correct root location
- Created temporary **stub classes** in the `api` and `processor` module to allow compilation to succeed
- Successfully validated the multi-project structure with `./gradlew build`

---

## 4. Notes

- The legacy Maven `pom.xml` files will be deleted once migration is complete
- CI/CD validation will be added via GitHub Actions to validate pull requests and enforce code quality
- Work is being done on feature branches (`feat/*`) and integrated into `dev` for stability and traceability
