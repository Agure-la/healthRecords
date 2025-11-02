# Patient Management API

This is a Spring Boot application that provides RESTful endpoints for managing patients, their encounters, and observations. The application uses **PostgreSQL** as the database and provides **Swagger UI** documentation for easy API exploration.

---

## Technologies

- Java 22
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI 3
- Maven

---

## Features / Endpoints

### Patients
- **POST** `/api/patients` — Create a new patient (with optional encounters and observations)
- **GET** `/api/patients/{id}` — Retrieve patient details
- **PUT** `/api/patients/{id}` — Update patient details
- **DELETE** `/api/patients/{id}` — Delete patient and related data
- **GET** `/api/patients` — Search patients with filters:
    - `family` — family name
    - `given` — given name
    - `identifier` — patient identifier
    - `birthDate` — exact date of birth
    - `startDate` / `endDate` — birth date range
    - `page` / `size` / `sort` — pagination and sorting
- **GET** `/api/patients/{id}/encounters` — Get paginated encounters for a patient
- **GET** `/api/patients/{id}/observations` — Get all observations for a patient

### Encounters
- Returned as part of patient details
- Includes start/end times, class, and linked observations

### Observations
- Can be linked to an encounter or directly to a patient
- Includes code, value, effective date, and references to patient and encounter

---

## Database Configuration (PostgreSQL)

Configure `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/digital_health
    username: yourusername
    password: yourpassword
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  jackson:
    serialization:
      write-dates-as-timestamps: false

server:
  port: 9000


