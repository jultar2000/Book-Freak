import { OrderItemBookData } from "../Book/OrderItemBookData";

export interface OrderItemData {
    oid?: string,
    book?: OrderItemBookData,
    orderId?: string,
    quantity: number,
    bookCover: string,
    bookLanguage: string
}