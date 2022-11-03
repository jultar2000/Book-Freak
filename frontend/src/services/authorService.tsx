import axiosInstance from "../utils/axiosInstance";

const basicAuthorUrl = "/author-module/api/v1/authors/"

export async function getAuthorsByNationality(nationality: string) {
    return await axiosInstance
        .get(basicAuthorUrl + "nationality/" + nationality);
}

export async function getAuthor(authorId: string) {
    return await axiosInstance
        .get(basicAuthorUrl + authorId)
}

export async function getAllAuthors() {
    return await axiosInstance
        .get(basicAuthorUrl + "all")
}