# Notification System - Backend

A Spring Boot-based notification system that processes messages from AWS SQS queues and delivers notifications to users through multiple channels (Email, SMS, Push) based on their subscription preferences.

## Architecture

This application follows an adaptation of **Hexagonal Architecture** (also known as Ports and Adapters), which separates business logic from external dependencies and makes the system more testable and maintainable.

For this project, a simplified version of this architecture is used due to the project size and to avoid excessive structural complexity. Note that in a production environment, additional changes would be recommended, such as adding explicit ports/interfaces for communication between layers to improve decoupling.

### Project Structure

```
backend/
├── src/main/java/com/example/notification/
│   ├── adapter/
│   │   ├── in/                          # Inbound adapters (driving ports)
│   │   │   ├── controller/              # REST controllers
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── MessageController.java
│   │   │   │   └── NotificationLogController.java
│   │   │   └── consumer/                # Message consumers
│   │   │       └── MessageConsumer.java # SQS message consumer
│   │   └── out/                         # Outbound adapters (driven ports)
│   │       └── repository/              # JPA repositories
│   │           ├── CategoryRepository.java
│   │           ├── NotificationLogRepository.java
│   │           └── UserRepository.java
│   ├── domain/                          # Business logic layer
│   │   ├── dto/                         # Data Transfer Objects
│   │   ├── model/                       # Domain entities
│   │   ├── service/                     # Business services
│   │   │   ├── CategoryService.java
│   │   │   ├── MessageService.java      # SQS message sender
│   │   │   └── NotificationService.java # Core notification logic
│   │   └── strategy/                    # Strategy pattern implementation
│   │       ├── NotificationStrategy.java
│   │       ├── NotificationStrategyFactory.java
│   │       ├── EmailStrategy.java
│   │       ├── SmsStrategy.java
│   │       └── PushStrategy.java
│   ├── infrastructure/                  # Infrastructure concerns
│   │   ├── config/
│   │   │   └── CorsConfig.java
│   │   └── exception/
│   │       └── CategoryNotSubscribedException.java
│   └── NotificationSystemApplication.java
└── src/main/resources/
    ├── application.properties
    ├── data.sql
    └── db/                              # Database initialization scripts
```

### Key Components

#### 1. **Message Flow**

- Messages are sent via REST API (`POST /v1/messages`)
- `MessageService` (domain service) sends messages to AWS SQS queue
- `MessageConsumer` (inbound adapter) listens to SQS queue and processes messages
- `NotificationService` handles the business logic for sending notifications

#### 2. **Strategy Pattern**

The system uses the Strategy pattern to support multiple notification channels:

- **EmailStrategy**: Handles email notifications
- **SmsStrategy**: Handles SMS notifications
- **PushStrategy**: Handles push notifications

The `NotificationStrategyFactory` dynamically selects the appropriate strategy based on the user's channel preferences.

#### 3. **Data Model**

- **UserEntity**: Represents users who can receive notifications
- **CategoryEntity**: Notification categories (e.g., Sports, Finance)
- **ChannelEntity**: Notification channels (Email, SMS, Push)
- **NotificationLogEntity**: Audit log of all sent notifications

#### 4. **SQS Integration**

- Uses Spring Cloud AWS SQS for message queue integration
- Configured to work with LocalStack for local development
- Queue name: `message-queue`

## Prerequisites

- **Java 21** (JDK 21)
- **Gradle 9.2.1** or higher (wrapper included)
- **Docker** and **Docker Compose** (for running LocalStack)

## Running the Application

### Option 1: Using Docker Compose (Recommended)

The easiest way to run the entire system including dependencies:

```bash
docker-compose up
```

**Note**: Make sure to run this command from the root directory (where `docker-compose.yaml` is located).

This will start:

- LocalStack (AWS SQS emulator) on port `4566`
- Backend application on port `8080`
- Frontend application on port `3000` (if included in docker-compose)

### Option 2: Running Locally

1. **Start LocalStack** (for SQS):

   ```bash
   docker run -d -p 4566:4566 localstack/localstack
   ```

