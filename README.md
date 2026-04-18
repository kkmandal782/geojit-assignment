# Request Processor Service

## Overview

Spring Boot application that:

* Accepts job requests via REST API
* Stores them in MySQL
* Queues them for async processing
* Sends them to an external HTTPS endpoint
* Uses Redis for caching
* Secured with Basic Auth

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Redis (cache)
* MySQL (database)
* Docker & Docker Compose

---

## How to Run

### Option 1: Docker Compose (Recommended)

```bash
docker-compose up --build
```

Services:

* App → http://localhost:8080
* MySQL → localhost:3307
* Redis → localhost:6379
* External Api → https://localhost:9090

````

---

## Authentication

| Username | Password | Role  |
|--------|--------|------|
| admin  | password | ADMIN |
| user   | password | USER  |

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
* Calls external HTTPS endpoint asynchronously

### Redis Caching

* `/jobs` response cached
* TTL: 10 minutes
* Cache invalidated on new job

### Validation

* Request validation using `@Valid`
* Custom error responses

### Centralized Exception Handling

* Handles:

  * Validation errors
  * Duplicate jobs
  * Date parsing errors

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

SPRING_CACHE_TYPE=redis;
SPRING_DATA_REDIS_HOST=redis;
SPRING_DATA_REDIS_PORT=6379;

```

---

## Docker

Build image:

```bash
docker build -t request-processor .
```

Run container:

```bash
docker run -p 8080:8080 request-processor
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
```
