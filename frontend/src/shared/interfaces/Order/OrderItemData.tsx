import { BookData } from "../Book/BookData";

export interface OrderItemData {
    oid?: string,
    book?: BookData,
    orderId?: string,
    quantity: number,
    bookCover: string,
    bookLanguage: string
}