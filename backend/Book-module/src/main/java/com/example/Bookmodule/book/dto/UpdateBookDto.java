package com.example.Bookmodule.book.dto;

import com.example.Bookmodule.book.entity.Genre;
import com.example.Bookmodule.book.entity.ViewerRating;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateBookDto {
    private int numberOfPages;
    private String description;
    private Genre genre;
    private ViewerRating viewerRating;
}
