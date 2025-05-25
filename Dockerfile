# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar el archivo de configuración de Maven y el wrapper
COPY ./pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Corregir los finales de línea y establecer permisos
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

# Descargar las dependencias (capa de caché)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente de la aplicación
COPY ./src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción (builder)
COPY --from=builder /app/target/newrelic/newrelic.yml /app/newrelic.yml
COPY --from=builder /app/target/wowlibre-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/target/newrelic/newrelic.jar .

ENV NEW_RELIC_LICENSE_KEY=82ff848c92a86cfccb796e3c22d3b01aFFFFNRAL
ENV SPRING_PROFILES_ACTIVE=prod
ENV NEW_RELIC_APP_NAME=wow-libre-core
ENV NEW_RELIC_LOG_LEVEL=info

EXPOSE 8091

ENTRYPOINT ["java", "-javaagent:newrelic.jar", "-jar", "wowlibre-0.0.1-SNAPSHOT.jar"]
