import React, { useEffect, useState } from "react";
import { getBookById, getBookImage } from "../../services/bookService";
import { getAllOrderItemsByActiveOrder } from "../../services/orderItemService";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { OrderItemData } from "../../shared/interfaces/Order/OrderItemData";

const CartPage = () => {

    const [orderItemsData, setOrderItemsData] = useState<OrderItemData[]>([])
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [booksImages, setBookImages] = useState<BookImagesData[]>([])

    useEffect(() => {
        getAllOrderItemsByActiveOrder()
            .then((res) => {
                setOrderItemsData(res.data)
                res.data.forEach((orderItem: OrderItemData) => {
                    if (orderItem.book) {
                        getBookById(orderItem.book.oid)
                            .then((res) => {
                                setBooksData([...booksData, res.data])
                            }).catch((err) => {
                                console.log(err)
                            })
                        getBookImage(orderItem.book.oid)
                            .then((res) => {
                                let bookImageData: BookImagesData
                                bookImageData = {
                                    oid: orderItem!.book!.oid,
                                    stringImage: res.data
                                }
                                setBookImages([...booksImages, bookImageData])
                            }).catch((err) => {
                                console.log(err)
                            })
                    }
                })
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    const appendOrderItems = () => {
        const orderItemObjects: any[] = [];
        orderItemsData.forEach(orderItem => {
            if (orderItem) {
                let book: BookData = booksData.find(b => b.oid === orderItem.book!.oid)!
                let bookImage = booksImages.find(i => i.oid === orderItem.book!.oid)
                let stringImage = ''
                if (bookImage)
                    stringImage = bookImage.stringImage
                if (book) {
                    const orderItemObject = (
                        <div className="order-item-container">
                            <div className="order-item-image-container">
                                <img width={200} height={200} src={"data:image/png;base64," + stringImage}></img>
                            </div>
                            <div className="order-item-author-name-container">
                                <span>{book.title}</span>
                                <span>{book.author.name + book.author.surname}</span>
                            </div>
                            <div className="order-item-quantity-container">
                                <span>{orderItem.quantity}</span>
                            </div>
                            <div className="order-item-price-container">
                                <span>{book.price}</span>
                            </div>
                            <div className="order-item-total-price-container">
                                <span>{book.price * orderItem.quantity}</span>
                            </div>
                        </div>
                    )
                    orderItemObjects.push(orderItemObject)
                }
            }
        })
        return orderItemObjects
    }

    return (
        <div className="main-cart-container">
            {
                appendOrderItems()
            }
        </div>
    )
}

export default CartPage