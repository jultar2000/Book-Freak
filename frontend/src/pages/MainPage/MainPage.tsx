import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllAuthors } from "../../services/authorService";
import { getAllBooks, getBooksRatings, getBooksImages } from "../../services/bookService";
import { AuthorData } from "../../shared/interfaces/Author/AuthorData";
import { BookData } from "../../shared/interfaces/Book/BookData";
import { BookImagesData } from "../../shared/interfaces/Book/BookImagesData";
import { BookRating } from "../../shared/interfaces/Book/BookRating";
import './MainPage.css'

const MainPage = () => {

    const nav = useNavigate()
    const [constBooksData, setConstBooksData] = useState<BookData[]>([])
    const [booksData, setBooksData] = useState<BookData[]>([])
    const [authorsData, setAuthorsData] = useState<AuthorData[]>([])
    const [bookRatings, setBookRatings] = useState<BookRating[]>([])
    const [booksImages, setBooksImages] = useState<BookImagesData[]>([])

    useEffect(() => {
        let promises = [getAllBooks(), getAllAuthors(), getBooksRatings(), getBooksImages()]
        Promise.all(promises)
            .then((res) => {
                setConstBooksData(res[0].data)
                setBooksData(res[0].data)
                setAuthorsData(res[1].data)
                setBookRatings(res[2].data)
                setBooksImages(res[3].data)
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    const setCheckedValue = (checkElemName: string) => {
        const arr = ["genre-filter-checkbox", "author-filter-checkbox", "title-filter-checkbox"]
        arr.forEach(name => {
            const checkbox = document.getElementById(name) as HTMLInputElement
            if (name === checkElemName) {
                checkbox.checked = true
            } else {
                checkbox.checked = false
            }

        })
    }

    const filterBooks = () => {
        const genreCheckbox = document.getElementById("genre-filter-checkbox") as HTMLInputElement
        const authorCheckbox = document.getElementById("author-filter-checkbox") as HTMLInputElement
        const titleCheckbox = document.getElementById("title-filter-checkbox") as HTMLInputElement
        const valueInput = document.getElementById("filter-value-input") as HTMLInputElement
        if (genreCheckbox.checked === true) {
            setBooksData(booksData.filter(book => book.genre.toLowerCase().includes(valueInput.value.toLowerCase())))
        } else if (authorCheckbox.checked === true) {
            const arr: BookData[] = []
            booksData.forEach(book => {
                const author = authorsData.find(a => a.oid === book.author.oid)!
                if (author.name.toLowerCase().includes(valueInput.value.toLowerCase()) ||
                    author.surname.toLowerCase().includes(valueInput.value.toLowerCase())) {
                    arr.push(book)
                }
            })
            setBooksData(arr)
        } else if (titleCheckbox.checked === true) {
            setBooksData(booksData.filter(book => book.title.toLowerCase().includes(valueInput.value.toLowerCase())))
        } else {
            setBooksData(constBooksData)
        }
    }

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
                        <span id="price-main">{book.price + "$"}</span>
                        <div id="rating-main-container">
                            <span id="rating-main">{rating != null ? rating.realRating.toFixed(1) + "/10" : null}</span>
                        </div>
                    </div>
                </div>
            )
            bookObjects.push(bookObject)
        })
        return bookObjects
    }

    return (
        <div className="main-container">
            <div className="filter-stripe">
                <div className="filter-sub-container">
                    <input id="filter-value-input"></input>
                    <div className="input-filter-container">
                        <label className="filter-checkbox-label" htmlFor="genre-filter-checkbox">Genre</label>
                        <input className="filter-checkbox" type="checkbox" id="genre-filter-checkbox" onChange={() => setCheckedValue("genre-filter-checkbox")}></input>
                    </div>
                    <div className="input-filter-container">
                        <label className="filter-checkbox-label" htmlFor="author-filter-checkbox">Author</label>
                        <input className="filter-checkbox" type="checkbox" id="author-filter-checkbox" onChange={() => setCheckedValue("author-filter-checkbox")}></input>
                    </div>
                    <div className="input-filter-container">
                        <label className="filter-checkbox-label" htmlFor="title-filter-checkbox">Title</label>
                        <input className="filter-checkbox" type="checkbox" id="title-filter-checkbox" onChange={() => setCheckedValue("title-filter-checkbox")}></input>
                    </div>
                    <button onClick={filterBooks}>Search</button>
                </div>
            </div>
            <div className="main-books-container">
                {
                    appendBooks()
                }
            </div>
        </div>
    );
}

export default MainPage;