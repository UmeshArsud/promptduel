# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this caches them for faster subsequent builds)
RUN ./mvnw dependency:go-offline -B

# Copy the actual source code
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render requires applications to listen on the port provided by the PORT env var (usually 10000)
# But Spring Boot defaults to 8080. We can expose 8080, but we will override it via Render's dashboard.
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
