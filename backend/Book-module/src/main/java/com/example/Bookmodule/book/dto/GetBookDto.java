package com.example.Bookmodule.book.dto;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.entity.Genre;
import com.example.Bookmodule.book.entity.ReaderRating;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBookDto {

    private String oid;

    private int year;

    private int numberOfPages;

    private String title;

    private Genre genre;

    private String description;

    private Author author;

    private ReaderRating viewerRating;

    private double price;
}
