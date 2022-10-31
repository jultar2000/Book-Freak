import { UserData } from '../shared/interfaces/User/UserData'
import axiosInstance from "../utils/axiosInstance";
import { setItemToLocalStorage } from "../utils/helpers";

const basicAuthUrl = "/api/v1/auth/"

export async function register(userData: UserData) {
    return await axiosInstance
        .post(basicAuthUrl + "signup", userData);

}

export async function login(userData: UserData) {
    return await axiosInstance
        .post(basicAuthUrl + "login", userData)
}

export async function getCurrentUser() {
    await axiosInstance
        .get("api/v1/communication")
        .then(res => res.data)
        .catch((err) => console.log(err))
}
