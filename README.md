# Blog API

A RESTful Blog API built with **Spring Boot**, **Spring Security**, **MySQL**, **Redis**, **Apache Kafka**, and the **Transactional Outbox Pattern**.

The project focuses on building a backend system with authentication, authorization, caching, asynchronous event processing, and Dockerized infrastructure.

## Features

### Guest

* View published posts
* Search posts
* View post details
* Register an account

### Registered User

* Login with JWT
* Refresh access token
* Create posts
* Edit own posts
* Delete own posts
* Manage draft and published posts
* Comment on posts
* View own draft posts

### Administrator

* Manage users
* Manage posts
* Manage comments

## Tech Stack

* **Java 21**
* **Spring Boot 4.1.1**
* **Spring Web MVC**
* **Spring Data JPA**
* **Hibernate**
* **Spring Security**
* **JWT**
* **MySQL 8.4**
* **Redis 7**
* **Apache Kafka 4.0.1**
* **Spring Kafka**
* **SpringDoc OpenAPI / Swagger**
* **Docker & Docker Compose**
* **Lombok**

## Architecture

```text
                         Client
                           |
                           v
                    Spring Boot API
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
        MySQL            Redis            Kafka
          |                |                |
          |                |                v
          |                |          Kafka Consumer
          |                |                |
          |                |                v
          |                |          Activity Log
          |
          v
   Transactional Outbox
          |
          v
    Outbox Publisher
          |
          +-------------> Kafka
```

## Transactional Outbox Pattern

The project uses the **Transactional Outbox Pattern** to reliably publish events to Kafka.

When a post is created:

```text
Create Post
    |
    v
+---------------------------+
| Database Transaction      |
|                           |
|  Save Post                |
|  Save OutboxEvent         |
|  published = false       |
+---------------------------+
             |
             v
       Transaction Commit
             |
             v
      OutboxPublisher
        every 5 seconds
             |
             v
           Kafka
             |
             v
       Kafka Consumer
             |
             v
       ActivityLog
```

This prevents the application from losing an event when the database transaction succeeds but Kafka is temporarily unavailable.

If Kafka is unavailable, the `OutboxEvent` remains unpublished and will be retried later.

## Redis

Redis is used for:

* Post caching
* Refresh token storage

Example cache key:

```text
post::1
```

The application uses Spring Cache with Redis and configures a cache expiration time.

## Authentication & Authorization

The API uses JWT-based authentication.

Access token:

```text
Expiration: 15 minutes
```

Refresh token:

```text
Expiration: 7 days
```

Role-based authorization is used for protected operations.

Example roles:

```text
USER
ADMIN
```

Users can manage their own posts while administrators can manage posts across the system.

## Post Status

Posts support two states:

```text
DRAFT
PUBLISHED
```

### DRAFT

* Not visible to guests
* Can be viewed by the post owner
* Can be managed by the owner or administrator

### PUBLISHED

* Visible to guests
* Can be viewed publicly
* Public post details can be cached with Redis

## Database

Main entities:

```text
User
Post
Category
Comment
OutboxEvent
ActivityLog
```

Relationships:

```text
User 1 -------- * Post

User 1 -------- * Comment

Post 1 -------- * Comment

Post * -------- * Category
```

## API Documentation

Swagger UI is available when the application is running:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger can be used to test the REST API and authenticated endpoints with a JWT access token.

## Running Locally

### Requirements

* Java 21
* Maven
* Docker
* Docker Compose

### Start infrastructure

```bash
docker compose up -d
```

This starts:

```text
MySQL      localhost:3306
Redis      localhost:6379
Kafka      localhost:9092
Blog API   localhost:8080
```

Check containers:

```bash
docker compose ps
```

### Stop containers

```bash
docker compose down
```

> Do not use `docker compose down -v` unless you intentionally want to remove the persistent database and Redis volumes.

## Run Without Docker

Start MySQL, Redis, and Kafka separately, then run:

```bash
./mvnw spring-boot:run
```

The default application configuration uses:

```text
MySQL: localhost:3306
Redis: localhost:6379
Kafka: localhost:9092
```

## Project Structure

```text
src
└── main
    └── java
        └── com.example.blogapi
            ├── config
            ├── controller
            ├── dto
            ├── entity
            ├── event
            ├── exception
            ├── mapper
            ├── repository
            ├── security
            └── service
```

## Testing the Main Flow

A typical flow is:

```text
1. Register
      ↓
2. Login
      ↓
3. Receive JWT
      ↓
4. Create Category
      ↓
5. Create Post
      ↓
6. Post is saved as DRAFT
      ↓
7. OutboxEvent is created
      ↓
8. OutboxPublisher publishes event to Kafka
      ↓
9. Kafka Consumer processes event
      ↓
10. ActivityLog is created
```

## Docker Services

The project is fully containerized using Docker Compose.

Services:

```text
blog-api
blog-mysql
blog-redis
blog-kafka
```

Persistent Docker volumes are used for MySQL and Redis data.

## Future Improvements

Possible future improvements:

* Pagination and sorting for posts
* Better search with full-text search
* Post likes
* Comment pagination
* Admin dashboard
* Kafka retry/DLT monitoring
* Integration tests with Testcontainers
* CI/CD with GitHub Actions
* Production environment configuration
* API rate limiting
* Improved logging and monitoring

## License

This project is created for learning and portfolio purposes.
