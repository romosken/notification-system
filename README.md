# Notification System

A full-stack notification system that processes messages from AWS SQS queues and delivers notifications to users through multiple channels (Email, SMS, Push) based on their subscription preferences.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
  - [Installing Docker](#installing-docker)
- [Quick Start](#quick-start)
- [Running the Application](#running-the-application)
- [Testing the Application](#testing-the-application)
- [Project Structure](#project-structure)
- [Services Overview](#services-overview)
- [API Documentation](#api-documentation)
- [Troubleshooting](#troubleshooting)

## Overview

This notification system consists of:

- **Backend**: Spring Boot application that processes messages via SQS and "sends" notifications
- **Frontend**: React application for sending messages and viewing notification logs
- **LocalStack**: AWS services emulator (SQS) for local development
- **H2 Database**: In-memory database for storing users, categories, and notification logs

### Key Features

- Send messages via REST API or web interface
- Messages are processed asynchronously through AWS SQS
- Support for multiple notification channels (Email, SMS, Push)
- User subscriptions to categories
- Notification log history with pagination
- Dead Letter Queue (DLQ) for failed messages (5 retries)

## Architecture

The system follows a microservices-inspired architecture:

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Frontend  │─────▶│   Backend    │─────▶│  LocalStack │
│  (React)    │      │ (Spring Boot)│      │    (SQS)    │
│   :3000     │      │    :8080     │      │    :4566    │
└─────────────┘      └──────────────┘      └─────────────┘
                            │
                            ▼
                     ┌──────────────┐
                     │   H2 DB      │
                     │  (in-memory) │
                     └──────────────┘
```

**Message Flow**:

1. User sends a message via frontend or API
2. Backend sends message to SQS queue
3. MessageConsumer processes messages from SQS
4. NotificationService sends notifications to subscribed users
5. Notifications are logged in the database

## Prerequisites

Before running the application, ensure you have:

- **Docker** (version 20.10 or higher)
- **Docker Compose** (version 2.0 or higher)
- **Internet connection** (to pull Docker images on first run)

**Optional** (for local development without Docker):

- Java 21 (for backend)
- Node.js 14+ (for frontend)

## Installation

### Installing Docker

If you don't have Docker installed, follow the instructions for your operating system:

#### Windows

1. Download **Docker Desktop** from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
2. Run the installer and follow the setup wizard
3. Restart your computer if prompted
4. Launch Docker Desktop from the Start menu
5. Verify installation:
   ```bash
   docker --version
   docker-compose --version
   ```

#### macOS

1. Download **Docker Desktop** from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
2. Open the downloaded `.dmg` file and drag Docker to Applications
3. Launch Docker from Applications
4. Follow the setup wizard
5. Verify installation:
   ```bash
   docker --version
   docker-compose --version
   ```

#### Linux (Ubuntu/Debian)

1. Update package index:

   ```bash
   sudo apt-get update
   ```

2. Install prerequisites:

   ```bash
   sudo apt-get install ca-certificates curl gnupg lsb-release
   ```

3. Add Docker's official GPG key:

   ```bash
   sudo mkdir -p /etc/apt/keyrings
   curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
   ```

4. Set up the repository:

   ```bash
   echo \
     "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
     $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
   ```

5. Install Docker Engine and Docker Compose:

   ```bash
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
   ```

6. Verify installation:
   ```bash
   docker --version
   docker compose version
   ```

**Note**: On Linux, you may need to use `docker compose` (without hyphen) instead of `docker-compose`.

For other Linux distributions, see: [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/)

## Quick Start

1. **Clone the repository** (if you haven't already):

   ```bash
   git clone <repository-url>
   cd notification-system
   ```

2. **Start all services**:

   ```bash
   docker-compose up
   ```

   Or use `docker compose` on Linux:

   ```bash
   docker compose up
   ```

3. **Wait for services to start** (this may take a few minutes on first run):

   - LocalStack: Starting AWS SQS emulator
   - create-resources: Creating SQS queues
   - Backend: Starting Spring Boot application
   - Frontend: Starting React application

4. **Access the application**:
   - Frontend: [http://localhost:3000](http://localhost:3000)
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Running the Application

### Using Docker Compose (Recommended)

Start all services in the foreground:

```bash
docker-compose up
```

Start all services in the background:

```bash
docker-compose up -d
```

View logs:

```bash
docker-compose logs -f
```

Stop all services:

```bash
docker-compose down
```

Stop and remove volumes (cleans up database):

```bash
docker-compose down -v
```

### Running Services Individually

#### Backend Only

```bash
cd backend
docker build -t notification-system-backend .
docker run -p 8080:8080 \
  -e SPRING_CLOUD_AWS_SQS_ENDPOINT=http://localhost:4566 \
  notification-system-backend
```

#### Frontend Only

```bash
cd frontend
docker build -t notification-system-frontend .
docker run -p 3000:3000 notification-system-frontend
```

**Note**: If running services individually, make sure LocalStack is running first.

## Testing the Application

### 1. Test via Web Interface

1. Open [http://localhost:3000](http://localhost:3000) in your browser
2. You should see the "Notification System" interface with:

   - A form to send messages
   - A log history section

3. **Send a test message**:

   - Select a category from the dropdown (e.g., "Sports", "Finance")
   - Enter a message (e.g., "Your team won the match!")
   - Click "Send Message"
   - You should see a success message

4. **View notification logs**:
   - The log history section will show all sent notifications
   - Logs are paginated (5 per page by default)
   - Each log entry shows: user, category, channel, message, and timestamp

### 2. Test via API (cURL)

#### Get Available Categories

```bash
curl http://localhost:8080/v1/categories
```

Expected response:

```json
["Sports", "Finance", "Movies"]
```

#### Send a Message

```bash
curl -X POST http://localhost:8080/v1/messages \
  -H "Content-Type: application/json" \
  -d '{
    "category": "Sports",
    "body": "Your team won the championship!"
  }'
```

Expected response: `202 Accepted`

#### Get Notification Logs

```bash
curl "http://localhost:8080/v1/logs?page=0&size=10"
```

Expected response:

```json
{
  "content": [
    {
      "id": 1,
      "userName": "John Doe",
      "category": "Sports",
      "channel": "EMAIL",
      "message": "Your team won the championship!",
      "sentAt": "2024-01-01T12:00:00"
    }
  ],
  "pageable": {...},
  "totalElements": 1,
  "totalPages": 1
}
```

### 3. Test via Postman or Similar Tools

1. Import the following endpoints:

   **POST** `http://localhost:8080/v1/messages`

   ```json
   {
     "category": "Finance",
     "body": "Stock prices have increased!"
   }
   ```

   **GET** `http://localhost:8080/v1/categories`

   **GET** `http://localhost:8080/v1/logs?page=0&size=10`

### 4. Verify SQS Queue

Check if messages are being queued:

```bash
# Install AWS CLI if needed
# On macOS: brew install awscli
# On Linux: sudo apt-get install awscli

aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/message-queue \
  --attribute-names ApproximateNumberOfMessages \
  --region us-east-1
```

### 5. Access H2 Database Console

1. Open [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
2. Enter connection details:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click "Connect"
4. Query tables:
   ```sql
   SELECT * FROM USER_ENTITY;
   SELECT * FROM CATEGORY_ENTITY;
   SELECT * FROM NOTIFICATION_LOG_ENTITY;
   ```

### 6. End-to-End Test Flow

1. **Check initial state**:

   ```bash
   curl http://localhost:8080/v1/categories
   curl "http://localhost:8080/v1/logs?page=0&size=5"
   ```

2. **Send multiple messages**:

   ```bash
   curl -X POST http://localhost:8080/v1/messages \
     -H "Content-Type: application/json" \
     -d '{"category": "Sports", "body": "Match starts in 30 minutes"}'

   curl -X POST http://localhost:8080/v1/messages \
     -H "Content-Type: application/json" \
     -d '{"category": "Finance", "body": "Market update: Stocks are up 5%"}'
   ```

3. **Wait a few seconds** (for async processing)

4. **Verify logs were created**:
   ```bash
   curl "http://localhost:8080/v1/logs?page=0&size=10"
   ```
   You should see new log entries for each message sent.

## Project Structure

```
notification-system/
├── backend/                    # Spring Boot backend application
│   ├── src/
│   │   └── main/
│   │       ├── java/          # Java source code
│   │       └── resources/     # Configuration and SQL scripts
│   ├── Dockerfile
│   └── README.md              # Backend-specific documentation
├── frontend/                   # React frontend application
│   ├── src/
│   │   ├── components/        # React components
│   │   └── services/          # API service layer
│   ├── Dockerfile
│   └── README.md              # Frontend-specific documentation
├── docker-compose.yaml        # Docker Compose configuration
├── create-resources.sh        # Script to create SQS queues
└── README.md                  # This file
```

## Services Overview

| Service        | Port | Description                              |
| -------------- | ---- | ---------------------------------------- |
| **Frontend**   | 3000 | React web application                    |
| **Backend**    | 8080 | Spring Boot REST API                     |
| **LocalStack** | 4566 | AWS SQS emulator                         |
| **H2 Console** | 8080 | Database management UI (via /h2-console) |

## API Documentation

### Base URL

```
http://localhost:8080/v1
```

### Endpoints

#### Send Message

```http
POST /v1/messages
Content-Type: application/json

{
  "category": "Sports",
  "body": "Your message here"
}
```

#### Get Categories

```http
GET /v1/categories
```

#### Get Notification Logs

```http
GET /v1/logs?page=0&size=10&sort=sentAt,desc
```

Query Parameters:

- `page`: Page number (default: 0)
- `size`: Page size (default: 5)
- `sort`: Sort field and direction (default: sentAt)

For detailed API documentation, see [backend/README.md](backend/README.md).

## Troubleshooting

### Docker Issues

**Problem**: `docker-compose: command not found`

- **Solution**: Use `docker compose` (without hyphen) on newer Docker versions, or install docker-compose separately

**Problem**: Port already in use

- **Solution**: Stop the service using the port or change the port mapping in `docker-compose.yaml`

**Problem**: Cannot connect to backend

- **Solution**:
  1. Verify backend is running: `docker-compose ps`
  2. Check backend logs: `docker-compose logs backend`
  3. Ensure LocalStack is running first

### Application Issues

**Problem**: Messages are sent but no logs appear

- **Solution**:
  1. Check if MessageConsumer is processing: `docker-compose logs backend | grep MessageConsumer`
  2. Verify SQS queue exists: `aws --endpoint-url=http://localhost:4566 sqs list-queues --region us-east-1`
  3. Check for errors in backend logs

**Problem**: Frontend cannot connect to backend

- **Solution**:
  1. Verify backend is accessible: `curl http://localhost:8080/v1/categories`
  2. Check browser console for errors
  3. Verify `REACT_APP_API_URL` configuration

**Problem**: H2 Console not accessible

- **Solution**:
  1. Ensure backend is running
  2. Verify H2 console is enabled in `application.properties`
  3. Use correct JDBC URL: `jdbc:h2:mem:testdb`

### Reset Everything

To start fresh:

```bash
# Stop and remove all containers and volumes
docker-compose down -v

# Remove images (optional)
docker rmi romosken/notification-system:latest
docker rmi romosken/notification-system-frontend:latest

# Start again
docker-compose up
```

### View Logs

View all logs:

```bash
docker-compose logs -f
```

View specific service logs:

```bash
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f localstack
```

## Additional Resources

- [Backend Documentation](backend/README.md) - Detailed backend architecture and API docs
- [Frontend Documentation](frontend/README.md) - Frontend architecture and configuration

