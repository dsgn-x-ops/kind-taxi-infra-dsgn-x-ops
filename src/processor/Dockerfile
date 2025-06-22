# src/processor/Dockerfile
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /workspace/app
COPY . .
RUN ./gradlew :src:processor:build -x test

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/src/processor/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
