import axios from "axios";
import { getItemFromLocalStorage, setItemToLocalStorage } from "./helpers";

const axiosInstance = axios.create({
    baseURL: "https://localhost:8443/api/v1",
    headers: {Authorization: `Bearer ${getItemFromLocalStorage("authenticationToken")}`}
  });
  
export default axiosInstance;