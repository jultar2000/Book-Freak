package com.example.Authormodule.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class Author {
    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;
    private String name;
    private String surname;
    private String nationality;
    private Gender gender;
    private LocalDate birthDate;
    private boolean alive;
}