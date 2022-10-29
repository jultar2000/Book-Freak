package com.example.Bookmodule.book.dto;

import com.example.Bookmodule.book.entity.ReaderRating;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateBookDto {

    private int numberOfPages;

    private String description;

    private double price;

    private String genre;

    private ReaderRating viewerRating;
}
