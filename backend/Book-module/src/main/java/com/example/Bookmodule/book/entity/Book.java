package com.example.Bookmodule.book.entity;

import com.example.Bookmodule.author.entity.Author;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
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
    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;
    private int year;
    private String title;
    private int numberOfPages;
    private String description;
    private Author author;
    private ViewerRating viewerRating;
    private Genre genre;
    private List<Comment> comments;
}