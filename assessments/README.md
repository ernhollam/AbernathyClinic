# Assessments microservice

This microservice is an API which is part of the Mediscreen project, a web application which helps to assess a patient's
risk
of developing diabetes given their history.

## Summary

With this API you can assess a patient's risk of developing diabetes given their personal information and history of
notes.

## Getting started

### Prerequisites

Things you need to install the software and how to install them:

- Java JDK 17.0.1
- SpringBoot 2.7.13
- Maven 3.8.4

Both patients and history microservices must be run before using the assessment microservice.

### Running App

#### Cloning the project to your local environment

Import the code into an IDE of your choice, run PatientsApplication.java, HistoryApplication and
AssessmentsApplication.java to launch the application.

### Testing

*assessments* has unit tests.

To run the tests and generate the JaCoCo and Surefire reports, in your IDE Terminal, run the following command:
`mvn clean verify site`.
This will generate a `site` folder within the `target` folder. By opening the `index.html` file, you'll land on the
summary page of project information.

## Usage

### Documentation

Once the microservice is running, the documentation can be found on the
link http://localhost:8083/swagger-ui/index.html.

### Available endpoints

`GET /assess/{patientId}`
`GET /asses/familyName`