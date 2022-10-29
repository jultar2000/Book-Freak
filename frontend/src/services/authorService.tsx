import axiosInstance from "../utils/axiosInstance";

const basicAuthorUrl = "/author-module/api/v1/"

export async function getAuthorsByNationality(nationality: string) {
    axiosInstance
        .get(basicAuthorUrl + "nationality/" + nationality)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}

export async function getAuthor(authorId: string) {
    axiosInstance
        .get(basicAuthorUrl + authorId)
        .then((res) => {
            return res.data
        }).catch((err) => console.log(err))
}