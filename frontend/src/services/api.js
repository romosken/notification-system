import axios from "axios";

// Support both build-time and runtime configuration
// Priority: window variable (runtime) > build-time env > default
// Always use localhost:8080 for browser requests (not container hostnames)
let API_BASE_URL = "http://localhost:8080";

// Override with runtime config if available
if (window.REACT_APP_API_URL) {
  API_BASE_URL = window.REACT_APP_API_URL;
} else if (
  process.env.REACT_APP_API_URL &&
  process.env.REACT_APP_API_URL.includes("localhost")
) {
  // Only use build-time env if it's localhost (not container hostnames)
  API_BASE_URL = process.env.REACT_APP_API_URL;
}

// Log the API URL for debugging
console.log("API Base URL:", API_BASE_URL);

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const sendMessage = async (category, body) => {
  try {
    const response = await api.post("/v1/messages", {
      category,
      body,
    });
    return response;
  } catch (error) {
    throw error;
  }
};

export const getLogHistory = async (page = 0, size = 10) => {
  try {
    const response = await api.get("/v1/logs", {
      params: {
        page,
        size,
        sort: "sentAt,desc",
      },
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default api;
