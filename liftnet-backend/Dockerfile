# ===== FASE 1: COMPILAR =====
FROM maven:3.9.7-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos el pom primero
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código
COPY src ./src

# Compilamos el JAR
RUN mvn clean package -DskipTests

# ===== FASE 2: EJECUTAR =====
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/liftnet-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
