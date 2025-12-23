# Notification System Frontend

React frontend for the Notification System backend API.

## Features

- **Message Submission Form**: Send messages with category selection and message body
- **Paginated Log History**: View all notification logs sorted from newest to oldest

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

The app will open at [http://localhost:3000](http://localhost:3000)

### Configuration

The frontend is configured to connect to the backend API at `http://localhost:8080` by default.

To change the API URL, create a `.env` file in the frontend directory:
```
REACT_APP_API_URL=http://your-api-url:8080
```

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm build` - Builds the app for production
- `npm test` - Launches the test runner

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── MessageForm.js
│   │   ├── MessageForm.css
│   │   ├── LogHistory.js
│   │   └── LogHistory.css
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── App.css
│   ├── index.js
│   └── index.css
└── package.json
```

