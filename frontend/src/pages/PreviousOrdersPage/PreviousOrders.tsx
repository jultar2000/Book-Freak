import React, { useEffect, useState } from "react";
import { OrderData } from "../../shared/interfaces/Order/OrderData";
import { getAllOrdersByUser } from "../../services/orderService"
import { getAllOrderItems } from "../../services/orderItemService";
import { OrderItemData } from "../../shared/interfaces/Order/OrderItemData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { getAllBooks, getBooksImages } from "../../services/bookService";
import './PreviousOrders.css'
import { useNavigate } from "react-router-dom";

const PreviousOrdersPage = () => {

    const nav = useNavigate()
    const [orders, setOrders] = useState<OrderData[]>([])
    const [orderItems, setOrderItems] = useState<OrderItemData[]>([])
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [booksImages, setBookImages] = useState<BookImagesData[]>([])

    useEffect(() => {
        const promises = [getAllOrdersByUser(), getAllOrderItems(), getBooksImages(), getAllBooks()]
        Promise.all(promises)
            .then((res) => {
                setOrders(res[0].data)
                setOrderItems(res[1].data)
                setBookImages(res[2].data)
                setBooksData(res[3].data)
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    const appendOrders = () => {
        const orderObjects: any[] = [];
        orders.forEach(order => {
            let totalPrice = 0
            let orderItemsObjects: any[] = []
            const filteredOrderItems = orderItems.filter((oi) => oi.orderId === order.oid)

            filteredOrderItems.forEach(orderItem => {
                let book: BookData = booksData.find(b => b.oid === orderItem.book!.oid)!
                let bookImage = booksImages.find(i => i.oid === orderItem.book!.oid)!
                const orderItemObject = (
                    <div className="main-inside-order-item-container">
                        <div className="main-inside-image-container">
                            <div className="sub-inside-image-container">
                                <img width={75} height={120} src={"data:image/png;base64," + bookImage.stringImage} onClick={() => nav("/book/" + book.oid)}></img>
                            </div>
                        </div>
                        <div className="main-inside-title-container">
                            <span>{book.title}</span>
                        </div>
                        <div className="main-inside-price-container">
                            <span>{orderItem.quantity + " x " + book.price + "$"}</span>
                        </div>
                        <div className="main-inside-total-container">
                            <span>{book.price * orderItem.quantity + "$"}</span>
                        </div>
                    </div>
                );
                totalPrice += book.price * orderItem.quantity
                orderItemsObjects.push(orderItemObject)
            })

            const orderObject = (
                <div className="order-main-container">
                    <div className="status-container">
                        <span>{order.ordered ? "Ordered" : "Not ordered"}</span>
                        {order.ordered ?
                            <div className="sub-status-container">
                                <span>{order.shippingStatus}</span>
                                <span>{order.orderDate}</span>
                            </div> : null
                        }
                    </div>
                    <div className="order-items-inside-container">
                        {orderItemsObjects}
                    </div>
                    <div className="main-summary-container">
                        <span>{"Total: " + totalPrice + "$"}</span>
                    </div>
                </div>
            )
            orderObjects.push(orderObject)
        })
        return orderObjects
    }

    return (
        <div className="main-previous-orders-container">
            {appendOrders().reverse()}
        </div>
    );
}

export default PreviousOrdersPage