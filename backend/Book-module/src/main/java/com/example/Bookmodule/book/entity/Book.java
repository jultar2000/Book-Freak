package com.example.Bookmodule.book.entity;

import com.example.Bookmodule.author.entity.Author;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class Book {
    private int year;
    private String title;
    private String rates;
    private Author author;
    private ViewerRating viewerRating;
    private List<String> comments;
}