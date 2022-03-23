package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookDto {
    private int year;
    private String title;
    private int numberOfPages;
}
