package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksRatingRequest {
    int limit;
    int skip;
}
