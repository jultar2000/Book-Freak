import axiosInstance from "../utils/axiosInstance";
import { getCurrentUser } from "./authService";

const booksBasicUrl = "/book-module/api/v1/books/"
const extentedBooksUrl = booksBasicUrl + "comments/"

export async function getAllBooks() {
    axiosInstance
        .get(booksBasicUrl + "all")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBookById(bookId: string) {
    axiosInstance
        .get(booksBasicUrl + bookId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBooksByKeyword(keyword: string) {
    axiosInstance
        .get(booksBasicUrl + "keyword/" + keyword)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBooksByGenre(genre: string) {
    axiosInstance
        .get(booksBasicUrl + "genre/" + genre)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBooksByRating() {
    axiosInstance
        .get(booksBasicUrl + "/rating")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBooksByAuthor(authorId: string) {
    axiosInstance
        .get(booksBasicUrl + "authors/" + authorId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function updateRating(bookId: string, ratingNum: number) {
    const rating = {
        rating: ratingNum
    }
    axiosInstance
        .put(booksBasicUrl + bookId + "/rating", rating)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getBookComments(bookId: string) {
    axiosInstance
        .get(booksBasicUrl + bookId + "/comments")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function addComment(bookId: string) {
    axiosInstance
        .post(booksBasicUrl + bookId + "/comments")
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function updateComment(commentId: string) {
    axiosInstance
        .put(extentedBooksUrl + commentId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function deleteComment(commentId: string) {
    let username = await getCurrentUser()
    axiosInstance
        .delete(extentedBooksUrl + commentId + "/users/" + username)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}


