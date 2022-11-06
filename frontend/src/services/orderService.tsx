import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";
import { getCurrentUser } from "./authService";

const basicOrderUrl = "/order-module/api/v1/orders/"

export async function getAllOrdersByUser() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .get(basicOrderUrl + "all/user/" + username)
}

export async function getOrderById(orderId: string) {
    return await axiosInstance
        .get(basicOrderUrl + orderId)
}

export async function getActiveOrder() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .get(basicOrderUrl + "active/users/" + username)
}

export async function updateOrder(orderId: string) {
    return await axiosInstance
        .put(basicOrderUrl + orderId)
}

export async function makeOrder() {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .put(basicOrderUrl + "users/" + username)
}

export async function deleteOrder(orderId: string) {
    return await axiosInstance
        .delete(basicOrderUrl + orderId)
}