package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksStringParameterRequest {
    int limit;
    int skip;
    String parameter;
}
