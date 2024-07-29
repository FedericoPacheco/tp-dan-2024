# Stage 1: Build Maven dependencies
FROM maven:3.9.6-eclipse-temurin-21 AS dependencies
WORKDIR /app

# Copy only the pom.xml file to cache dependencies
COPY pom.xml .

# Run the go-offline goal to download all dependencies
RUN mvn dependency:go-offline

# Stage 2: Build the JAR file
FROM maven:3.9.6-eclipse-temurin-21  AS build
WORKDIR /app

# Copy the cached dependencies from the first stage
COPY --from=dependencies /root/.m2 /root/.m2

# Copy the rest of the project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Stage 3: Run the JAR file
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
