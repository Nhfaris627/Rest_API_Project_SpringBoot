# Books API - Spring Boot REST Application

A comprehensive REST API for managing books and authors built with Spring Boot, JPA, and PostgreSQL.

## Features

- **Author Management**: Create, read, update, and delete authors
- **Book Management**: Manage books with full CRUD operations  
- **Pagination Support**: Paginated book listings
- **Data Validation**: Input validation using Spring Boot Validation
- **Database Integration**: PostgreSQL for production, H2 for testing
- **Comprehensive Testing**: Unit and integration tests included

## Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17  
- **Database**: PostgreSQL (Production), H2 (Testing)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Testing**: JUnit 5, MockMvc
- **Utilities**: Lombok, ModelMapper
- **Containerization**: Docker Compose for database setup

## Architecture

The project follows a layered architecture pattern:

```
├── controllers/     # REST endpoints
├── services/        # Business logic  
├── repositories/    # Data access layer
├── domain/
│   ├── entities/    # JPA entities
│   └── dto/         # Data transfer objects
├── mappers/         # Entity-DTO mapping
└── config/          # Configuration classes
```

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+  
- Docker & Docker Compose (for database)

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd Rest_API_Project
```

### 2. Start PostgreSQL Database

```bash
cd database
docker-compose up -d
```

This will start a PostgreSQL instance with:

- **Port**: 5432
- **Database**: postgres  
- **Username**: postgres
- **Password**: changemeinprod!

### 3. Run the Application

```bash
# From the root directory
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### 4. Run Tests

```bash
./mvnw test
```

## API Endpoints

### Authors

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/authors` | Create a new author |
| `GET` | `/authors` | List all authors |  
| `GET` | `/authors/{id}` | Get author by ID |
| `PUT` | `/authors/{id}` | Update author (full) |
| `PATCH` | `/authors/{id}` | Update author (partial) |
| `DELETE` | `/authors/{id}` | Delete author |

### Books

| Method | Endpoint | Description |
|--------|----------|-------------|
| `PUT` | `/books/{isbn}` | Create or update book |
| `GET` | `/books` | List all books (paginated) |
| `GET` | `/books/{isbn}` | Get book by ISBN |  
| `PATCH` | `/books/{isbn}` | Update book (partial) |
| `DELETE` | `/books/{isbn}` | Delete book |

## API Examples

### Create an Author

```bash
curl -X POST http://localhost:8080/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "J.K. Rowling",
    "age": 58
  }'
```

### Create a Book

```bash
curl -X PUT http://localhost:8080/books/978-0-7475-3269-9 \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-0-7475-3269-9",
    "title": "Harry Potter and the Philosopher'\''s Stone",
    "author": {
      "id": 1,
      "name": "J.K. Rowling", 
      "age": 58
    }
  }'
```

### Get All Books (Paginated)

```bash
curl "http://localhost:8080/books?page=0&size=10"
```

## Database Schema

### Authors Table

```sql
CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    age INTEGER
);
```

### Books Table

```sql
CREATE TABLE books (
    isbn VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    author_id BIGINT REFERENCES authors(id)
);
```

## Configuration

### Application Properties

**Production** (`src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=changemeinprod!
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
```

**Testing** (`src/test/resources/application.properties`):

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create
```

## Testing

The project includes comprehensive tests:

- **Repository Tests**: Integration tests for data layer
- **Controller Tests**: Integration tests for REST endpoints  
- **Service Tests**: Unit tests for business logic

### Run Specific Test Categories

```bash
# Run all tests
./mvnw test

# Run only integration tests
./mvnw test -Dtest="**/*IntegrationTests"

# Run specific test class  
./mvnw test -Dtest=AuthorControllerIntegrationTests
```

## Project Structure

```
Rest_API_Project/
├── database/                          # Database setup
│   ├── docker-compose.yml            # PostgreSQL container
│   └── src/                          # Original project structure
├── src/
│   ├── main/
│   │   ├── java/com/devtiro/database/
│   │   │   ├── controllers/          # REST controllers
│   │   │   ├── services/             # Business logic
│   │   │   ├── repositories/         # JPA repositories  
│   │   │   ├── domain/
│   │   │   │   ├── entities/         # JPA entities
│   │   │   │   └── dto/              # Data transfer objects
│   │   │   ├── mappers/              # Entity-DTO mappers
│   │   │   └── config/               # Configuration
│   │   └── resources/
│   │       ├── application.properties
│   │       └── banner.txt
│   └── test/                         # Test classes
├── pom.xml                           # Maven dependencies
└── README.md
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK` - Successful GET/PUT/PATCH operations
- `201 Created` - Successful POST operations
- `204 No Content` - Successful DELETE operations  
- `404 Not Found` - Resource not found
- `400 Bad Request` - Invalid input data

## Deployment

### Using Maven

```bash
# Build the application
./mvnw clean package

# Run the JAR file
java -jar target/database-0.0.1-SNAPSHOT.jar
```

### Using Docker

You can containerize the application by creating a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/database-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)  
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Related Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)  
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Built with ❤️ using Spring Boot**
