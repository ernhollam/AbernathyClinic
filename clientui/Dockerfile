# Useamazoncorretto jdk as base image
FROM amazoncorretto:17-alpine3.18

# Create main application directory
WORKDIR /app

# Copy the jar from local machine to image
COPY target/*.jar clientui.jar

# Expose used port
EXPOSE 8083

# Define command to run java application
CMD ["java", "-jar", "clientui.jar"]