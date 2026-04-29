# ========================================
# Build stage — compilar com Maven
# ========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar POM primeiro (cache de dependências)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código-fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ========================================
# Runtime stage — imagem mínima
# ========================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Metadados
LABEL maintainer="João Vitor Lacerda Consorte"
LABEL description="VaultBank API — CP2 DevOps FIAP"

# Copiar JAR
COPY --from=build /app/target/*.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
