## Documentation: Tempo - Full Stack Application

## README for Backend

```markdown
# Backend: Prueba Full Stack

## Description
This project serves as the backend for the Tempo full-stack application, managing transactions and Tenpista entities.

## Prerequisites
- Java 17
- Maven
- PostgreSQL
- Docker (optional)

## Setup

### Local Development
1. Install PostgreSQL and create the database:
   ```sql
   CREATE DATABASE tenpista_db;
   ```
2. Add the following configuration to `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/tenpista_db
   spring.datasource.username=postgres
   spring.datasource.password=stefano00
   spring.jpa.hibernate.ddl-auto=create
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```
3. Run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Using Docker
1. Build the image:
   ```bash
   mvn clean package
   docker build -t stefano00123/prueba-fullstack:v1 .
   ```
2. Run the container:
   ```bash
   docker-compose up
   ```

## API Documentation
Swagger UI is available at:
```
http://localhost:8080/swagger-ui/index.html
```

## Tests
Run tests with:
```bash
mvn test
```

## Docker Hub
Pull the image from Docker Hub:
```bash
docker pull stefano00123/prueba-fullstack:v1
```
```


