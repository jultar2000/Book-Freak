package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksRequest {
    int limit;
    int skip;
}
