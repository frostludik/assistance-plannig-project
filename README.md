
# Assistance Planning Backend

## Overview

This project is a backend service for managing assistance planning. It is built using Java, Spring Boot, and Maven. The project includes various services and repositories to handle different aspects of the application, such as managing predefined meetings and weeks.

This project is implemented as a microservice running in AWS. The main application serves as a planning, overview, and managing tool for charity organizations. They can plan workload for their employees, plan meetings with customers, etc. This part of the project serves for planning meetings between charity employees and customers, similar to calendar planning. You can also use pre-planned dates.

## Technologies Used

- **Java**
- **Spring Boot**
- **Maven**
- **SQL**

## Project Structure

The project is organized into several packages:

- `cz.echarita.assistance_planning_backend.model`: Contains the entity classes.
- `cz.echarita.assistance_planning_backend.repository`: Contains the repository interfaces for database operations.
- `cz.echarita.assistance_planning_backend.service`: Contains the service classes that implement the business logic.
- `cz.echarita.assistance_planning_backend.controller`: Contains the controller classes that handle HTTP requests.
- `cz.echarita.assistance_planning_backend.dto`: Contains the Data Transfer Objects (DTOs) used for transferring data between layers.

## Key Classes and Their Responsibilities

### `MeetingService.java`

Purpose: Manages operations related to Meeting entities, including creating, updating, deleting, and fetching meetings. It ensures that meeting times are valid and do not overlap with existing meetings.


### `PredefinedMeetingService.java`

Purpose: Manages operations related to PredefinedMeeting entities, including defining new predefined meetings, fetching meetings for specific customers or weeks, and updating meeting details. It also handles the calculation of planned hours for customers.


### `CustomerMeetingService.java`

Purpose: Manages operations related to customer meetings, including sorting and splitting predefined meetings into active and inactive lists based on the given date. It checks the active status of meetings for customers.

## Logging

The project has detailed logging implemented using the SLF4J library.



## Tests

The project includes unit and integration tests using the following libraries and tools:  
- *JUnit: For writing and running unit tests.*
- *Mockito: For mocking dependencies in unit tests.*
- *Spring Boot Test: For integration testing with Spring Boot.*



## Database

The project is prepared for use with an MySQL database as well as H2 in-memory database for testing purposes. 

## Swagger Annotations

The project uses Swagger annotations for API documentation.



