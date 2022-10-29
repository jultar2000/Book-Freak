import axiosInstance from "../utils/axiosInstance";
import { getCurrentUser } from "./authService";

const basicOrderItemUrl = "/order-module/api/v1/order-items/"

export async function getAllOrderItemsByOrder(orderId: string) {
    axiosInstance
        .get(basicOrderItemUrl + "all/orders/" + orderId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getAllOrderItemsByActiveOrder() {
    let username = await getCurrentUser()
    axiosInstance
        .get(basicOrderItemUrl + "active/users/" + username)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getOrderItemById(orderItemId: string) {
    axiosInstance
        .get(basicOrderItemUrl + orderItemId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function addOrUpdateItem(bookId: string) {
    let username = await getCurrentUser()
    axiosInstance
        .post(basicOrderItemUrl + "users/" + username + "/books/" + bookId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function deleteOrderItem(orderItemId: string) {
    axiosInstance
        .delete(basicOrderItemUrl + orderItemId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}