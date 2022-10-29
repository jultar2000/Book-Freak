import { UserData } from "../interfaces/User/UserData";
import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage } from "../utils/helpers";

const basicAuthUrl = "api/v1/auth/"

export async function register(userData: UserData) {
    try {
        await axiosInstance
            .post(basicAuthUrl + "signup", userData);
    } catch (err) {
        console.error(err);
    }
}

export async function login(userData: UserData) {
    await axiosInstance
        .post(basicAuthUrl + "login", userData)
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
