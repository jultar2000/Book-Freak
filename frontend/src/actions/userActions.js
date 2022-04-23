import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage } from "../utils/helpers";

export function register(userData) {
    try {
        axiosInstance
            .post("api/v1/auth/signup", userData, {
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
        .post("api/v1/auth/login", userData, {
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

export function getUserImage() {
    let username = getCurrentUser()
    axiosInstance
        .get("/user-module/api/v1/users/" + "username" + "/image")
        .then((res) => {
          
        }).catch((err) => console.log(err))
}

export function getUserData() {
    let username = getCurrentUser()
    axiosInstance
    .get("/user-module/api/v1/users/" + "username", {
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then((res) => {
        

    }).catch((err) => console.log(err))
}

export function getCurrentUser() {
    axiosInstance
        .get("api/v1/communication")
        .then((res) => {
            
        }).catch((err) => console.log(err))
}