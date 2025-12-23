# Notification System - Frontend

A React-based frontend application for the Notification System that provides a user interface for sending messages and viewing notification logs.

## Architecture

The frontend is built using **React 18** with a component-based architecture. It follows a simple, clean structure that separates concerns:

- **Components**: Reusable UI components
- **Services**: API communication layer
- **App**: Main application component that orchestrates the UI

### Project Structure

```
frontend/
├── public/
│   └── index.html                    # HTML template with runtime config
├── src/
│   ├── components/                   # React components
│   │   ├── MessageForm.js           # Form for sending messages
│   │   ├── MessageForm.css          # Styles for message form
│   │   ├── LogHistory.js            # Component for displaying logs
│   │   └── LogHistory.css           # Styles for log history
│   ├── services/
│   │   └── api.js                   # Axios-based API client
│   ├── App.js                       # Main application component
│   ├── App.css                      # Application styles
│   ├── index.js                     # Application entry point
│   └── index.css                    # Global styles
├── Dockerfile                        # Docker build configuration
└── package.json                     # Dependencies and scripts
```

### Key Components

#### 1. **MessageForm**

- Allows users to select a category and enter a message
- Validates input before submission
- Fetches available categories from the backend
- Displays success/error messages
- Automatically refreshes log history after successful submission

#### 2. **LogHistory**

- Displays paginated notification logs
- Sorted by most recent first
- Supports pagination controls
- Auto-refreshes when new messages are sent

#### 3. **API Service** (`services/api.js`)

- Centralized API client using Axios
- Handles API URL configuration (build-time and runtime)
- Provides functions for:
  - `sendMessage()`: Send messages to the backend
  - `getLogHistory()`: Fetch paginated notification logs
  - `getCategories()`: Fetch available notification categories

## Features

- **Message Submission Form**: Send messages with category selection and message body
- **Paginated Log History**: View all notification logs sorted from newest to oldest
- **Real-time Updates**: Log history automatically refreshes after sending messages
- **Error Handling**: User-friendly error messages and validation feedback
- **Responsive Design**: Clean, modern UI that works on different screen sizes

## Prerequisites

- **Node.js** (v14 or higher)
- **npm** or **yarn**
- **Docker** and **Docker Compose** (optional, for containerized deployment)

## Running the Application

### Option 1: Using Docker Compose (Recommended)

The easiest way to run the entire system including the backend:

```bash
docker-compose up
```

**Note**: Make sure to run this command from the root directory (where `docker-compose.yaml` is located).

This will start:

- Backend application on port `8080`
- Frontend application on port `3000`
- LocalStack (AWS SQS emulator) on port `4566`

The frontend will be available at `http://localhost:3000`.

### Option 2: Running Locally

1. **Make sure the backend is running** on `http://localhost:8080`

2. **Install dependencies**:

   ```bash
   npm install
   ```

3. **Start the development server**:

   ```bash
   npm start
   ```

   The app will automatically open at [http://localhost:3000](http://localhost:3000).

   The development server supports hot-reloading, so changes will be reflected automatically.

### Option 3: Building Docker Image

To build the frontend Docker image:

```bash
docker build -t notification-system-frontend .
```

To run the container:

```bash
docker run -p 3000:3000 notification-system-frontend
```

**Note**: When building the Docker image, you can override the API URL:

```bash
docker build --build-arg REACT_APP_API_URL=http://your-backend-url:8080 -t notification-system-frontend .
```

## Configuration

### API URL Configuration

The frontend can be configured to connect to different backend URLs using multiple methods (in priority order):

1. **Runtime Configuration** (Highest Priority):

   - Set `window.REACT_APP_API_URL` in `public/index.html` or via browser console
   - This is useful for Docker environments where the API URL might differ

2. **Build-time Environment Variable**:

   - Create a `.env` file in the frontend directory:
     ```
     REACT_APP_API_URL=http://your-api-url:8080
     ```
   - Or pass it as a build argument when building Docker image:
     ```bash
     docker build --build-arg REACT_APP_API_URL=http://your-backend-url:8080 .
     ```

3. **Default Fallback**:
   - Defaults to `http://localhost:8080` if no configuration is provided

**Important Notes**:

- In Docker environments, the browser cannot resolve container hostnames (e.g., `http://backend:8080`)
- The runtime configuration in `index.html` is set to `http://localhost:8080` to handle this
- The API service (`services/api.js`) intelligently handles localhost vs container scenarios

### Development Proxy

The `package.json` includes a proxy configuration:

```json
"proxy": "http://localhost:8080"
```

This allows the React development server to proxy API requests, avoiding CORS issues during local development.

## API Integration

The frontend communicates with the backend REST API:

### Endpoints Used

- **POST `/v1/messages`**: Send a notification message

  ```json
  {
    "category": "Sports",
    "body": "Your team won!"
  }
  ```

- **GET `/v1/logs`**: Get paginated notification logs

  - Query parameters: `page`, `size`, `sort`
  - Example: `/v1/logs?page=0&size=10&sort=sentAt,desc`

- **GET `/v1/categories`**: Get available notification categories

## Available Scripts

- `npm start` - Runs the app in development mode (opens at http://localhost:3000)
- `npm build` - Builds the app for production (outputs to `build/` directory)
- `npm test` - Launches the test runner in interactive watch mode
- `npm eject` - Ejects from Create React App (⚠️ irreversible)

## Technologies

- **React 18.2.0**: UI library
- **Axios 1.6.0**: HTTP client for API requests
- **React Scripts 5.0.1**: Build tools and development server
- **Node.js 18**: Runtime environment (for Docker)

## Development

### Component Communication

The application uses React refs to enable communication between components:

- `App.js` maintains a ref to `LogHistory`
- When `MessageForm` successfully sends a message, it triggers a refresh in `LogHistory`
- This ensures the log history is updated without manual refresh

### Error Handling

- Form validation provides immediate feedback
- API errors are caught and displayed to the user
- Network errors are handled gracefully with user-friendly messages

## Building for Production

```bash
npm run build
```

This creates an optimized production build in the `build/` folder. The build is minified and ready for deployment to any static hosting service.

You can serve the production build using:

```bash
npx serve -s build
```

Or use the Dockerfile which includes `serve` and serves the production build on port 3000.
