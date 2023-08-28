# Patients microservice

This microservice is an API which is part of the Mediscreen project, a web application which helps to assess a patient's
risk
of developing diabetes given their history.

## Summary

With this API you can manage your patients.
Patients are saved in a SQL database.

Basic CRUD operations are available at the `/patient` endpoint.

## Getting started

### Prerequisites

Things you need to install the software and how to install them:

- Java JDK 17.0.1
- SpringBoot 2.7.13
- Maven 3.8.4
- MySQL

### Running App

#### Cloning the project to your local environment

Import the code into an IDE of your choice, create then populate the database and run PatientsApplication.java to launch
the application.

#### Creating and populating the database

Initialize the database by running the SQL script `/src/main/resources/database/initDb.sql`.

### Testing

*patients* microservice has unit tests.

To run the tests and generate the JaCoCo and Surefire reports, in your IDE Terminal, run the following command:
`mvn clean verify site`.
This will generate a `site` folder within the `target` folder. By opening the `index.html` file, you'll land on the
summary page of project information.

## Usage

### Documentation

Once the microservice is running, the documentation can be found on the
link http://localhost:8081/swagger-ui/index.html.

### CRUD endpoints

Basic Create, Read, Update and Delete operations are available on the app's entities at the endpoint `/patient`

### Available endpoints

`POST /patient`
`GET /patient/familyName`
`GET /patient/{id}`
`GET /patient`
`PUT /patient/{id}`
`DELETE /patient/{id}`