# Start image with amazoncorretto jdk base image
FROM amazoncorretto:17-alpine3.18

# Create main application directory
WORKDIR /app

# Copy the jar from local machine to image
COPY target/*.jar patients.jar

# Expose used port
EXPOSE 8081

# Define command to run java application
CMD ["java", "-jar", "patients.jar"]