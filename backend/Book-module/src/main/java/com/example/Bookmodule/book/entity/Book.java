package com.example.Bookmodule.book.entity;

import com.example.Bookmodule.author.entity.Author;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Book {
    @Id
    private Long id;
    private int year;
    private ViewerRating viewerRating;
    private String title;
    private String rates;
    private Author author;
    private List<String> comments;
}