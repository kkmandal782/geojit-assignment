# Request Processor Service

## Overview

Spring Boot application that:

* Accepts job requests via REST API
* Stores them in MySQL
* Queues them for async processing
* Sends them to an external HTTPS endpoint
* Uses Redis for caching
* Secured with Basic Authentication
* Processes jobs using a scheduled background worker

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Security (Basic Auth)
* Spring Data JPA (Hibernate)
* Spring Cache + Redis
* MySQL (Database)
* Docker & Docker Compose
* Maven

---

## How to Run

### Option 1: Docker Compose

```bash
docker-compose up --build
```

### Services:

* App → http://localhost:8080
* MySQL → localhost:3307
* Redis → localhost:6379
* External Api → https://localhost:9090

````

## Authentication

| Username | Password | Role |
|----------|----------|------|
| admin    | password | USER |

---

## API Endpoints

### 1. Submit Job
```bash
POST /submit
````

**Request**

```json
{
  "name": "job1",
  "job_creation_time": "2026-04-15"
}
```

**Response**

```
Queued For Https and inserted in db!!
```

---

### 2. Get All Jobs

```bash
GET /jobs
```

Requires: `ADMIN`

---

## Features

### Queue + Async Processing

* Jobs added to in-memory queue
* Scheduler processes queue every 5 seconds
* Processes queued jobs in background
* Calls external HTTPS endpoint asynchronously

### Redis Caching

* /jobs response is cached using Redis
* Cache key: jobs::all
* TTL: 10 minutes
* Cache is invalidated when a new job is created

### Validation

* Request validation using `@Valid`
* Constraints handled via Jakarta Validation

### Centralized Exception Handling

* Handles:

  * Validation errors (MethodArgumentNotValidException)
  * Duplicate job creation (JobAlreadyExistException)
  * Date parsing errors (DateTimeParseException)

---

## Testing Strategy

The project includes both Unit Testing and Integration Testing to ensure reliability and correctness.

---

### Unit Testing

* Framework: **JUnit 5 + Mockito**
* Scope:
  - Service layer logic
  - Business rules validation
  - Exception handling

#### Example Coverage:
- Duplicate job detection
- Successful job creation
- Repository interaction verification

---

### Integration Testing

* Framework: **Rest Assured**
* Scope:
  - End-to-end API testing
  - Controller → Service → Database flow
  - Authentication & authorization checks
  - Validation and exception responses

#### Covered Scenarios:
- Job submission success
- Unauthorized access (401)
- Role-based access control
- Validation failure (400)
- Duplicate job handling
- Invalid date format handling
- Redis caching performance validation

---


## Database Configuration

```properties
SPRING_DATASOURCE_PASSWORD=springstudent;
SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3307/geojitdb;
SPRING_DATASOURCE_USERNAME=springstudent;
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

---

## Cache Configuration

* Redis used as cache store
* TTL: 10 minutes

```properties 

SPRING_CACHE_TYPE=redis
SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PORT=6379

```

---

## Docker

Build image:

```bash
docker build -t request-processor-service .
```

Run container:

```bash
docker run -p 8080:8080 request-processor-service
```

---

## Running Tests

```bash
mvn test
```

---

## Project Structure

```
controller/
service/
repository/
model/
dto/
exception/
config/
scheduler/
mapper/
cache/
```
