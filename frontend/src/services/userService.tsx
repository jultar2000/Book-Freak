import { ExtendedUserData } from "../shared/interfaces/User/ExtendedUserData";
import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";

const basicUserUrl = "/user-module/api/v1/users/"

export async function getUserImage() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .get(basicUserUrl + username + "/image");
}

export async function getUserData() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .get(basicUserUrl + username);
}

export async function updateUserData(extendedUserData: ExtendedUserData) {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .put(basicUserUrl + username, extendedUserData);
}

export async function updateUserImage(file: FormData) {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .put(basicUserUrl + username + "/image", file);
}