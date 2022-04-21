import axios from "axios";
import { getItemFromLocalStorage } from "./helpers";
import { refreshToken } from '../events/jwtActions'

const axiosInstance = axios.create({
  baseURL: "https://localhost:8443/api/v1"
});

axiosInstance.interceptors.request.use(req => {
  if (!req.headers.Authorization) {
    req.headers.Authorization = `Bearer ${getItemFromLocalStorage("authenticationToken")}`
  }
  return req;
},
  error => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      console.log('elo')
      refreshToken();
      return axiosInstance(originalRequest);
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;