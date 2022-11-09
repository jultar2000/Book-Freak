import React, { useEffect, useState } from "react";
import { getAllAuthors, getAuthor } from "../../services/authorService";
import { getAllBooks, getBookById, getBookImage, getBooksImages } from "../../services/bookService";
import { getAllOrderItemsByActiveOrder } from "../../services/orderItemService";
import { AuthorData } from "../../shared/interfaces/Author/AuthorData";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { OrderItemData } from "../../shared/interfaces/Order/OrderItemData";
import './CartPage.css'

const CartPage = () => {

    const [orderItemsData, setOrderItemsData] = useState<OrderItemData[]>([])
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [booksImages, setBookImages] = useState<BookImagesData[]>([])
    const [authorsData, setAuthorsData] = useState<AuthorData[]>([])

    useEffect(() => {
        let promises = [getAllOrderItemsByActiveOrder(), getAllAuthors(), getBooksImages(), getAllBooks()]
        Promise.all(promises)
            .then((res) => {
                setOrderItemsData(res[0].data)
                setAuthorsData(res[1].data)
                setBookImages(res[2].data)
                setBooksData(res[3].data)
            })
    }, [])

    const appendOrderItems = () => {
        const orderItemObjects: any[] = [];
        orderItemsData.forEach(orderItem => {
            if (orderItem) {
                let book: BookData = booksData.find(b => b.oid === orderItem.book!.oid)!
                let author: AuthorData = authorsData.find(a => a.oid === book!.author.oid)!
                let bookImage = booksImages.find(i => i.oid === orderItem.book!.oid)!
                    const orderItemObject = (
                        <div className="order-item-container">
                            <div id="order-item-image-container" className="order-item-sub-container">
                                <div id="order-item-image-sub-container">
                                    <img width={100} height={140} src={"data:image/png;base64," + bookImage.stringImage}></img>
                                </div>
                            </div>
                            <div id="order-item-author-name-container" className="order-item-sub-container">
                                <span>{book.title}</span>
                                <span>{author.name + " " + author.surname}</span>
                            </div>
                            <div id="order-item-quantity-container" className="order-item-sub-container">
                                <div className="order-item-sub-header-container">
                                    <span>Quantity</span>
                                </div>
                                <span>{orderItem.quantity}</span>
                            </div>
                            <div id="order-item-price-container" className="order-item-sub-container">
                                <div className="order-item-sub-header-container">
                                    <span>Price</span>
                                </div>
                                <span>{book.price + "$"}</span>
                            </div>
                            <div id="order-item-total-price-container" className="order-item-sub-container">
                                <div className="order-item-sub-header-container">
                                    <span>Total Price</span>
                                </div>
                                <span>{book.price * orderItem.quantity + "$"}</span>
                            </div>
                        </div>
                    )
                    orderItemObjects.push(orderItemObject)  
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