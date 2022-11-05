import { OrderItemData } from "../shared/interfaces/Order/OrderItemData";
import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";
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

export async function addOrUpdateItem(bookId: string, orderItemData: OrderItemData) {
    let username = getItemFromLocalStorage("username")
    axiosInstance
        .post(basicOrderItemUrl + "users/" + username + "/books/" + bookId, orderItemData)
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