package com.example.Bookmodule.book.dto;

import com.example.Bookmodule.author.entity.Author;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookDto {
    private int year;
    private String title;
    private String description;
    private int numberOfPages;
    private String genre;
    private Author author;
}