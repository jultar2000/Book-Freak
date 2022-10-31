import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";
import { getCurrentUser } from "./authService";

const basicAddressUrl = "/user-module/api/v1/addresses/"

export async function getAddress(addressId: string) {
    return await axiosInstance
        .get(basicAddressUrl + addressId)
}

export async function updateAddress(addressId: string) {
    return await axiosInstance
        .put(basicAddressUrl + addressId)
}

export async function addAddress() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .post(basicAddressUrl + username)
}
