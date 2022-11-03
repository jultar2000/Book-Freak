import React, { useEffect, useState } from "react";
import { getBookById } from "../../services/bookService";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { useParams } from "react-router-dom"
import './BookPage.css'
import Button from "../../Components/Button/Button";

const BookPage = () => {

    const [book, setBook] = useState<BookData>()
    const { id } = useParams();

    useEffect(() => {
        if (id != null) {
            getBookById(id)
                .then((res) => {
                    setBook(res.data)
                }).catch((err) => {
                    console.log(err)
                })
        }
    }, [])

    return (
        <div className="main-book-detail-container">
            <div className="book-detail-container">
                <div className="book-detail-description-container">
                    <div className="book-detail-image-container">
                        <figure>
                            <img width={320} height={480} src='/images/book.jpg'></img>
                        </figure>
                        <span>Genre</span>
                        <span>{book != null ? book.genre : null}</span>
                    </div>
                </div>
                <div className="info-cart-container">
                    {book != null ?
                        <div className="detail-info-container">
                            <span>{book.title}</span>
                            <span>{book.description}</span>
                            <span>{book.numberOfPages}</span>
                            <span>{book.year}</span>
                            <span>{book.author.name + book.author.surname}</span>
                        </div> : null
                    }
                    <div className="pre-cart-container">
                        <Button type="medium-btn" text="Add to cart" onClick={() => { }}></Button>
                    </div>
                </div>
            </div>
            <div className="comments-container">
                fwerfwerfwerfwerfwerfwerfwerfwer
            </div>
        </div>
    )
}

export default BookPage