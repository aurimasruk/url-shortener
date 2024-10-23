# URL Shortener API

## Description
This application provides a REST API to shorten URLs. Users can shorten a URL and retrieve the original URL using a shortened code.

## Prerequisites
- **Java 17**
- **Maven 3.x**
- **Docker** (optional for containerization)

---
## How to Run the Project

### 1. Running with Maven
To run the application locally, ensure you have the necessary dependencies and run the following command:

```bash
mvn spring-boot:run
```

The application will be available at ``http://localhost:8080``

<br>


### 2. Running with Docker
To build and run the application inside a Docker container, follow these steps:

#### 1. Compile the project into a JAR file:
```bash
mvn clean package
```
#### 2. Build the Docker image:
```bash
docker build -t urlshortener-app .
```
#### 3. Run the Docker container:
```bash
docker run -p 8080:8080 urlshortener-app
```

<br>

### 3. Swagger API Documentation
Once the application is running, you can access the Swagger UI for API documentation and testing at:
```bash
http://localhost:8080/swagger-ui/index.html
```
---
## API Endpoints

### 1. Shorten a URL
- **POST** `/api/shorten`
- Request body: original URL
- Response: Shortened URL

Example request: ``"url": "https://www.google.com"``

Example response: ``"abcd123"``

### 2. Retrieve Original URL
- **GET** `/api/{shortenedUrl}`
- Response: Original URL or error message if the shortened URL does not exist.

Example request: ``GET /api/abcd123``

Example response: ``"url": "https://www.google.com"``

---
## Unit Tests
Unit tests are provided for:

- Controller logic (UrlControllerTest)
- Service logic (UrlServiceTest)

To run the tests, execute the following command:

```bash
mvn test
```

---

## Technologies Used

- **Spring Boot** for the application framework
- **H2 Database** for in-memory database
- **Spring Data JPA** for database interaction
- **Swagger** for API documentation
- **JUnit** for unit testing
- **Docker** for containerization