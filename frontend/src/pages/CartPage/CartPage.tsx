import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllAuthors } from "../../services/authorService";
import { getAllBooks, getBooksImages } from "../../services/bookService";
import { getActiveOrder, makeOrder } from "../../services/orderService";
import { deleteOrderItem, getAllOrderItemsByActiveOrder } from "../../services/orderItemService";
import { getUserData } from "../../services/userService";
import { AuthorData } from "../../shared/interfaces/Author/AuthorData";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { OrderItemData } from "../../shared/interfaces/Order/OrderItemData";
import { ExtendedUserData } from "../../shared/interfaces/User/ExtendedUserData";
import Button from '../../Components/Button/Button'
import './CartPage.css'

const CartPage = () => {

    let totalPrice = 0;

    const nav = useNavigate()
    const [orderItemsData, setOrderItemsData] = useState<OrderItemData[]>([])
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [booksImages, setBookImages] = useState<BookImagesData[]>([])
    const [authorsData, setAuthorsData] = useState<AuthorData[]>([])
    const [userData, setUserData] = useState<ExtendedUserData>()
    const [isDataValid, setIsDataValid] = useState(false)

    useEffect(() => {
        getActiveOrder()
            .then((res) => {
                if (res.data.length !== 0) {
                    const promises = [getAllOrderItemsByActiveOrder(), getAllAuthors(), getBooksImages(), getAllBooks(), getUserData()]
                    Promise.all(promises)
                        .then((res) => {
                            setOrderItemsData(res[0].data)
                            setAuthorsData(res[1].data)
                            setBookImages(res[2].data)
                            setBooksData(res[3].data)
                            setUserData(res[4].data)
                        }).catch((err) => {
                            console.log(err)
                        })
                }
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    const deleteItemFromCartHandler = (orderItemId: string) => {
        deleteOrderItem(orderItemId)
            .then(() => {
                window.location.reload()
            }).catch((err) => {
                console.log(err)
            })
    }

    const placeOrderHandler = () => {
        makeOrder()
            .then(() => {
                window.location.reload()
            }).catch((err) => {
                console.log(err)
            })
    }

    const validateOrderData = () => {
        const countryInput = document.getElementById("country-input") as HTMLInputElement
        const cityInput = document.getElementById("city-input") as HTMLInputElement
        const zipInput = document.getElementById("zip-input") as HTMLInputElement
        const streetInput = document.getElementById("street-input") as HTMLInputElement
        const houseNumInput = document.getElementById("house-number-input") as HTMLInputElement
        const balanceAfterTransaction = userData != null ?  userData.funds! - totalPrice : -1

        if (balanceAfterTransaction >= 0 && countryInput.value != '' && cityInput.value != '' &&
            zipInput.value != '' && streetInput.value != '' && houseNumInput.value != '') {
            setIsDataValid(true)
        } else {
            setIsDataValid(false)
        }
    }

    const appendOrderItems = () => {
        const orderItemObjects: any[] = [];
        let price = 0
        orderItemsData.forEach(orderItem => {
            if (orderItem) {
                let book: BookData = booksData.find(b => b.oid === orderItem.book!.oid)!
                let author: AuthorData = authorsData.find(a => a.oid === book!.author.oid)!
                let bookImage = booksImages.find(i => i.oid === orderItem.book!.oid)!
                price = price + book.price * orderItem.quantity
                const orderItemObject = (
                    <div className="order-item-container">
                        <div id="order-item-image-container" className="order-item-sub-container">
                            <div id="order-item-image-sub-container">
                                <img width={75} height={120} src={"data:image/png;base64," + bookImage.stringImage} onClick={() => nav("/book/" + book.oid)}></img>
                            </div>
                        </div>
                        <div id="order-item-author-name-container" className="order-item-sub-container">
                            <div id="order-details-container" className="order-item-sub-container">
                                <span>Details</span>
                            </div>
                            <span>{book.title}</span>
                            <span>{author.name + " " + author.surname}</span>
                        </div>
                        <div id="order-item-language-container" className="order-item-sub-container">
                            <div id="details-item-header" className="order-item-sub-header-container">
                                <span>Language</span>
                            </div>
                            <span>{orderItem.bookLanguage}</span>
                        </div>
                        <div id="order-item-cover-container" className="order-item-sub-container">
                            <div id="details-item-header" className="order-item-sub-header-container">
                                <span>Book Cover</span>
                            </div>
                            <span>{orderItem.bookCover}</span>
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
                        <div id="delete-btn-con-sub" className="order-item-sub-container">
                            <div id="order-item-button-container">
                                <button id="delete-order-item-btn" onClick={() => { deleteItemFromCartHandler(orderItem.oid!) }}>X</button>
                            </div>
                        </div>
                    </div>
                )
                orderItemObjects.push(orderItemObject)
            }
        })
        totalPrice = price
        return orderItemObjects
    }

    return (
        <div className="main-cart-container">
            <div className="top-cart-container">
                <h1>{orderItemsData.length !== 0 ? "Your cart" : "Your cart is empty!"}</h1>
            </div>
            {orderItemsData.length !== 0 ?
                <div className="bottom-cart-container">
                    <div className="items-container">
                        {
                            appendOrderItems()
                        }
                    </div>
                    <div className="summary-container">
                        <div className="address-container">
                            <span id="delivery-header">Delivery address</span>
                            <form onChange={validateOrderData}>
                                <div className="address-form">
                                    <div className='address-content-container'>
                                        <span className="address-content-span">Country</span>
                                        <input className="address-content-input" id='country-input' placeholder="country" required />
                                    </div>
                                    <div className='address-content-container'>
                                        <span className="address-content-span">City</span>
                                        <input className="address-content-input" id='city-input' placeholder="city" required />
                                    </div>
                                    <div className='address-content-container'>
                                        <span className="address-content-span">ZIP</span>
                                        <input className="address-content-input" id='zip-input' placeholder="xx-yyy" required />
                                    </div>
                                    <div className='address-content-container'>
                                        <span className="address-content-span">Street</span>
                                        <input className="address-content-input" id='street-input' placeholder="street" required />
                                    </div>
                                    <div className='address-content-container'>
                                        <span className="address-content-span">House Number</span>
                                        <input className="address-content-input" id='house-number-input' placeholder="xx" required />
                                    </div>
                                </div>
                            </form>
                            <span id="delivery-header">Summary and payment</span>
                        </div>
                        <div className="buy-container">
                            <span>{userData != null ? "Your account balance: " + userData.funds + "$" : null}</span>
                            <span>{"Total price: " + totalPrice + "$"}</span>
                            <span>{"Balance after transaction: " + (userData != null ? userData.funds! - totalPrice + "$" : null)}</span>
                            <br></br>
                            <Button disabled={!isDataValid} type="medium-btn" text="Place order" onClick={placeOrderHandler}></Button>
                        </div>
                    </div>
                </div>
                :
                null
            }
        </div>
    )
}

export default CartPage