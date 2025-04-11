# Energy Optimization Backend

A comprehensive Spring Boot application for IoT device management and energy optimization.

## 🌟 Overview

The Energy Optimization Backend is a robust and secure REST API designed to manage IoT devices for energy monitoring and optimization. The system provides secure APIs for device management, real-time status monitoring, user authentication, and service-to-service communication.

## 🛠️ Technology Stack

### Core Technologies
- **Spring Boot**: Framework for building Java-based enterprise applications
- **Maven**: Build automation tool and dependency management
- **Java**: JDK 17
- **PostgreSQL**: Relational database for data storage
- **JPA/Hibernate**: ORM framework for database access

### Security
- **JWT**: JSON Web Tokens for secure authentication
- **Spring Security**: Security framework for authentication and authorization
- **HTTP-Only Cookies**: Protection of refresh tokens against XSS attacks

### API & Documentation

- **RESTful API**: Designed using standard HTTP methods for clean and intuitive resource management.
- **Swagger/OpenAPI**: Provides an interactive interface for exploring and testing API endpoints.

🔗 [View Swagger Documentation](https://energy-optimisation-backend.onrender.com/swagger-ui/index.html#/)

### Additional Technologies
- **Caffeine**: In-memory caching for performance optimization
- **Server-Sent Events (SSE)**: Real-time communication protocol for device status updates
- **Lombok**: Java library to reduce boilerplate code
- **Spring Mail**: Email service integration

## ✨ Features

### Authentication & Authorization
- JWT-based authentication with access and refresh tokens
- Role-based authorization (HOUSE_OWNER and ADMIN roles)
- API key authentication for service-to-service communication
- Secure token renewal flow

### Device Management
- CRUD operations for IoT devices
- Real-time device status updates via Server-Sent Events
- Energy consumption monitoring
- Device grouping by house/property

### House/Property Management
- CRUD operations for houses/properties
- Association of devices with properties
- User-to-house relationship management

### User Management
- User registration and authentication
- Email verification
- Password reset functionality
- Profile management

### Email Notifications
- Email verification for new users
- Password reset emails
- System notifications

## 🏛️ Architecture

### Layered Architecture
- **Model Layer**: Defines the domain entities and data structures
- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Data access layer

### Security Architecture
- **JWT Authentication**: Token-based access (expiration: 24 hours)
- **CORS Configuration**: Secures cross-origin requests
- **Global Exception Handling**: Centralized error management

### Database Design
The application uses several key entities:
- **User**: System users with roles and account details
- **House**: Properties with relationships to users and devices
- **Device**: IoT devices with their properties and relationships
- **EnergyReading**: Stores energy consumption readings from devices
- **ApiKey**: Service-to-service authentication keys
- **RefreshToken**: JWT refresh tokens
- **EmailVerification**: Email verification tokens

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.6+ or compatible build tool
- PostgreSQL database
- Docker (optional, for containerized deployment)

### Configuration
The application uses environment variables for configuration. Here are the key variables needed:

```properties
# Server Configuration
APP_BASE_URL=http://localhost:8080

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=energy_optimization
DB_USER=postgres
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your_jwt_secret_key

# Email Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_email_password

# CORS Configuration
ALLOWED_ORIGINS=http://localhost:3000,http://127.0.0.1:5500

# External Service Configuration
FASTAPI_SERVER_URL=http://localhost:8000
```

### Installation & Running

#### Method 1: Running with Maven

1. Clone the repository:
```bash 
git clone https://github.com/yourusername/energy-optimization-backend.git
cd energy-optimization-backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR file directly:
```bash
java -jar target/energy-optimization-backend-0.0.1-SNAPSHOT.jar
```

#### Method 2: Docker Deployment

1. Create a Dockerfile in your project root with the following content:
```dockerfile
FROM openjdk:21-slim
WORKDIR /app
COPY target/energy-optimization-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

2. Build the project with Maven:
```bash
mvn clean package
```

3. Build Docker image:
```bash
docker build -t energy-optimization-backend .
```

4. Run Docker container:
```bash
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=energy_optimization \
  -e DB_USER=postgres \
  -e DB_PASSWORD=your_password \
  -e JWT_SECRET=your_jwt_secret \
  -e MAIL_USERNAME=your_email@gmail.com \
  -e MAIL_PASSWORD=your_email_password \
  -e ALLOWED_ORIGINS=http://localhost:3000 \
  -e FASTAPI_SERVER_URL=http://host.docker.internal:8000 \
  -e APP_BASE_URL=http://localhost:8080 \
  energy-optimization-backend
```

### Docker Compose (Optional)

For a more complete setup including PostgreSQL, create a `docker-compose.yml` file:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=energy_optimization
      - DB_USER=postgres
      - DB_PASSWORD=postgres
      - JWT_SECRET=your_jwt_secret
      - MAIL_USERNAME=your_email@gmail.com
      - MAIL_PASSWORD=your_email_password
      - ALLOWED_ORIGINS=http://localhost:3000
      - FASTAPI_SERVER_URL=http://fastapi:8000
      - APP_BASE_URL=http://localhost:8080
    depends_on:
      - postgres
    networks:
      - backend-network

  postgres:
    image: postgres:14
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=energy_optimization
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - backend-network

networks:
  backend-network:
    driver: bridge

volumes:
  postgres-data:
```

Run with:
```bash
docker-compose up -d
```

## 📝 API Documentation

The API documentation is available through Swagger UI when the application is running:

```
http://localhost:8080/swagger-ui/index.html
```

### Complete API Endpoints Reference

#### Authentication and User Management
- `POST /api/users/login`: Authenticate a user
- `POST /api/users/register`: Register a new user
- `POST /api/users/logout`: Logout a user
- `POST /api/users/renew-token`: Renew access token
- `POST /api/users/forgot-password`: Forgot password
- `POST /api/users/reset-password`: Reset password
- `PUT /api/users/change-password`: Change password
- `PUT /api/users/profile-status`: Update user profile status
- `GET /api/users/all`: Get all users by profile status

#### House Management
- `GET /api/houses`: Get all houses
- `GET /api/houses/{houseId}`: Get house by ID
- `POST /api/houses`: Create a new house
- `PUT /api/houses/{houseId}`: Update a house
- `DELETE /api/houses/{houseId}`: Delete a house

#### Device Management
- `GET /api/devices`: Get all devices
- `GET /api/devices/{id}`: Get device by ID
- `POST /api/devices`: Create a new device
- `PUT /api/devices/{id}`: Update a device
- `DELETE /api/devices/{id}`: Delete a device

#### Device Status Management
- `GET /api/device-status/house/{houseId}/status`: Get house device statuses
- `GET /api/device-status/house/{houseId}/subscribe`: Subscribe to house updates (SSE)
- `POST /api/device-status/{deviceId}/toggle`: Toggle device status

#### API Key Management (Admin Only)
- `GET /api/admin/api-keys`: Get all API keys
- `GET /api/admin/api-keys/{id}/details`: Get API key details
- `POST /api/admin/api-keys`: Generate a new API key
- `PUT /api/admin/api-keys/{id}/status`: Toggle API key status
- `DELETE /api/admin/api-keys/{id}`: Delete an API key

#### System Monitoring
- `GET /api/health`: Health check endpoint

#### Request Forwarding
- `POST /api/forward`: Forward a request to external service

## 📊 Project Structure

```
energy-optimization-backend/
├── Dockerfile                   # Docker configuration
├── docker-compose.yml           # Optional: Docker Compose config
└── src/main/java/hpe/energy_optimization_backend/
    ├── config/                  # Application configuration classes
    ├── controller/              # REST API controllers
    ├── dto/                     # Data Transfer Objects
    │   ├── request/             # Request DTOs
    │   └── response/            # Response DTOs
    ├── enums/                   # Enumeration classes
    ├── exception/               # Custom exceptions
    │   ├── houseAndDevice/      # House and device related exceptions
    │   ├── token/               # Authentication token exceptions
    │   └── user/                # User related exceptions
    ├── mapper/                  # Entity-DTO mappers
    ├── model/                   # JPA entity models
    ├── repository/              # Data access repositories
    ├── security/                # Security configurations
    │   ├── filter/              # Custom security filters
    │   └── jwt/                 # JWT implementation
    ├── service/                 # Business logic services
    │   └── Impl/                # Service implementations
    ├── urlMapper/               # URL mapping constants
    └── EnergyOptimizationBackendApplication.java
```

## 🔒 Security Considerations

- All passwords are stored using BCrypt hashing
- JWT tokens have appropriate expiration times (24 hours)
- CORS is properly configured for frontend integration
- Input validation is implemented across all endpoints
- Role-based access control restricts sensitive operations
- House-specific data access ensures proper data isolation

## 📄 License

[MIT License](LICENSE)
