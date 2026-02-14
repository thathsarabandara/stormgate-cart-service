# Multi-stage build for optimal image size

# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml and download dependencies (cacheable layer)
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Stormgate Team"
LABEL description="Cart Microservice for Stormgate E-commerce Platform"

# Create a non-root user for security
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/cart/health || exit 1

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
CMD []
