# Useamazoncorretto jdk as base image
FROM amazoncorretto:17-alpine3.18

# Create main application directory
WORKDIR /app

# Copy the jar from local machine to image
COPY target/*.jar history.jar

# Expose used port
EXPOSE 8082

# Define command to run java application
CMD ["java", "-jar", "history.jar"]