import React, { useEffect, useState } from "react";
import { getAllBooks } from "../../services/bookService";
import { BookData } from "../../shared/interfaces/Book/BookData";

const MainPage = () => {

    const [booksData, setBooksData] = useState<BookData[]>([])
    const [booksImages, setBooksImages] = useState()

    useEffect(() => {
        getAllBooks()
            .then((res) => {
                setBooksData(res.data)
                console.log(booksData)
            }).catch((err) => {
                console.log(err)
            })
    })

    const appendBooks = () => {
        const bookObjects: any[] = [];
        booksData.forEach(book => {
            const bookObject = (
                <div className="book-container">
                    <div className="book-image-container">
                        <figure>
                            <img></img>
                            <figcaption>{book.title}</figcaption>
                        </figure>
                    </div>
                    <div className="description-container">
                        <span>{book.author.oid}</span>
                        <span>{book.description}</span>
                        <span>{book.readerRating.rating + "/10"}</span>
                    </div>
                </div>
            )
            console.log(book.author.oid)
            bookObjects.push(bookObject)
        })
        return bookObjects
    }

    return (
        <div className="main-books-container">
            {
                appendBooks()
            }
        </div>
    );
}

export default MainPage;