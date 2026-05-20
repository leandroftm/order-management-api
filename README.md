# Order Management API

A RESTful backend application for managing users, categories, products, and orders, built with Java and Spring Boot.

This project was developed as a practical study project focused on backend architecture, authentication/authorization, database migrations, testing, and REST API best practices.

## Technologies
```
Java 17
Spring Boot 3.5.*
Spring Security
JWT Authentication
Spring Data JPA
Lombok
PostgreSQL
Flyway
H2
Swagger / OpenAPI
JUnit 5
Mockito
Maven
```

## Features

### Authentication & Authorization
```
- User registration
- JWT authentication
- Role-based authorization (USER / ADMIN)
- Protected endpoints using Spring Security
```

## Users
```
- Get authenticated User
- Enable/disable Users
- Filter Users by Role
- Admin-only management endpoints
```

## Categories

```
Create Categories
Enable/Disable Categories
Update Category data
List all Categories
```

Products

```
Create Products inside Categories
Update Product information
Update Product price
Increase/Decrease Product stock
Enable/Disable Products
Product listing with pagination
```

Orders

```
Create Orders
Cancel Orders
Pay Orders
List authenticated User Orders
Filter Orders by Status
Admin Order management
```

## Security

Authentication is handled using JWT tokens.

Protected endpoints require:

```http
Authorization: Bearer <token>
```

Authorization is role-based using Spring Security method security:

```java
@PreAuthorize("hasRole('ADMIN')")
```

## Database

The project uses PostgreSQL as the main database and Flyway for versioned database migrations and H2 database for tests.

### Flyway

Database schema creation and updates are managed through migration scripts located at:

```
src/main/resources/db/migration
```

## Environment Variables

The application uses environment variables for sensitive configuration.

Example:

```properties

DB_HOST=localhost
DB_USER=postgres
DB_PASSWORD=postgres
JWT_SECRET_KEY=your-secret-key
```

## Application Properties
```properties
spring.application.name=order-management-api

spring.datasource.url=jdbc:postgresql://${DB_HOST}:5432/order_management
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=none

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

jwt.secret=${JWT_SECRET_KEY}
```

## Running the Project

### 1. Clone repository

```bash
git clone <repository-url>
```

### 2. Configure PostgreSQL

Create database:

```sql
CREATE DATABASE order_management;
```

### 3. Configure environment variables

Example:

```properties
DB_HOST=localhost
DB_USER=postgres
DB_PASSWORD=postgres
JWT_SECRET_KEY=my-secret-key
```

### 4. Run application

```bash
mvn spring-boot:run
```

## Swagger / API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

## Example Authentication Flow
### Register

```http
POST /auth/register
```
```json
{
  "name": "Admin User",
  "email": "admin@email.com",
  "password": "123456"
}
```
