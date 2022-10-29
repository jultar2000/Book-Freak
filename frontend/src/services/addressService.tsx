import axiosInstance from "../utils/axiosInstance";
import { getCurrentUser } from "./authService";

const basicAddressUrl = "/user-module/api/v1/addresses/"

export async function getAddress(addressId: string) {
    axiosInstance
        .get(basicAddressUrl + addressId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function updateAddress(addressId: string) {
    axiosInstance
        .put(basicAddressUrl + addressId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function addAddress() {
    let username = await getCurrentUser()
    axiosInstance
        .post(basicAddressUrl + username)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}