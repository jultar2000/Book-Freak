package com.example.Bookmodule.author.entity;

import com.example.Bookmodule.book.entity.Book;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Author {
    @Id
    private Long id;
    private List<Book> books;
}
