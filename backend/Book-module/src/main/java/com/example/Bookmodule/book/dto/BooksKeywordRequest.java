package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksKeywordRequest {
    int limit;
    int skip;
    String keyword;
}
