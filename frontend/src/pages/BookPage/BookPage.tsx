import React, { useEffect, useState } from "react";
import { getBookById, getBookComments, addComment, getBookImage } from "../../services/bookService";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { CommentData } from "../../shared/interfaces/Book/CommentData"
import { useParams } from "react-router-dom"
import './BookPage.css'
import Button from "../../Components/Button/Button";
import { getAuthor } from "../../services/authorService";
import { AuthorData } from "../../shared/interfaces/Author/AuthorData"
import { getItemFromLocalStorage } from "../../utils/helpers";
import { addOrUpdateItem } from "../../services/orderItemService";
import { OrderItemData } from "../../shared/interfaces/Order/OrderItemData";
import { useNavigate } from 'react-router-dom';

const BookPage = () => {

    const [book, setBook] = useState<BookData>()
    const [bookImage, setBookImage] = useState('')
    const [author, setAuthor] = useState<AuthorData>()
    const [commentsData, setCommentsData] = useState<CommentData[]>([])
    const nav = useNavigate();
    const { id } = useParams();

    useEffect(() => {
        if (id != null) {
            getBookById(id)
                .then((res) => {
                    setBook(res.data)
                    getAuthor(res.data.author.oid)
                        .then((res) => {
                            setAuthor(res.data)
                        }).catch((err) => {
                            console.log(err)
                        })
                }).catch((err) => {
                    console.log(err)
                })
            getBookComments(id)
                .then((res) => {
                    setCommentsData(res.data)
                }).catch((err) => {
                    console.log(err)
                })
            getBookImage(id)
                .then((res) => {
                    setBookImage(res.data)
                }).catch((err) => {
                    console.log(err)
                })
        }
    }, [])

    const addCommentHandler = () => {
        let commentData: CommentData
        const commentInput = document.getElementById('comment-input') as HTMLInputElement
        const username = getItemFromLocalStorage("username")

        if (id && username && commentInput) {
            commentData = {
                "text": commentInput.value,
                "book_oid": id,
                "username": username
            }
            addComment(id, commentData).then(() => {
                window.location.reload()
            }).catch((err) => {
                console.log(err)
            })
        }
    }

    const addItemToCartHandler = () => {
        let orderItemData: OrderItemData
        let bookCover = "SOFT"
        let bookLanguage = "POLISH"
        let quantity = 1

        const hardCoverCheckbox = document.getElementById("book-cover-hard") as HTMLInputElement
        const engLanguageCheckbox = document.getElementById("book-language-eng") as HTMLInputElement
        const quatityInput = document.getElementById("quantity") as HTMLInputElement

        if (id != null) {
            if (hardCoverCheckbox && hardCoverCheckbox.checked === true) {
                bookCover = "HARD"
            }
            if (engLanguageCheckbox && engLanguageCheckbox.checked === true) {
                bookLanguage = "HARD"
            }
            if (quatityInput && quatityInput.value) {
                quantity = +quatityInput.value
            }
            orderItemData = {
                bookCover: bookCover,
                bookLanguage: bookLanguage,
                quantity: quantity
            }
            addOrUpdateItem(id, orderItemData)
                .then(() => {
                    nav("/cart")
                }).catch((err) => {
                    console.log(err)
                })
        }
    }

    const setCheckedValue = (elementId: string) => {
        const checkbox = document.getElementById(elementId) as HTMLInputElement
        if (checkbox) {
            checkbox.checked = false
        }
    }

    const appendComments = () => {
        const commentObjects: any[] = [];
        commentsData.forEach(comment => {
            const commentObject = (
                <div className="comment-container">
                    <div className="comment-details-container">
                        <span id="comment-username">{comment.username}</span>
                        <span >{comment.date}</span>
                    </div>
                    <div className="comment-text-container">
                        <span id="comment-text">{comment.text}</span>
                    </div>
                </div>
            )
            commentObjects.push(commentObject)
        })
        return commentObjects
    }

    return (
        <div className="main-book-detail-container">
            <div className="book-detail-container">
                <div className="book-detail-description-container">
                    <div className="book-detail-image-container">
                        <figure>
                            <img width={370} height={540} src={"data:image/png;base64," + bookImage}></img>
                        </figure>
                        <span>Genre</span>
                        <span>{book != null ? book.genre : null}</span>
                    </div>
                </div>
                <div className="info-cart-container">
                    {book != null ?
                        <div className="detail-info-container">
                            <div className="info-container">
                                <span id="book-title">{book.title}</span>
                                <span id="book-author">{author != null ? "Author: " + author.name + " " + author.surname : null}</span>
                                <span className="addit-info">{"Number of pages: " + book.numberOfPages}</span>
                                <span className="addit-info">{"Publishment year: " + book.year}</span>
                            </div>
                            <br />
                            <div className="description-div">
                                <span id="description-label">Description</span>
                                <span id="description-span">{book.description}</span>
                            </div>
                        </div> : null
                    }
                </div>
                <div className="pre-cart-container">
                    <h2>Choose your options and order today!</h2>
                    <form>
                        <div className="pre-checkboxes">
                            <label className="main-label" htmlFor="book-cover">Book Cover</label>
                            <div className="book-cover-checkboxes">
                                <input className="pre-checkbox" type="checkbox" id="book-cover-hard" onChange={() => setCheckedValue("book-cover-soft")} ></input>
                                <label className="pre-checkbox-label" htmlFor="book-cover-hard">HARD</label>
                                <input className="pre-checkbox" type="checkbox" id="book-cover-soft" onChange={() => setCheckedValue("book-cover-hard")}></input>
                                <label className="pre-checkbox-label" htmlFor="book-cover-soft">SOFT</label>
                            </div>
                        </div>
                        <div className="pre-checkboxes">
                            <label className="main-label" htmlFor="book-language">Book language</label>
                            <div className="book-language-checkboxes">
                                <input className="pre-checkbox" type="checkbox" id="book-language-en" onChange={() => setCheckedValue("book-language-pl")}></input>
                                <label className="pre-checkbox-label" htmlFor="book-language-en">ENGLISH</label>
                                <input className="pre-checkbox" type="checkbox" id="book-language-pl" onChange={() => setCheckedValue("book-language-en")}></input>
                                <label className="pre-checkbox-label" htmlFor="book-language-pl">POLISH</label>
                            </div>
                        </div>
                        <div>
                            <label className="main-label" htmlFor="book-language">Quantity</label>
                            <div id="quantity-input">
                                <input type="number" id="quantity" name="quantity" min="1" max="5"></input>
                            </div>
                        </div>
                        <input id="add-item-btn" type="submit" value="Add to cart" onClick={addItemToCartHandler}></input>
                    </form>
                </div>
            </div>
            <div className="main-comments-container">
                <span id="comments-label">Comments</span>
                <div id="user-comment-container" className="comment-container">
                    <span id="comment-header">Add a comment</span>
                    <textarea id="comment-input"></textarea>
                    <div id="comment-button-container">
                        <Button style={{ background: "green" }} type="medium-btn" text="Comment" onClick={addCommentHandler} />
                    </div>
                </div>
                {
                    appendComments()
                }
            </div>
        </div>
    )
}

export default BookPage