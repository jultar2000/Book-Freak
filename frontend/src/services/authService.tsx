import { UserData } from "../interfaces/UserData";
import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage } from "../utils/helpers";

export async function register(userData: UserData) {
    try {
        await axiosInstance
            .post("api/v1/auth/signup", userData);
    } catch (err) {
        console.error(err);
    }
}

export async function login(userData: UserData) {
    await axiosInstance
        .post("api/v1/auth/login", userData)
        .then((res) => {
            setItemToLocalStorage("authenticationToken", res.data.authenticationToken)
            setItemToLocalStorage("refreshToken", res.data.refreshToken);
            setItemToLocalStorage("expiresAt", res.data.expiresAt);
            setItemToLocalStorage("username", res.data.username);
        }).catch((err) => console.log(err))
}

export async function getCurrentUser() {
    await axiosInstance
        .get("api/v1/communication")
        .then(res => res.data)
        .catch((err) => console.log(err))
}