2. **Create SQS Queue**:

   ```bash
   aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name message-queue --region us-east-1
   ```

3. **Build and Run**:

   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

   Or simply run without building first:

   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`.

### Environment Variables

You can override default configuration using environment variables:

- `SPRING_CLOUD_AWS_SQS_ENDPOINT`: SQS endpoint URL (default: `http://localhost:4566`)
- `SPRING_CLOUD_AWS_REGION_STATIC`: AWS region (default: `us-east-1`)
- `SPRING_CLOUD_AWS_CREDENTIALS_ACCESS_KEY`: AWS access key (default: `test`)
- `SPRING_CLOUD_AWS_CREDENTIALS_SECRET_KEY`: AWS secret key (default: `test`)

## H2 Database Console

The application uses **H2 in-memory database** for development and testing. The H2 console is enabled by default.

### Accessing H2 Console

1. **URL**: `http://localhost:8080/h2-console`

2. **Connection Settings**:

   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa` (or leave empty)
   - **Password**: (leave empty)
   - Click **Connect**

### Database Initialization

The database is automatically initialized on startup using:

- SQL schema files in `src/main/resources/db/`
- Data files: `data.sql` and SQL files in `db/` directory

The initialization scripts load:

- Users and their subscriptions
- Categories
- Notification channels
- Sample data

## API Endpoints

### Send Message

```http
POST /v1/messages
Content-Type: application/json

{
  "category": "Sports",
  "body": "Your team won!"
}
```

### Get Notification Logs

```http
GET /v1/notifications/logs?page=0&size=10
```

### Get Categories

```http
GET /v1/categories
```

## Technologies

- **Spring Boot 3.4.1**: Application framework
- **Spring Data JPA**: Database access
- **Spring Cloud AWS**: AWS services integration
- **H2 Database**: In-memory database
- **Lombok**: Reducing boilerplate code
- **Gradle**: Build tool

## Development

### Building the Application

```bash
./gradlew build
```

### Running Tests

The project includes comprehensive unit tests using JUnit 5 and Mockito. All external dependencies (repositories, SQS, etc.) are mocked to ensure fast, isolated unit tests.

#### Run All Unit Tests

```bash
./gradlew test
```

#### Run Specific Test Class

```bash
./gradlew test --tests "CategoryServiceTest"
./gradlew test --tests "MessageControllerTest"
./gradlew test --tests "NotificationServiceTest"
```

#### Run Specific Test Method

```bash
./gradlew test --tests "MessageControllerTest.send_WithValidRequest_ShouldReturnAccepted"
```

### Test Coverage

The project uses **JaCoCo** for code coverage reporting. Coverage reports are automatically generated after running tests.

#### Run Tests with Coverage Report

```bash
./gradlew test jacocoTestReport
```

#### View Coverage Reports

After running tests with coverage, open the HTML report:

**On Linux/Mac:**
```bash
xdg-open build/reports/jacoco/test/html/index.html
```

**On Windows:**
```bash
start build/reports/jacoco/test/html/index.html
```

Or manually navigate to:
```
backend/build/reports/jacoco/test/html/index.html
```

#### Coverage Report Locations

- **HTML Report** (interactive): `build/reports/jacoco/test/html/index.html`
- **XML Report** (for CI/CD): `build/reports/jacoco/test/jacocoTestReport.xml`

#### Viewing Test Results

Test execution results are available at:
```
build/reports/tests/test/index.html
```

Open this file in your browser to see:
- Test execution summary
- Passed/Failed tests
- Test execution time
- Detailed error messages for failed tests

### Test Coverage Summary

The unit tests provide:
- **100% coverage** for all service classes
- **100% coverage** for all controller classes
- **100% coverage** for all consumer classes
- Comprehensive edge case testing (null values, empty lists, exceptions)

Coverage reports automatically exclude:
- DTOs (Data Transfer Objects)
- Model/Entity classes
- Configuration classes
- Exception classes
- Application main class

### Building Docker Image

```bash
docker build -t notification-system-backend .
```
