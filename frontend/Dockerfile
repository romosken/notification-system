FROM node:18-alpine

WORKDIR /app

# Copy all source files first
COPY . .

# Build argument for API URL (must be set during build)
ARG REACT_APP_API_URL=http://localhost:8080

# Install dependencies
RUN npm install

# Set the environment variable for the build process
ARG REACT_APP_API_URL=http://localhost:8080
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Verify the API URL is set correctly
RUN echo "Building with API URL: $REACT_APP_API_URL"

# Build the React app (this will use REACT_APP_API_URL from env)
RUN npm run build

# Install serve globally
RUN npm install -g serve

# Expose port 3000
EXPOSE 3000

# Start the server
CMD ["serve", "-s", "build", "-l", "3000"]

