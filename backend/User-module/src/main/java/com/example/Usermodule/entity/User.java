package com.example.Usermodule.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class User {
    private String username;
    private String email;
    private String name;
    private String surname;
    private Gender gender;
    private LocalDate birthDate;
    @ToString.Exclude
    private byte[] image;
}