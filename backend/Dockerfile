# ============================================================
# MES Backend - Production Docker Image
# Java 21 + Spring Boot (Multi-stage, Gradle Shadow JAR)
# ============================================================

FROM eclipse-temurin:21-jdk-alpine AS gradle-cleanup
WORKDIR /app
RUN apk add --no-cache unzip curl && \
    curl -fsSL -o gradle-bin.zip https://services.gradle.org/distributions/gradle-8.12-bin.zip && \
    unzip gradle-bin.zip && \
    rm gradle-bin.zip

# ============================================================
# Stage 1: Shadow JAR Build (Gradle)
# ============================================================
FROM gradle:8.12-jdk21 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .

RUN gradle shadowJar -x test --no-daemon

# ============================================================
# Stage 2: Runtime Image (JRE only)
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar

RUN addgroup -S spring && adduser -S spring -G spring && chown -R spring:spring /app
USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q "status.*UP"

CMD java \
     -XX:+UseG1GC \
     -XX:MaxRAMPercentage=75.0 \
     -Djava.security.egd=file:/dev/./urandom \
     -jar app.jar
