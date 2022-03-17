package com.example.Authormodule.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class Author {
    private ObjectId id;
    private String name;
    private String surname;
    private String nationality;
    private Gender gender;
    private LocalDate birthDate;
    private boolean isAlive;
}
