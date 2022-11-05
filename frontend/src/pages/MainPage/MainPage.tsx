import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllAuthors } from "../../services/authorService";
import { getAllBooks, getBookRatings, getBooksImages } from "../../services/bookService";
import { AuthorData } from "../../shared/interfaces/Author/AuthorData";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { BookRating } from "../../shared/interfaces/Book/BookRating";
import './MainPage.css'

const MainPage = () => {

    const nav = useNavigate()
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [authorsData, setAuthorsData] = useState<AuthorData[]>([])
    const [bookRatings, setBookRatings] = useState<BookRating[]>([])
    const [booksImages, setBooksImages] = useState<BookImagesData[]>([])

    useEffect(() => {
        getAllBooks()
            .then((res) => {
                setBooksData(res.data)
            }).catch((err) => {
                console.log(err)
            })
        getAllAuthors()
            .then((res) => {
                setAuthorsData(res.data)
            }).catch((err) => {
                console.log(err)
            })
        getBookRatings()
            .then((res) => {
                setBookRatings(res.data)
            }).catch((err) => {
                console.log(err)
            })
        getBooksImages()
            .then((res) => {
                setBooksImages(res.data)
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    const appendBooks = () => {
        const bookObjects: any[] = [];
        booksData.forEach(book => {
            const author = authorsData.find(a => a.oid === book.author.oid)
            const rating = bookRatings.find(r => r.bookOid === book.oid)
            let image = booksImages.find(i => i.oid === book.oid)
            const stringImage = image != null ? image.stringImage : ''
            const bookObject = (
                <div className="book-container">
                    <div className="book-image-container">
                        <figure>
                            <img width={320} height={480} src={"data:image/png;base64," + stringImage} onClick={() => nav("/book/" + book.oid)}></img>
                            <figcaption>{book.title}</figcaption>
                        </figure>
                    </div>
                    <div className="description-container">
                        <span className="author-span">{author != null ? author.name + ' ' + author.surname : null}</span>
                        <span>{book.price + "$"}</span>
                        <span>{rating != null ? rating.realRating + "/10" : null}</span>
                    </div>
                </div>
            )
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