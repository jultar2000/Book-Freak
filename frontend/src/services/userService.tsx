import axiosInstance from "../utils/axiosInstance";
import { getCurrentUser } from "./authService";

const basicUserUrl = "/user-module/api/v1/users/"

export async function getUserImage() {
    let username = await getCurrentUser()
    axiosInstance
        .get(basicUserUrl + username + "/image")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getUserData() {
    let username = await getCurrentUser()
    axiosInstance
        .get(basicUserUrl + username)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}
