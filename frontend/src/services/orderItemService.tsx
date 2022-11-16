import { OrderItemData } from "../shared/interfaces/Order/OrderItemData";
import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";

const basicOrderItemUrl = "/order-module/api/v1/order-items/"

export async function getAllOrderItems() {
    return await axiosInstance
        .get(basicOrderItemUrl + "all")
}

export async function getAllOrderItemsByOrder(orderId: string) {
    return await axiosInstance
        .get(basicOrderItemUrl + "all/orders/" + orderId)
}

export function getAllOrderItemsByActiveOrder() {
    let username = getItemFromLocalStorage("username")
    return axiosInstance
        .get(basicOrderItemUrl + "active/users/" + username)
}

export async function getOrderItemById(orderItemId: string) {
    return await axiosInstance
        .get(basicOrderItemUrl + orderItemId)
}

export async function addOrUpdateItem(bookId: string, orderItemData: OrderItemData) {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .post(basicOrderItemUrl + "users/" + username + "/books/" + bookId, orderItemData)
}

export async function deleteOrderItem(orderItemId: string) {
    return await axiosInstance
        .delete(basicOrderItemUrl + orderItemId)
}