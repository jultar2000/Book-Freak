import { AuthorData } from "../Author/AuthorData";
import { ReaderRatingData } from "./ReaderRatingData";

export interface BookData {
    oid: string,
    year: number,
    numberOfPages: number,
    title: string,
    genre: string,
    description: string,
    price: number,
    author: AuthorData,
    readerRating: ReaderRatingData
}