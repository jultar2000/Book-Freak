import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "https://localhost:8443/api/v1",
  });
  
  export default axiosInstance;