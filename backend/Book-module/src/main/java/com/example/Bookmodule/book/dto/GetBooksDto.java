package com.example.Bookmodule.book.dto;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.entity.ViewerRating;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBooksDto {
    private int year;
    private String title;
    private int numberOfPages;
    private Author author;
    private ViewerRating viewerRating;
}