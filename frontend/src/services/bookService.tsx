import { CommentData } from "../shared/interfaces/Book/CommentData";
import axiosInstance from "../utils/axiosInstance";
import { getItemFromLocalStorage } from "../utils/helpers";

const booksBasicUrl = "/book-module/api/v1/books/"
const extentedBooksUrl = booksBasicUrl + "comments/"

export async function getAllBooks() {
    return await axiosInstance
        .get(booksBasicUrl + "all");
}

export async function getBookById(bookId: string) {
    return await axiosInstance
        .get(booksBasicUrl + bookId)
}

export async function getBooksByKeyword(keyword: string) {
    return await  axiosInstance
        .get(booksBasicUrl + "keyword/" + keyword)
}

export async function getBooksByGenre(genre: string) {
    return await  axiosInstance
        .get(booksBasicUrl + "genre/" + genre)
}

export async function getBooksByRating() {
    return await axiosInstance
        .get(booksBasicUrl + "rating")
}

export async function getBooksByAuthor(authorId: string) {
    return await axiosInstance
        .get(booksBasicUrl + "authors/" + authorId)
}

export async function getBookRatings() {
    return await axiosInstance
        .get(booksBasicUrl + "ratings")
}

export async function getBooksImages() {
    return await axiosInstance
        .get(booksBasicUrl + "images")
}

export async function updateRating(bookId: string, ratingNum: number) {
    const rating = {
        rating: ratingNum
    }
    return await axiosInstance
        .put(booksBasicUrl + bookId + "/rating", rating)
}

export async function getBookComments(bookId: string) {
    return await axiosInstance
        .get(booksBasicUrl + bookId + "/comments")
}

export async function addComment(bookId: string, commentData: CommentData) {
    return await axiosInstance
        .post(booksBasicUrl + bookId + "/comments", commentData)
}

export async function updateComment(commentId: string) {
    return await axiosInstance
        .put(extentedBooksUrl + commentId)
}

export async function deleteComment(commentId: string) {
    let username = getItemFromLocalStorage("username")
    return await axiosInstance
        .delete(extentedBooksUrl + commentId + "/users/" + username)
}


