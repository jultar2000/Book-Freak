import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage , getItemFromLocalStorage } from "../utils/helpers";

export function refreshToken() {
    const data = {
        "refreshToken": getItemFromLocalStorage("refreshToken"),
        "username": getItemFromLocalStorage("username")
    };
    axiosInstance.post("api/v1/auth/refresh/token", data, {
        headers: {
            "Content-Type": "application/json",
        }
    }).then((res) => {
        setItemToLocalStorage("authenticationToken", res.data.authenticationToken)
        setItemToLocalStorage("refreshToken", res.data.refreshToken);
        setItemToLocalStorage("expiresAt", res.data.expiresAt);
        setItemToLocalStorage("username", res.data.username);
    }).catch((err) => console.log(err))
}