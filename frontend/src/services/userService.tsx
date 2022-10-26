import axiosInstance from "../utils/axiosInstance";
import { getCurrentUser } from "./authService";

export async function getUserImage() {
    let username = await getCurrentUser()
    axiosInstance
        .get("/user-module/api/v1/users/" + username + "/image")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getUserData() {
    let username = await getCurrentUser()
    axiosInstance
        .get("/user-module/api/v1/users/" + username)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}
