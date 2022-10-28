package com.example.Bookmodule.book.entity;

import com.example.Bookmodule.author.entity.Author;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private double price;

    private String title;

    private Genre genre;

    private Author author;

    private String description;

    private int numberOfPages;

    private ReaderRating viewerRating;
}
