import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage } from "../utils/helpers";

export function register(userData) {
    try {
        axiosInstance
            .post("/auth/signup", userData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
    } catch (err) {
        console.error(err);
    }
}

export function login(userData) {
    axiosInstance
        .post("/auth/login", userData, {
            headers: {
                "Content-Type": "application/json"
            }
        }).then((res) => {
            setItemToLocalStorage("authenticationToken", res.data.authenticationToken)
            setItemToLocalStorage("refreshToken", res.data.refreshToken);
            setItemToLocalStorage("expiresAt", res.data.expiresAt);
            setItemToLocalStorage("username", res.data.username);
        }).catch((err) => console.log(err))
}

